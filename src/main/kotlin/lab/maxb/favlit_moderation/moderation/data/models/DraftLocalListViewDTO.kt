package lab.maxb.favlit_moderation.moderation.data.models

import lab.maxb.favlit_moderation.moderation.domain.models.Draft
import org.springframework.beans.factory.annotation.Value
import java.util.*

/**
 * A Projection for the {@link lab.maxb.favlit_moderation.moderation.data.model.Draft} entity
 */
interface DraftLocalListViewDTO {
    val id: UUID
    val name: String
    val genres: MutableList<UUID>
    val authorId: UUID
    val cover: AttachmentLocalDTO? @Value("#{target.attachments.size() == 0 ? null : (target.attachments.?[isCover].size() == 0 ? target.attachments[0] : target.attachments.?[isCover][0])}") get
}

fun DraftLocalListViewDTO.toDomain() = Draft(
    id = id,
    name = name,
    genres = genres,
    authorId = authorId,
    attachments = cover?.toDomain()?.let { listOf(it) } ?: emptyList()
)
