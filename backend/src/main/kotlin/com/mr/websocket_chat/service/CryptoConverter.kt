package com.mr.websocket_chat.service

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


@Converter
@Component
class CryptoConverter @Autowired constructor(
	@Value("\${encryption.message.secret}")
	private val secretKey: String
): AttributeConverter<String, String> {

	companion object {
		private val ENC: String = "AES"
		private val ALGORITHM: String = "AES/ECB/PKCS5Padding"
	}

	override fun convertToDatabaseColumn(attribute: String): String {
		val key: Key = SecretKeySpec(secretKey.toByteArray(), ENC)
		val c: Cipher = Cipher.getInstance(ALGORITHM)
		c.init(Cipher.ENCRYPT_MODE, key)
		return Base64.getEncoder().encodeToString(c.doFinal(attribute.toByteArray()))
	}

	override fun convertToEntityAttribute(dbData: String): String {
		val key: Key = SecretKeySpec(secretKey.toByteArray(), ENC)
		val c: Cipher = Cipher.getInstance(ALGORITHM)
		c.init(Cipher.DECRYPT_MODE, key)
		return String(c.doFinal(Base64.getDecoder().decode(dbData)))
	}
}