package lab.maxb.favlit_moderation.moderation.presentation.services

import com.fasterxml.jackson.databind.ObjectMapper
import lab.maxb.favlit_moderation.moderation.domain.services.MQTTSender
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class RabbitSender @Autowired constructor(
    private val template: RabbitTemplate
) : MQTTSender {
    val queue = Queue("VerifiedWorks")
        @Bean get
    
    override fun <T> send(data: T) {
        val message = mapper.writeValueAsString(data)
        template.convertAndSend(queue.name, message)
    }

    companion object {
        private val mapper = ObjectMapper()
    }
}