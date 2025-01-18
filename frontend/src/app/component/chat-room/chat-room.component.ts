import { Component, effect } from '@angular/core';
import { ChatMessage } from "../../model/chat-message";
import { MessageComponent } from "../message/message.component";
import { NgForOf } from "@angular/common";
import { WebSocketService } from "../../service/web-socket.service";
import { DataStoreService } from "../../service/data-store.service";
import { StompSubscription } from "@stomp/stompjs";
import { FormsModule } from "@angular/forms";
import { ChatRoom } from "../../model/chat-room";

@Component({
  selector: 'app-chat-room',
  standalone: true,
  imports: [
    MessageComponent,
    NgForOf,
    FormsModule
  ],
  templateUrl: './chat-room.component.html',
  styleUrl: './chat-room.component.scss'
})
export class ChatRoomComponent {

  protected currentChatRoom: ChatRoom | undefined;
  private currentSubscription: StompSubscription | undefined;
  protected chatMessages: ChatMessage[] = []
  protected message: string = '';
  protected chatRoomName: string = '';

  constructor(
    private websocketService: WebSocketService,
    private dataStoreService: DataStoreService
  ) {
    effect(() => {
      if (!this.websocketService.isConnected()) return

      if (this.currentSubscription != undefined) {
        this.currentSubscription.unsubscribe();
        this.chatMessages = [];
      }

      this.currentChatRoom = this.dataStoreService.getCurrentlySelectedChatRoom();
      if (this.currentChatRoom != undefined) {
        this.chatRoomName = this.currentChatRoom.name;
        this.currentSubscription = this.websocketService.subscribeToRoom(
          this.currentChatRoom.id!!,
          (message: ChatMessage[] | ChatMessage) => {
            if (Array.isArray(message)) {
              this.chatMessages.push(...message);
            } else {
              this.chatMessages.push(message);
            }
          }
        )
      }
    })
  }

  sendMessage() {
    this.websocketService.sendMessage(this.currentChatRoom!.id!!, this.message);
    this.message = '';
  }
}
