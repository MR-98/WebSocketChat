package com.mr.websocket_chat.repository

import com.mr.websocket_chat.domain.jpa.AttachmentEntity
import com.mr.websocket_chat.domain.jpa.AttachmentIdAndS3KeyProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.sql.Timestamp

interface AttachmentRepository : JpaRepository<AttachmentEntity, Long> {

    @Query("SELECT id, s3key FROM attachments WHERE message_id is null and upload_timestamp < :timestamp", nativeQuery = true)
    fun findAllOrphanedAttachmentsBeforeTimestamp(
        @Param("timestamp") timestamp: Timestamp
    ): List<AttachmentIdAndS3KeyProjection>
}