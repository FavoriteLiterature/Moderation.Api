package lab.maxb.favlit_moderation.moderation.domain.services

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import lab.maxb.favlit_moderation.core.presentation.models.toNetwork
import lab.maxb.favlit_moderation.moderation.domain.exceptions.ForbiddenException
import lab.maxb.favlit_moderation.moderation.domain.exceptions.NotFoundException
import lab.maxb.favlit_moderation.moderation.domain.gateways.DraftsGateway
import lab.maxb.favlit_moderation.moderation.domain.models.Attachment
import lab.maxb.favlit_moderation.moderation.domain.models.Draft
import lab.maxb.favlit_moderation.moderation.presentation.model.toNetworkListView
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import java.util.*


@SpringBootTest
@AutoConfigureEmbeddedDatabase
internal class DraftsServiceTest @Autowired constructor(
    private val service: DraftsService,
    private val gateway: DraftsGateway,
) {
    private val newUUID get() = UUID.randomUUID()

    private fun getUUIDs(count: Int) = buildList {
        repeat(count) {
            add(newUUID)
        }
    }

    @BeforeEach
    fun setUp() {
        gateway.getAll(Pageable.unpaged()).forEach {
            gateway.deleteById(it.id)
        }
    }

    @Test
    fun getAllDrafts() {
        val uuids = getUUIDs(2)
        val models = buildSet {
            Draft(
                id = uuids[0],
                name = "tEst 1",
                authorId = newUUID,
                description = "Some test instance",
                genres = listOf(newUUID, newUUID),
                attachments = listOf(
                    Attachment(
                        type = Attachment.Type.Cover,
                        fileId = newUUID,
                        draftId = uuids[0]
                    ),
                    Attachment(
                        type = Attachment.Type.Document,
                        fileId = newUUID,
                        draftId = uuids[0]
                    ),
                )
            ).also { add(it) }
            Draft(
                id = uuids[1],
                name = "tEst 2",
                authorId = newUUID,
                description = "Some test 2 instance",
                genres = listOf(newUUID, newUUID),
                attachments = listOf(
                    Attachment(
                        type = Attachment.Type.Cover,
                        fileId = newUUID,
                        draftId = uuids[1]
                    ),
                    Attachment(
                        type = Attachment.Type.Document,
                        fileId = newUUID,
                        draftId = uuids[1]
                    ),
                )
            ).also { add(it) }
        }
        models.forEach { gateway.save(it) }
        val expectedContent = models.map { it.toNetworkListView() }.toSet()
        val actualContent = service.getDrafts(null, page = Pageable.unpaged())
            .toNetwork().content.map { it.toNetworkListView() }.toSet()
        assertEquals(expectedContent, actualContent)
    }

    @Test
    fun getAuthorDrafts() {
        val uuids = getUUIDs(5)
        val filteredAuthorId = uuids[3]
        val models = buildSet {
            Draft(
                id = uuids[0],
                name = "tEst 1",
                authorId = filteredAuthorId,
                description = "Some test instance",
                genres = listOf(newUUID, newUUID),
                attachments = listOf(
                    Attachment(
                        type = Attachment.Type.Cover,
                        fileId = newUUID,
                        draftId = uuids[0]
                    ),
                    Attachment(
                        type = Attachment.Type.Document,
                        fileId = newUUID,
                        draftId = uuids[0]
                    ),
                )
            ).also { add(it) }
            Draft(
                id = uuids[1],
                name = "tEst 2",
                authorId = filteredAuthorId,
                description = "Some test 2 instance",
                genres = listOf(newUUID, newUUID),
                attachments = listOf(
                    Attachment(
                        type = Attachment.Type.Cover,
                        fileId = newUUID,
                        draftId = uuids[1]
                    ),
                    Attachment(
                        type = Attachment.Type.Document,
                        fileId = newUUID,
                        draftId = uuids[1]
                    ),
                )
            ).also { add(it) }
            Draft(
                id = uuids[2],
                name = "tEst 3",
                authorId = uuids[4],
                description = "Some test 3 instance",
                genres = listOf(newUUID, newUUID),
                attachments = listOf(
                    Attachment(
                        type = Attachment.Type.Cover,
                        fileId = newUUID,
                        draftId = uuids[2]
                    ),
                    Attachment(
                        type = Attachment.Type.Document,
                        fileId = newUUID,
                        draftId = uuids[2]
                    ),
                )
            ).also { add(it) }
        }
        models.forEach { gateway.save(it) }
        val expectedContent = models.filter { it.authorId == filteredAuthorId }.map { it.toNetworkListView() }.toSet()
        val actualContent = service.getDrafts(filteredAuthorId, page = Pageable.unpaged())
            .toNetwork().content.map { it.toNetworkListView() }.toSet()
        assertEquals(expectedContent, actualContent)
    }

    @Test
    fun addDraft() {
        val draftId = newUUID
        val draft = Draft(
            id = draftId,
            name = "tEst 1",
            authorId = newUUID,
            description = "Some test instance",
            genres = listOf(newUUID, newUUID),
            attachments = listOf(
                Attachment(
                    type = Attachment.Type.Cover,
                    fileId = newUUID,
                    draftId = draftId
                ),
                Attachment(
                    type = Attachment.Type.Document,
                    fileId = newUUID,
                    draftId = draftId
                ),
            )
        )
        service.addDraft(draft)
        assertEquals(draft, gateway.getById(draftId))
    }

    @Test
    fun addDraftWithNoCover() {
        val draftId = newUUID
        val draft = Draft(
            id = draftId,
            name = "tEst 1",
            authorId = newUUID,
            description = "Some test instance",
            genres = listOf(newUUID, newUUID),
            attachments = listOf(
                Attachment(
                    type = Attachment.Type.Image,
                    fileId = newUUID,
                    draftId = draftId
                ),
                Attachment(
                    type = Attachment.Type.Image,
                    fileId = newUUID,
                    draftId = draftId
                ),
            )
        )
        service.addDraft(draft)
        assertEquals(
            listOf(Attachment.Type.Cover, Attachment.Type.Image),
            gateway.getById(draftId)!!.attachments.map { it.type },
        )
    }

    @Test
    fun addDraftWithDuplicatedCover() {
        val draftId = newUUID
        val draft = Draft(
            id = draftId,
            name = "tEst 1",
            authorId = newUUID,
            description = "Some test instance",
            genres = listOf(newUUID, newUUID),
            attachments = listOf(
                Attachment(
                    type = Attachment.Type.Cover,
                    fileId = newUUID,
                    draftId = draftId
                ),
                Attachment(
                    type = Attachment.Type.Image,
                    fileId = newUUID,
                    draftId = draftId
                ),
                Attachment(
                    type = Attachment.Type.Cover,
                    fileId = newUUID,
                    draftId = draftId
                ),
            )
        )
        service.addDraft(draft)
        assertEquals(
            listOf(Attachment.Type.Cover, Attachment.Type.Image, Attachment.Type.Image),
            gateway.getById(draftId)!!.attachments.map { it.type },
        )
    }

    @Test
    fun addDraftWithNoImages() {
        val draftId = newUUID
        val draft = Draft(
            id = draftId,
            name = "tEst 1",
            authorId = newUUID,
            description = "Some test instance",
            genres = listOf(newUUID, newUUID),
            attachments = listOf(
                Attachment(
                    type = Attachment.Type.Document,
                    fileId = newUUID,
                    draftId = draftId
                ),
            )
        )
        service.addDraft(draft)
        val actualDraft = gateway.getById(draftId)!!
        assertEquals(draft, actualDraft)
        assertNull(actualDraft.toNetworkListView().cover)
    }

    @Test
    fun getDraft() {
        val draftId = newUUID
        val draft = Draft(
            id = draftId,
            name = "tEst 1",
            authorId = newUUID,
            description = "Some test instance",
            genres = listOf(newUUID, newUUID),
            attachments = listOf(
                Attachment(
                    type = Attachment.Type.Cover,
                    fileId = newUUID,
                    draftId = draftId
                ),
                Attachment(
                    type = Attachment.Type.Document,
                    fileId = newUUID,
                    draftId = draftId
                ),
            )
        )
        service.addDraft(draft)
        assertEquals(draft, service.getDraft(draftId))
        assertThrows<NotFoundException> {
            service.getDraft(newUUID)
        }
    }

    @Test
    fun updateDraft() {
        val draftId = newUUID
        val draft = Draft(
            id = draftId,
            name = "tEst 1",
            authorId = newUUID,
            description = "Some test instance",
            genres = listOf(newUUID, newUUID),
            attachments = listOf(
                Attachment(
                    type = Attachment.Type.Cover,
                    fileId = newUUID,
                    draftId = draftId
                ),
                Attachment(
                    type = Attachment.Type.Document,
                    fileId = newUUID,
                    draftId = draftId
                ),
            )
        )
        service.addDraft(draft)
        val expectedDraft = draft.copy(name = "Test")
        val newDraft = expectedDraft.copy(
            attachments = draft.attachments.mapIndexed { i, it ->
                if (i == 0)
                    it.copy(type = Attachment.Type.Image)
                else it
            }
        )
        service.updateDraft(newDraft)
        assertEquals(expectedDraft, service.getDraft(draftId))
        assertThrows<ForbiddenException> {
            service.updateDraft(draft.copy(authorId = newUUID))
        }
        assertThrows<NotFoundException> {
            service.updateDraft(draft.copy(id = newUUID))
        }
    }

    @Test
    fun verifyAcceptDraft() {
        val draftId = newUUID
        val draft = Draft(
            id = draftId,
            name = "tEst 1",
            authorId = newUUID,
            description = "Some test instance",
            genres = listOf(newUUID, newUUID),
            attachments = listOf(
                Attachment(
                    type = Attachment.Type.Cover,
                    fileId = newUUID,
                    draftId = draftId
                ),
                Attachment(
                    type = Attachment.Type.Document,
                    fileId = newUUID,
                    draftId = draftId
                ),
            )
        )
        service.addDraft(draft)
        assertThrows<NotFoundException> {
            service.verifyDraft(newUUID, true)
        }
        service.verifyDraft(draftId, true)
        assertNull(gateway.getById(draftId))
    }

    @Test
    fun verifyDeclineDraft() {
        val draftId = newUUID
        val draft = Draft(
            id = draftId,
            name = "tEst 1",
            authorId = newUUID,
            description = "Some test instance",
            genres = listOf(newUUID, newUUID),
            attachments = listOf(
                Attachment(
                    type = Attachment.Type.Cover,
                    fileId = newUUID,
                    draftId = draftId
                ),
                Attachment(
                    type = Attachment.Type.Document,
                    fileId = newUUID,
                    draftId = draftId
                ),
            )
        )
        service.addDraft(draft)
        service.verifyDraft(draftId, false)
        assertNull(gateway.getById(draftId))
    }
}