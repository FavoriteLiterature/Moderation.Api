package lab.maxb.favlit_moderation.moderation.domain.gateways

import lab.maxb.favlit_moderation.moderation.domain.models.Draft
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface DraftsGateway {
    fun save(model: Draft): UUID
    fun deleteById(id: UUID)
    fun existsById(id: UUID): Boolean
    fun getById(id: UUID): Draft?
    fun getAll(page: Pageable): Page<Draft>
    fun getAllByAuthorId(authorId: UUID, page: Pageable): Page<Draft>
    fun verify(model: Draft)
}