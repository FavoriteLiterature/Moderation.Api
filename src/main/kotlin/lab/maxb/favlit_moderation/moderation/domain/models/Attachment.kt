package lab.maxb.favlit_moderation.moderation.domain.models

import java.util.*

data class Attachment(
    val id: UUID = UUID.randomUUID(),
    val type: Type,
    val fileId: UUID,
    val draftId: UUID,
) {
    enum class Type {
        Image,
        Cover,
        Document,
    }
}

inline val Attachment.isCover get() = type == Attachment.Type.Cover