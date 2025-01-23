import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ChatRoom } from "../../model/chat-room";
import { DataStoreService } from "../../service/data-store.service";
import { NgClass, NgIf } from "@angular/common";
import { WebSocketService } from "../../service/web-socket.service";
import { ChatMessage } from "../../model/chat-message";
import { StompSubscription } from "@stomp/stompjs";

@Component({
  selector: 'app-chat-room-list-element',
  standalone: true,
  imports: [
    NgClass,
    NgIf
  ],
  templateUrl: './chat-room-list-element.component.html',
  styleUrl: './chat-room-list-element.component.scss'
})
export class ChatRoomListElementComponent implements OnInit, OnDestroy{

  @Input() chatRoom: ChatRoom = {
    name: "",
    users: [],
    id: 0
  }

  protected lastMessage: ChatMessage | undefined;
  private currentSubscription: StompSubscription | undefined;

  constructor(
    private dataStoreService: DataStoreService,
    private webSocketService: WebSocketService
  ) {}

  ngOnInit() {
    this.currentSubscription = this.webSocketService.subscribeToRoom(
      this.chatRoom.id!!,
      (message: ChatMessage[] | ChatMessage) => {
        if (Array.isArray(message) && message.length > 0) {
          this.lastMessage = <ChatMessage>(message.shift());
        } else {
          this.lastMessage = <ChatMessage>message;
        }
      }
    )
  }

  isCurrentlySelectedChatRoom() {
    return this.dataStoreService.getCurrentlySelectedChatRoom()?.id == this.chatRoom.id
  }

  getLastMessageText() {
    if (
      this.lastMessage?.sender?.firstName == undefined ||
      this.lastMessage?.sender?.lastName == undefined ||
      this.lastMessage?.data == undefined
    ) {
      return ""
    }
    return `${this.lastMessage?.sender?.firstName} ${this.lastMessage?.sender?.lastName}: ${this.lastMessage?.data}`;
  }

  ngOnDestroy() {
    this.currentSubscription?.unsubscribe();
  }
}
