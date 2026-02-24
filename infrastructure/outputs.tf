output "bucket_name" {
  value = aws_s3_bucket.chat_files.bucket
}

output "bucket_arn" {
  value = aws_s3_bucket.chat_files.arn
}