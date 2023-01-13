package lab.maxb.favlit_moderation.core.presentation.models

import org.springframework.data.domain.Page


data class PageNetworkDTO<T>(
    val content: List<T>,
    val count: Int,
    val totalCount: Long,
    val isLast: Boolean,
)

fun <T> Page<T>.toNetwork() = PageNetworkDTO(
    content = content,
    count = numberOfElements,
    totalCount = totalElements,
    isLast = isLast,
)
