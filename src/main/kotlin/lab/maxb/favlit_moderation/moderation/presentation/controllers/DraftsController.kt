package lab.maxb.favlit_moderation.moderation.presentation.controllers

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lab.maxb.favlit_moderation.SECURITY_SCHEME
import lab.maxb.favlit_moderation.core.presentation.models.PageNetworkDTO
import lab.maxb.favlit_moderation.core.presentation.models.Response
import lab.maxb.favlit_moderation.core.presentation.models.toNetwork
import lab.maxb.favlit_moderation.moderation.domain.exceptions.ForbiddenException
import lab.maxb.favlit_moderation.moderation.domain.models.Roles
import lab.maxb.favlit_moderation.moderation.domain.models.User
import lab.maxb.favlit_moderation.moderation.domain.services.DraftsService
import lab.maxb.favlit_moderation.moderation.presentation.model.*
import org.springdoc.core.converters.models.PageableAsQueryParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("drafts")
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@SecurityRequirement(name = SECURITY_SCHEME)
class DraftsController @Autowired constructor(
    private val service: DraftsService,
) {
    @GetMapping("/")
    @PageableAsQueryParam
    @RolesAllowed(Roles.READ_ALL_WORKS, Roles.READ_OWNED_WORKS)
    fun getDrafts(
        @RequestParam(required = false) authorId: UUID? = null,
        auth: Authentication,
        @Parameter(hidden = true) page: Pageable,
    ): PageNetworkDTO<DraftNetworkListViewDTO> {
        val user = auth.user
        val newAuthorId = if (user.roles.contains(User.Role.READ_ALL_WORKS))
            authorId else user.id
        val drafts = service.getDrafts(newAuthorId, page).map { it.toNetworkListView() }
        return drafts.toNetwork()
    }

    @PostMapping("/")
    @RolesAllowed(Roles.WORK_WITH_OWNED_WORKS)
    fun addDraft(@RequestBody draft: DraftNetworkCreationDTO, auth: Authentication) =
        Response(service.addDraft(draft.toDomain(auth.user.id)))

    @GetMapping("/{id}")
    @RolesAllowed(Roles.READ_ALL_WORKS, Roles.READ_OWNED_WORKS)
    fun getDraft(@PathVariable id: UUID, auth: Authentication) = service.getDraft(id).toNetwork().also {
        val user = auth.user
        if (it.authorId != user.id && !user.roles.contains(User.Role.READ_ALL_WORKS))
            throw ForbiddenException()
    }

    @PutMapping("/{id}")
    @RolesAllowed(Roles.WORK_WITH_OWNED_WORKS)
    fun updateDraft(
        @PathVariable id: UUID,
        @RequestBody draft: DraftNetworkCreationDTO,
        auth: Authentication,
    ) = service.updateDraft(draft.toDomain(auth.user.id).copy(id = id))

    @PostMapping("/{id}")
    @RolesAllowed(Roles.MODERATE_WORKS)
    fun verifyDraft(@PathVariable id: UUID, verified: Boolean) = service.verifyDraft(id, verified)
}
