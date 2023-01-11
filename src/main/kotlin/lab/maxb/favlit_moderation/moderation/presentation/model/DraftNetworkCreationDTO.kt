package lab.maxb.favlit_moderation.moderation.presentation.model

import lab.maxb.favlit_moderation.moderation.domain.models.Draft
import java.util.*

data class DraftNetworkCreationDTO(
    val name: String,
    val genres: List<UUID> = emptyList(),
    val description: String? = null,
    val attachments: List<AttachmentNetworkDTO> = emptyList(),
)

fun DraftNetworkCreationDTO.toDomain(authorId: UUID) = Draft(
    name = name,
    genres = genres,
    authorId = authorId,
    description = description,
    attachments = emptyList(),
).let { draft ->
    draft.copy(attachments = attachments.map { it.toDomain(draft.id) })
}