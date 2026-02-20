package com.mr.websocket_chat.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.time.Duration
import java.util.*

@Service
class S3Service @Autowired constructor(
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
    @Value("\${app.aws.s3.bucket-name}") private val bucketName: String
){

    fun uploadFile(file: MultipartFile, s3Key: String) {
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(s3Key)
            .contentType(file.contentType)
            .contentLength(file.size)
            .build()

        s3Client.putObject(
            request,
            RequestBody.fromBytes(file.bytes)
        )
    }

    fun generatePresignedUrl(s3Key: String): String {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(s3Key)
            .build()

        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(5))
            .getObjectRequest(getObjectRequest)
            .build()

        return s3Presigner.presignGetObject(presignRequest)
            .url()
            .toString()
    }

    fun generateS3Key(file: MultipartFile, chatRoomId: Long): String {
        val extension = file.originalFilename
            ?.substringAfterLast('.', "")
            ?.lowercase()

        return "chat/$chatRoomId/${UUID.randomUUID()}.$extension"
    }

    fun generateDownloadUrl(s3Key: String, fileName: String): String {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(s3Key)
            .responseContentDisposition("attachment; filename=\"${fileName}\"")
            .build()

        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(1))
            .getObjectRequest(getObjectRequest)
            .build()

        val presignedObject = s3Presigner.presignGetObject(presignRequest)

        return presignedObject.url().toString()
    }

    fun deleteFile(s3Key: String) {
        val request = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(s3Key)
            .build()

        s3Client.deleteObject(request)
    }
}