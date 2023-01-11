package lab.maxb.favlit_moderation.moderation.presentation.controllers

import lab.maxb.favlit_moderation.moderation.domain.exceptions.ForbiddenException
import lab.maxb.favlit_moderation.moderation.domain.exceptions.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
@RestController
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(NotFoundException::class)
    fun notFound(ex: NotFoundException) =
        ResponseEntity.notFound().build<Any>()

    @ExceptionHandler(ForbiddenException::class)
    fun forbidden(ex: ForbiddenException) =
        ResponseEntity.status(HttpStatus.FORBIDDEN).build<Any>()
}
