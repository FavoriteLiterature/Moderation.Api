package lab.maxb.favlit_moderation.moderation.presentation.model

import lab.maxb.favlit_moderation.moderation.domain.models.Draft
import lab.maxb.favlit_moderation.moderation.domain.models.isCover
import java.util.*

data class DraftNetworkListViewDTO(
    val id: UUID,
    val name: String,
    val genres: List<UUID>,
    val authorId: UUID,
    val cover: UUID?,
)

fun Draft.toNetworkListView() = DraftNetworkListViewDTO(
    id = id,
    name = name,
    genres = genres,
    authorId = authorId,
    cover = attachments.firstOrNull { it.isCover }?.fileId
)