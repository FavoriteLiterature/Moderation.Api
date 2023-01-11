package lab.maxb.favlit_moderation.moderation.presentation.security

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import lab.maxb.favlit_moderation.moderation.domain.models.User
import java.util.*

object JWTUtils {
    private val mapper = ObjectMapper()

    private fun parse(token: String): String {
        val encodedPayload =
            token.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replace('-', '+')
                .replace('_', '/')
        return String(Base64.getDecoder().decode(encodedPayload))
    }

    fun extractUser(token: String) =
        parse(token).let(::extractAllClaims).toDomain()

    private fun extractAllClaims(token: String) = mapper.readValue(token, Token::class.java)
    
    private data class Roles(
        @JsonProperty("roles")
        val roles: List<String>,
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class ResourceAccess(
        @JsonProperty("Literature.Moderation.Api")
        val main: Roles,
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class Token(
        @JsonProperty("sub")
        val id: UUID,
        @JsonProperty("resource_access")
        val resourceAccess: ResourceAccess,
    ) {
        fun toDomain() = User(
            id = id,
            roles = resourceAccess.main.roles.map {
                it.uppercase()
                    .replace('-', '_')
                    .let(User.Role::valueOf)
            }
        )
    }
}