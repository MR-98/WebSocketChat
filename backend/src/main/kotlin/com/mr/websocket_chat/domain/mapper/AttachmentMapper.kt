package com.mr.websocket_chat.domain.mapper

import com.mr.websocket_chat.domain.jpa.AttachmentEntity
import com.mr.websocket_chat.domain.rest.AttachmentDTO
import com.mr.websocket_chat.service.S3Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AttachmentMapper @Autowired constructor(
    private val s3Service: S3Service
){

    fun toDTO(entity: AttachmentEntity): AttachmentDTO {
        return AttachmentDTO(
            id = entity.id!!,
            type = entity.type,
            fileName = entity.fileName,
            url = s3Service.generatePresignedUrl(entity.s3Key),
            size = entity.size,
            contentType = entity.contentType
        )
    }
}