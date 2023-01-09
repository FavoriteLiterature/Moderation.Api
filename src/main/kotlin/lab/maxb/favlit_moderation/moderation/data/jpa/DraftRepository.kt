package lab.maxb.favlit_moderation.moderation.data.jpa

import lab.maxb.favlit_moderation.moderation.data.models.DraftLocalDTO
import lab.maxb.favlit_moderation.moderation.data.models.DraftLocalListViewDTO
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DraftRepository : JpaRepository<DraftLocalDTO, UUID> {
    fun findByAuthorId(id: UUID): List<DraftLocalListViewDTO>
    fun findByOrderByIdAsc(): List<DraftLocalListViewDTO>
}