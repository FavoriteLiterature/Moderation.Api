package lab.maxb.favlit_moderation.moderation.presentation.model

import lab.maxb.favlit_moderation.moderation.domain.models.Attachment
import java.io.Serializable
import java.util.*

data class AttachmentNetworkDTO(
    val id: UUID? = null,
    val type: Attachment.Type,
    val fileId: UUID
) : Serializable

fun AttachmentNetworkDTO.toDomain(draftId: UUID) = Attachment(
    type = type,
    fileId = fileId,
    draftId = draftId,
).let {
    if (id == null)
        it
    else
        it.copy(id = id)
}

fun Attachment.toNetwork() = AttachmentNetworkDTO(
    id = id,
    type = type,
    fileId = fileId,
)