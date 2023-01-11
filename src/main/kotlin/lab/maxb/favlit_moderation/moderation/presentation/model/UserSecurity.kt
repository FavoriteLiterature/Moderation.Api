package lab.maxb.favlit_moderation.moderation.presentation.model

import lab.maxb.favlit_moderation.moderation.domain.models.User
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserSecurity(
    val id: String,
    private val authorities: MutableCollection<GrantedAuthority>
) : UserDetails {
    override fun getAuthorities() = authorities
    override fun getPassword() = ""
    override fun getUsername() = id
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}

fun User.toSecurity() = UserSecurity(
    id = id.toString(),
    authorities = roles.map { SimpleGrantedAuthority("ROLE_" + it.name) }.toMutableList()
)

val Authentication.user get() = principal as User