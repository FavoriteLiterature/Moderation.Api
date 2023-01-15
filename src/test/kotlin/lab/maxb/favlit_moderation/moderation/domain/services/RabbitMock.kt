package lab.maxb.favlit_moderation.moderation.domain.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Primary
@Service
class RabbitMock : MQTTSender {
    override fun <T> send(data: T) {
        mapper.writeValueAsString(data)
        // Do nothing
    }

    companion object {
        private val mapper = ObjectMapper()
    }
}