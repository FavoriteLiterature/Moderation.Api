package lab.maxb.favlit_moderation.moderation.domain.services

import lab.maxb.favlit_moderation.moderation.domain.exceptions.NotFoundException
import lab.maxb.favlit_moderation.moderation.domain.gateways.DraftsGateway
import lab.maxb.favlit_moderation.moderation.domain.models.Attachment
import lab.maxb.favlit_moderation.moderation.domain.models.Draft
import lab.maxb.favlit_moderation.moderation.domain.models.isCover
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

interface DraftsService {
    fun getDrafts(authorId: UUID?): List<Draft>
    fun addDraft(draft: Draft): UUID
    fun getDraft(id: UUID): Draft
    fun updateDraft(draft: Draft)
    fun verifyDraft(id: UUID, verified: Boolean)
}

@Service
class DraftsServiceImpl @Autowired constructor(
    private val drafts: DraftsGateway
) : DraftsService {
    override fun getDrafts(authorId: UUID?) = authorId?.let {
        drafts.getAllByAuthorId(it)
    } ?: drafts.getAll()

    override fun addDraft(draft: Draft): UUID {
        val validModel = validateModel(draft)
        return drafts.save(validModel)
    }

    override fun getDraft(id: UUID) = drafts.getById(id) ?: throw NotFoundException()

    override fun updateDraft(draft: Draft) {
        if (drafts.existsById(draft.id)) {
            val validModel = validateModel(draft)
            drafts.save(validModel)
        } else throw NotFoundException()
    }

    override fun verifyDraft(id: UUID, verified: Boolean) {
        if (!drafts.existsById(id))
            throw NotFoundException()
        if (verified)
            TODO("Send copy to works service")
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