package lab.maxb.favlit_moderation.moderation.domain.models

import java.util.*

data class User(
    val id: UUID,
    val roles: List<Role>
) {
    enum class Role {
        MODERATE_WORKS,
        READ_ALL_WORKS,
        READ_OWNED_WORKS,
        WORK_WITH_OWNED_WORKS,
    }
}

object Roles {
    const val MODERATE_WORKS = "MODERATE_WORKS"
    const val READ_ALL_WORKS = "READ_ALL_WORKS"
    const val READ_OWNED_WORKS = "READ_OWNED_WORKS"
    const val WORK_WITH_OWNED_WORKS = "WORK_WITH_OWNED_WORKS"
}