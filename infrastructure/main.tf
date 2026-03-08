terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

########################################
# S3 Bucket
########################################

resource "aws_s3_bucket" "chat_files" {
  bucket = var.bucket_name

  tags = {
    Name        = var.bucket_name
    Environment = var.environment
    Project     = "WebSocketChat"
  }
}

########################################
# Block Public Access
########################################

resource "aws_s3_bucket_public_access_block" "chat_files_block" {
  bucket = aws_s3_bucket.chat_files.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

########################################
# Server-Side Encryption (AES256)
########################################

resource "aws_s3_bucket_server_side_encryption_configuration" "chat_files_encryption" {
  bucket = aws_s3_bucket.chat_files.id

  rule {
    apply_server_side_encryption_by_default {
      sse_algorithm = "AES256"
    }
  }
}