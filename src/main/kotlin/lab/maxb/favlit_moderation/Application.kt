package lab.maxb.favlit_moderation

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

const val SECURITY_SCHEME = "Moderation API Security"

@SpringBootApplication
@OpenAPIDefinition(
    info = Info(
        title = "Favorite Literature Moderation API",
        version = "1.0",
        description = "Favorite Literature microservice for moderation"
    )
)
@SecurityScheme(
    name = SECURITY_SCHEME,
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
@ConfigurationPropertiesScan
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
