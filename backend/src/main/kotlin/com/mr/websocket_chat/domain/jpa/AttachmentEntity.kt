package com.mr.websocket_chat.domain.jpa

import com.mr.websocket_chat.domain.enum.AttachmentType
import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "attachments")
data class AttachmentEntity(

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "message_id", nullable = true)
    var message: ChatMessageEntity?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: AttachmentType,

    @Column(nullable = false)
    val fileName: String,

    @Column(nullable = false)
    val s3Key: String,

    val size: Long? = null,

    val contentType: String? = null,

    val uploadTimestamp: Timestamp,

    val uploaderUsername: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
)


