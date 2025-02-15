package com.mr.websocket_chat.controller.rest

import com.mr.websocket_chat.domain.jpa.ChatMessageEntity
import com.mr.websocket_chat.domain.rest.LoadOldMessagesRequestBody
import com.mr.websocket_chat.service.ChatMessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chat")
class ChatRestController @Autowired constructor(
    private val chatMessageService: ChatMessageService
){

    @PostMapping("/loadOldMessages")
    fun loadOldMessagesForRoom(@RequestBody body: LoadOldMessagesRequestBody): ResponseEntity<List<ChatMessageEntity>> {
        return ResponseEntity.ok(
            chatMessageService.loadOlderMessagesForRoom(
                body.roomId,
                body.olderThanMessageId,
                body.numberOfMessagesToLoad
            )
        )
    }
}