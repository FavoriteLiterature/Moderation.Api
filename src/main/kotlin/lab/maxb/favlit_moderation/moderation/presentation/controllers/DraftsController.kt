package lab.maxb.favlit_moderation.moderation.presentation.controllers

import lab.maxb.favlit_moderation.core.presentation.models.Response
import lab.maxb.favlit_moderation.moderation.domain.services.DraftsService
import lab.maxb.favlit_moderation.moderation.presentation.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("drafts")
class DraftsController @Autowired constructor(
    private val service: DraftsService,
) {
    // TODO: use pagination
    // TODO: Enable token reads
    // TODO: Use token sub as id on creation/update
    // TODO: Check owner on update, get, get multiple

    @GetMapping("/")
    fun getDrafts(@RequestParam(required = false) authorId: UUID? = null) =
        Response(service.getDrafts(authorId).map { it.toNetworkListView() })

    @PostMapping("/")
    fun addDraft(@RequestBody draft: DraftNetworkCreationDTO) = Response(service.addDraft(draft.toDomain()))

    @GetMapping("/{id}")
    fun getDraft(@PathVariable id: UUID) = service.getDraft(id).toNetwork()

    @PutMapping("/{id}")
    fun updateDraft(@PathVariable id: UUID, @RequestBody draft: DraftNetworkCreationDTO) =
        service.updateDraft(draft.toDomain().copy(id = id))

    @PostMapping("/{id}")
    fun verifyDraft(@PathVariable id: UUID, verified: Boolean) = service.verifyDraft(id, verified)
}
