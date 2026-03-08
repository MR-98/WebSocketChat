package com.mr.websocket_chat.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
class AwsConfig(

    @Value("\${app.aws.s3.region}")
    private val s3Region: String
) {

    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder()
            .region(Region.of(s3Region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()
    }

    @Bean
    fun s3Presigner(): S3Presigner {
        return S3Presigner.builder()
            .region(Region.of(s3Region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()
    }
}