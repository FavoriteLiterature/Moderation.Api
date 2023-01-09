package lab.maxb.favlit_moderation

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
@OpenAPIDefinition(
    info = Info(
        title = "Favorite Literature Moderation API",
        version = "1.0",
        description = "Favorite Literature microservice for moderation"
    )
)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
