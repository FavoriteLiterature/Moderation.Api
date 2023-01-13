package lab.maxb.favlit_moderation.moderation.domain.services

import lab.maxb.favlit_moderation.moderation.domain.exceptions.NotFoundException
import lab.maxb.favlit_moderation.moderation.domain.gateways.DraftsGateway
import lab.maxb.favlit_moderation.moderation.domain.models.Attachment
import lab.maxb.favlit_moderation.moderation.domain.models.Draft
import lab.maxb.favlit_moderation.moderation.domain.models.isCover
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

interface DraftsService {
    fun getDrafts(authorId: UUID?, page: Pageable): Page<Draft>
    fun addDraft(draft: Draft): UUID
    fun getDraft(id: UUID): Draft
    fun updateDraft(draft: Draft)
    fun verifyDraft(id: UUID, verified: Boolean)
}

@Service
class DraftsServiceImpl @Autowired constructor(
    private val drafts: DraftsGateway,
) : DraftsService {
    override fun getDrafts(authorId: UUID?, page: Pageable) = authorId?.let {
        drafts.getAllByAuthorId(it, page)
    } ?: drafts.getAll(page)

    override fun addDraft(draft: Draft): UUID {
        val validModel = validateModel(draft)
        return drafts.save(validModel)
    }

    override fun getDraft(id: UUID) = drafts.getById(id) ?: throw NotFoundException()

    override fun updateDraft(draft: Draft) {
        val oldDraft = drafts.getById(draft.id) ?: throw NotFoundException()
        val validModel = validateModel(
            draft.copy(authorId = oldDraft.authorId)
        )
        drafts.save(validModel)
    }

    override fun verifyDraft(id: UUID, verified: Boolean) {
        val draft = drafts.getById(id) ?: throw NotFoundException()
        if (verified)
            drafts.verify(draft)
        else
            drafts.deleteById(id)
    }

    private fun validateModel(draft: Draft) = draft.let(::validateAttachments)

    private fun validateAttachments(draft: Draft) =
        if (draft.attachments.isEmpty() || 1 == draft.attachments.count { it.isCover })
            draft
        else draft.copy(attachments = getValidAttachments(draft.attachments))

    private fun getValidAttachments(attachments: List<Attachment>): List<Attachment> {
        val firstCover = attachments.indexOfFirst { it.isCover }.let {
            if (it == -1) 0
            else it
        }
        return attachments.mapIndexed { i, it ->
            when {
                i == firstCover -> it.copy(type = Attachment.Type.Cover)
                it.isCover -> it.copy(type = Attachment.Type.Image)
                else -> it
            }
        }
    }
}