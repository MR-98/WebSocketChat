package com.mr.websocket_chat.controller.rest

import com.mr.websocket_chat.domain.exception.AttachmentNotFoundException
import com.mr.websocket_chat.domain.rest.AttachmentDTO
import com.mr.websocket_chat.domain.rest.GetDownloadURLResponseDTO
import com.mr.websocket_chat.service.AttachmentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/attachments")
class AttachmentRestController @Autowired constructor(
    private val attachmentService: AttachmentService
){

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        @RequestParam("attachments") attachments: List<MultipartFile>,
        @RequestParam("chatRoomId") chatRoomId: Long,
        @RequestParam("uploaderUsername") uploaderUsername: String,
    ): ResponseEntity<List<AttachmentDTO>> {
        try {
            val result = attachmentService.saveAttachments(attachments, chatRoomId, uploaderUsername)
            return ResponseEntity.ok().body(result)
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/download-url/{attachmentId}")
    fun getDownloadUrl(@PathVariable("attachmentId") attachmentId: Long): ResponseEntity<GetDownloadURLResponseDTO> {
        try {
            val url = attachmentService.getDownloadUrl(attachmentId)
            return ResponseEntity.ok().body(GetDownloadURLResponseDTO(url))
        } catch (e: AttachmentNotFoundException) {
            return ResponseEntity.notFound().build()
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().build()
        }
    }

    @DeleteMapping("/{attachmentId}")
    fun deleteAttachment(@PathVariable("attachmentId") attachmentId: Long): ResponseEntity<Void> {
        try {
            attachmentService.deleteAttachment(attachmentId)
            return ResponseEntity.ok().build()
        } catch (e: AttachmentNotFoundException) {
            // Attachment does not exist, no need to remove it, 200 is returned
            return ResponseEntity.ok().build()
        }
    }
}