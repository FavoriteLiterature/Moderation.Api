package lab.maxb.favlit_moderation.moderation.presentation.model

import lab.maxb.favlit_moderation.moderation.domain.models.Draft
import java.io.Serializable
import java.util.*


data class DraftNetworkDTO(
    val id: UUID,
    val name: String,
    val authorId: UUID,
    val description: String? = null,
    val genres: MutableList<UUID> = mutableListOf(),
    val attachments: MutableList<AttachmentNetworkDTO> = mutableListOf(),
) : Serializable


fun Draft.toNetwork() = DraftNetworkDTO(
    id = id,
    name = name,
    authorId = authorId,
    description = description,
    genres = genres.toMutableList(),
    attachments = attachments.map { it.toNetwork() }.toMutableList()
)
