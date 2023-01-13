package lab.maxb.favlit_moderation.moderation.domain.services

interface MQTTSender {
    fun <T> send(data: T)
}