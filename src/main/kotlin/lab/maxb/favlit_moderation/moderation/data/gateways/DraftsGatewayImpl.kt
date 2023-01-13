package lab.maxb.favlit_moderation.moderation.data.gateways

import lab.maxb.favlit_moderation.moderation.data.jpa.DraftRepository
import lab.maxb.favlit_moderation.moderation.data.models.toDomain
import lab.maxb.favlit_moderation.moderation.data.models.toLocal
import lab.maxb.favlit_moderation.moderation.domain.gateways.DraftsGateway
import lab.maxb.favlit_moderation.moderation.domain.models.Draft
import lab.maxb.favlit_moderation.moderation.domain.services.MQTTSender
import lab.maxb.favlit_moderation.moderation.presentation.model.toNetwork
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class DraftsGatewayImpl @Autowired constructor(
    private val drafts: DraftRepository,
    private val mqttSender: MQTTSender,
) : DraftsGateway {
    @Transactional
    override fun save(model: Draft): UUID {
        val draft = model.toLocal()
        if (drafts.existsById(draft.id))
            drafts.deleteById(draft.id)
        val newDraft = drafts.save(draft)
        return newDraft.id
    }

    override fun deleteById(id: UUID) = drafts.deleteById(id)

    override fun existsById(id: UUID) = drafts.existsById(id)

    override fun getById(id: UUID) = drafts.findByIdOrNull(id)?.toDomain()

    override fun getAll() = drafts.findByOrderByIdAsc().map { it.toDomain() }

    override fun getAllByAuthorId(authorId: UUID) = drafts.findByAuthorId(authorId).map { it.toDomain() }

    override fun verify(model: Draft) {
        mqttSender.send(model.toNetwork())
        deleteById(model.id)
    }
}