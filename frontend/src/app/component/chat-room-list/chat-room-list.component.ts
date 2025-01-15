import { Component } from '@angular/core';
import { ChatRoom } from "../../model/chat-room";
import { ChatRoomService } from "../../service/chat-room.service";
import { NgForOf } from "@angular/common";
import { DataStoreService } from "../../service/data-store.service";

@Component({
  selector: 'app-chat-room-list',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './chat-room-list.component.html',
  styleUrl: './chat-room-list.component.scss'
})
export class ChatRoomListComponent {
  protected chatRoomList: ChatRoom[] = []

  constructor(
    private chatRoomService: ChatRoomService,
    private dataStoreService: DataStoreService
  ) {
    this.chatRoomService.getUserChatRooms().subscribe(userChatRooms => {
      this.chatRoomList = userChatRooms;
      if(this.chatRoomList.length > 0) {
        this.changeActiveChatRoom(this.chatRoomList[0])
      }
    });
  }

  changeActiveChatRoom(chatRoom: ChatRoom) {
    this.dataStoreService.setCurrentlySelectedChatRoomId(chatRoom.id)
  }
}
