package lab.maxb.favlit_moderation.moderation.domain.models

import java.util.*

data class Draft(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val genres: List<UUID> = emptyList(),
    val authorId: UUID,
    val description: String? = null,
    val attachments: List<Attachment> = emptyList(),
)
