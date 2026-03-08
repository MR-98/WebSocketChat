package com.mr.websocket_chat.scheduled

import com.mr.websocket_chat.repository.AttachmentRepository
import com.mr.websocket_chat.service.S3Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit


@Component
class AttachmentsCleanupCron @Autowired constructor(
    private val attachmentRepository: AttachmentRepository,
    private val s3Service: S3Service,
){

    @Scheduled(cron = "\${attachments.cleanup.cron}")
    @Transactional
    fun attachmentsCleanup() {
        val result = attachmentRepository.findAllOrphanedAttachmentsBeforeTimestamp(
            Timestamp.from(
                Instant.now().minus(1, ChronoUnit.DAYS)
            )
        )

        val attachmentIds = result.map { it.getId() }
        val s3Keys = result.map { it.getS3Key() }
        attachmentRepository.deleteAllByIdInBatch(attachmentIds)
        s3Service.deleteFiles(s3Keys)
    }
}