package lab.maxb.favlit_moderation.moderation.domain.gateways

import lab.maxb.favlit_moderation.moderation.domain.models.Draft
import java.util.*

interface DraftsGateway {
    fun save(model: Draft): UUID
    fun deleteById(id: UUID)
    fun existsById(id: UUID): Boolean
    fun getById(id: UUID): Draft?
    fun getAll(): List<Draft>
    fun getAllByAuthorId(authorId: UUID): List<Draft>
    fun verify(model: Draft)
}