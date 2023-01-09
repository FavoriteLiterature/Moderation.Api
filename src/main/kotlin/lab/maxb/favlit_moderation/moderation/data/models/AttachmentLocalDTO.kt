package lab.maxb.favlit_moderation.moderation.data.models

import lab.maxb.favlit_moderation.moderation.domain.models.Attachment
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "attachment")
open class AttachmentLocalDTO {
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID = UUID.randomUUID()

    @Version
    @Column(name = "version")
    open var version: Int? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    open lateinit var type: Attachment.Type

    @Column(name = "file_id", nullable = false)
    open lateinit var fileId: UUID

    @ManyToOne
    @JoinColumn(name = "draft_id", nullable = false)
    open lateinit var draft: DraftLocalDTO

    val isCover @Transient get() = type == Attachment.Type.Cover
}

fun AttachmentLocalDTO.toDomain() = Attachment(
    id = id,
    type = type,
    fileId = fileId,
    draftId = draft.id,
)

fun Attachment.toLocal(draft: DraftLocalDTO) = AttachmentLocalDTO().apply {
    val model = this@toLocal
    id = model.id
    type = model.type
    fileId = model.fileId
    assert(draft.id == model.draftId)
    this.draft = draft
}