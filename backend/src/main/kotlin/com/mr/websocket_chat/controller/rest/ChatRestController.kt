package com.mr.websocket_chat.controller.rest

import com.mr.websocket_chat.domain.exception.ChatRoomNotFoundException
import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.rest.ChatMessageDTO
import com.mr.websocket_chat.domain.rest.LoadOldMessagesRequestDTO
import com.mr.websocket_chat.service.ChatMessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chat")
class ChatRestController @Autowired constructor(
    private val chatMessageService: ChatMessageService
){

    @PostMapping("/loadOldMessages")
    fun loadOldMessagesForRoom(@RequestBody body: LoadOldMessagesRequestDTO, authentication: Authentication): ResponseEntity<List<ChatMessageDTO>> {
        val currentlyAuthenticatedUsername = authentication.name
        try {
            return ResponseEntity.ok(
                chatMessageService.loadOlderMessagesForRoom(
                    currentlyAuthenticatedUsername,
                    body.roomId,
                    body.olderThanMessageId,
                    body.numberOfMessagesToLoad
                )
            )
        } catch (e: ChatRoomNotFoundException) {
            return ResponseEntity.notFound().build()
        } catch (e: UserNotFoundException) {
            return ResponseEntity.notFound().build()
        } catch (e: RuntimeException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }
}