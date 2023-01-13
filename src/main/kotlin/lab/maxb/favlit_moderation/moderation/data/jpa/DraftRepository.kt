package lab.maxb.favlit_moderation.moderation.data.jpa

import lab.maxb.favlit_moderation.moderation.data.models.DraftLocalDTO
import lab.maxb.favlit_moderation.moderation.data.models.DraftLocalListViewDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DraftRepository : JpaRepository<DraftLocalDTO, UUID> {
    fun findByAuthorId(id: UUID, page: Pageable): Page<DraftLocalListViewDTO>
    fun findByOrderByIdAsc(page: Pageable): Page<DraftLocalListViewDTO>
}