package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.enum.AttachmentType
import com.mr.websocket_chat.domain.exception.AttachmentNotFoundException
import com.mr.websocket_chat.domain.exception.AttachmentTooBigException
import com.mr.websocket_chat.domain.exception.UnsupportedAttachmentExtensionException
import com.mr.websocket_chat.domain.jpa.AttachmentEntity
import com.mr.websocket_chat.domain.mapper.AttachmentMapper
import com.mr.websocket_chat.repository.AttachmentRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.Instant
import kotlin.test.Test

class AttachmentServiceTest {
    @MockK
    private val attachmentRepository: AttachmentRepository = mockk()

    @MockK
    private val attachmentMapper: AttachmentMapper = mockk()

    @MockK
    private val s3Service: S3Service = mockk()

    @InjectMockKs
    private val attachmentService: AttachmentService = AttachmentService(attachmentRepository, attachmentMapper, s3Service)

    private lateinit var validAttachment: MultipartFile
    private lateinit var tooBigAttachment: MultipartFile
    private lateinit var unsupportedExtensionAttachment: MultipartFile

    private lateinit var attachmentEntity: AttachmentEntity

    @BeforeEach
    fun setUp() {
        attachmentEntity = AttachmentEntity(
            message = null,
            type = AttachmentType.IMAGE,
            fileName = "image.jpg",
            s3Key = "test",
            uploadTimestamp = Timestamp.from(Instant.now()),
            uploaderUsername = "testUser",
            id = 1L
        )
        validAttachment = mockk()
        tooBigAttachment = mockk()
        unsupportedExtensionAttachment = mockk()

        every { validAttachment.originalFilename } returns "image.jpg"
        every { validAttachment.contentType } returns "image/jpg"
        every { validAttachment.isEmpty } returns false
        every { validAttachment.bytes } returns "hello".toByteArray()
        every { validAttachment.size } returns 5

        every { tooBigAttachment.originalFilename } returns "file.pdf"
        every { tooBigAttachment.contentType } returns "application/pdf"
        every { tooBigAttachment.isEmpty } returns false
        every { tooBigAttachment.bytes } returns ByteArray(6 * 1024 * 1024)
        every { tooBigAttachment.size } returns 6 * 1024 * 1024

        every { unsupportedExtensionAttachment.originalFilename } returns "file.test"
        every { unsupportedExtensionAttachment.contentType } returns "application/octet-stream"
        every { unsupportedExtensionAttachment.isEmpty } returns false
        every { unsupportedExtensionAttachment.bytes } returns ByteArray(1 * 1024 * 1024)
        every { unsupportedExtensionAttachment.size } returns 1 * 1024 * 1024

        every { s3Service.generateS3Key(validAttachment, 1L) } returns "test"
        every { s3Service.uploadFile(validAttachment, "test") } returns Unit

        every { attachmentRepository.saveAll(ofType<List<AttachmentEntity>>()) } returns listOf()
    }

    @Test
    fun whenSavingAttachments_thenValidateAttachmentsExtensions() {
        // given
        val chatRoomId = 1L
        val testUsername = "testUser"

        // when
        val action = {
            attachmentService.saveAttachments(
                listOf(validAttachment, unsupportedExtensionAttachment),
                chatRoomId,
                testUsername
            )
        }

        // then
        assertThrows<UnsupportedAttachmentExtensionException> {
            action()
        }
    }

    @Test
    fun whenSavingAttachments_thenValidateAttachmentsSize() {
        // given
        val chatRoomId = 1L
        val testUsername = "testUser"

        // when
        val action = {
            attachmentService.saveAttachments(
                listOf(validAttachment, tooBigAttachment),
                chatRoomId,
                testUsername
            )
        }

        // then
        assertThrows<AttachmentTooBigException> {
            action()
        }
    }

    @Test
    fun whenSavingValidAttachments_thenS3ServiceShouldGenerateS3Key() {
        // given
        val chatRoomId = 1L
        val testUsername = "testUser"

        // when
        attachmentService.saveAttachments(listOf(validAttachment), chatRoomId, testUsername)

        // then
        verify { s3Service.generateS3Key(validAttachment, chatRoomId) }
    }

    @Test
    fun whenSavingValidAttachments_thenS3ServiceShouldUploadFile() {
        // given
        val chatRoomId = 1L
        val testUsername = "testUser"
        val s3Key = "test"

        // when
        attachmentService.saveAttachments(listOf(validAttachment), chatRoomId, testUsername)

        // then
        verify { s3Service.uploadFile(validAttachment, s3Key) }
    }

    @Test
    fun whenSavingValidAttachmentsList_thenShouldSaveAttachment() {
        // given
        val chatRoomId = 1L
        val testUsername = "testUser"

        // when
        attachmentService.saveAttachments(listOf(validAttachment), chatRoomId, testUsername)

        // then
        verify { attachmentRepository.saveAll(ofType<List<AttachmentEntity>>()) }
    }

    @Test
    fun whenGettingDownloadURLForNonExistingAttachment_thenThrowException() {
        // given
        val attachmentId = 1L
        every { attachmentRepository.findByIdOrNull(attachmentId) } returns null

        // when
        val action = {
            attachmentService.getDownloadUrl(attachmentId)
        }

        // then
        assertThrows<AttachmentNotFoundException> {
            action()
        }
    }

    @Test
    fun whenTryingToDeleteNonExistingAttachment_thenThrowException() {
        // given
        val attachmentId = 1L
        every { attachmentRepository.findByIdOrNull(attachmentId) } returns null

        // when
        val action = {
            attachmentService.deleteAttachment(attachmentId)
        }

        // then
        assertThrows<AttachmentNotFoundException> {
            action()
        }
    }

}