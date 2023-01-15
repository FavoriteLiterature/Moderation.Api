package lab.maxb.favlit_moderation.moderation.data.models

import lab.maxb.favlit_moderation.moderation.domain.models.Draft
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "draft")
open class DraftLocalDTO {
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID = UUID.randomUUID()

    @Version
    @Column(name = "version")
    open var version: Int? = null
        protected set

    @Column(name = "name", nullable = false)
    open lateinit var name: String

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "draft_genres", joinColumns = [JoinColumn(name = "draft_id")])
    @Column(name = "genre")
    open var genres: MutableList<UUID> = mutableListOf()

    @Column(name = "author_id", nullable = false)
    open lateinit var authorId: UUID

    @Column(name = "description", length = 2000)
    open var description: String? = null

    @OneToMany(mappedBy = "draft", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    open var attachments: MutableList<AttachmentLocalDTO> = mutableListOf()
}

fun DraftLocalDTO.toDomain() = Draft(
    id = id,
    name = name,
    genres = genres,
    authorId = authorId,
    description = description,
    attachments = attachments.map { it.toDomain() },
)

fun Draft.toLocal() = DraftLocalDTO().apply {
    val draft = this@toLocal
    id = draft.id
    name = draft.name
    genres = draft.genres.toMutableList()
    authorId = draft.authorId
    description = draft.description
    attachments = draft.attachments.map { it.toLocal(this) }.toMutableList()
}
