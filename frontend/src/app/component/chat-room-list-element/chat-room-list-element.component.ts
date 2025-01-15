import { Component, Input } from '@angular/core';
import { ChatRoom } from "../../model/chat-room";
import { DataStoreService } from "../../service/data-store.service";
import { NgClass } from "@angular/common";

@Component({
  selector: 'app-chat-room-list-element',
  standalone: true,
  imports: [
    NgClass
  ],
  templateUrl: './chat-room-list-element.component.html',
  styleUrl: './chat-room-list-element.component.scss'
})
export class ChatRoomListElementComponent {
  @Input() chatRoom: ChatRoom = {
    name: "",
    users: [],
    id: 0
  }

  constructor(private dataStoreService: DataStoreService) {
  }

  isCurrentlySelectedChatRoom() {
    return this.dataStoreService.getCurrentlySelectedChatRoom()?.id == this.chatRoom.id
  }
}
