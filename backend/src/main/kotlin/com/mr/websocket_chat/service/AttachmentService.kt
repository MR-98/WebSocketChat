package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.enum.AttachmentType
import com.mr.websocket_chat.domain.exception.AttachmentNotFoundException
import com.mr.websocket_chat.domain.jpa.AttachmentEntity
import com.mr.websocket_chat.domain.mapper.AttachmentMapper
import com.mr.websocket_chat.domain.rest.AttachmentDTO
import com.mr.websocket_chat.repository.AttachmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.Instant

@Service
class AttachmentService @Autowired constructor(
    val attachmentRepository: AttachmentRepository,
    private val attachmentMapper: AttachmentMapper,
    private val s3Service: S3Service
) {

    @Transactional
    fun saveAttachments(
        attachments: List<MultipartFile>,
        chatRoomId: Long,
        uploaderUsername: String
    ): List<AttachmentDTO> {
        val attachmentsToSave = attachments.map {
            val s3Key = s3Service.generateS3Key(
                file = it,
                chatRoomId = chatRoomId
            )
            s3Service.uploadFile(it, s3Key)
            AttachmentEntity(
                message = null,
                type = resolveAttachmentType(it),
                fileName = it.originalFilename ?: "Filename",
                s3Key = s3Key,
                size = it.size,
                contentType = it.contentType,
                uploadTimestamp = Timestamp.from(Instant.now()),
                uploaderUsername = uploaderUsername,
            )
        }
        return attachmentRepository.saveAll(attachmentsToSave).map { attachmentMapper.toDTO(it) }
    }

    private fun resolveAttachmentType(file: MultipartFile): AttachmentType {
        val contentType = file.contentType?.lowercase() ?: ""
        return if (contentType.startsWith("image/")) {
            AttachmentType.IMAGE
        } else {
            AttachmentType.FILE
        }
    }

    fun getDownloadUrl(attachmentId: Long): String {
        val attachment = attachmentRepository.findByIdOrNull(attachmentId) ?: throw AttachmentNotFoundException()
        val s3Key = attachment.s3Key
        val filename = attachment.fileName
        return s3Service.generateDownloadUrl(s3Key, filename)
    }

    fun deleteAttachment(attachmentId: Long) {
        val attachment = attachmentRepository.findByIdOrNull(attachmentId) ?: throw AttachmentNotFoundException()
        attachmentRepository.deleteById(attachmentId)
        s3Service.deleteFile(attachment.s3Key)
    }
}