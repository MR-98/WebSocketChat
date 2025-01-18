import { Component, effect } from '@angular/core';
import { ChatRoom } from "../../model/chat-room";
import { ChatRoomService } from "../../service/chat-room.service";
import { NgForOf } from "@angular/common";
import { DataStoreService } from "../../service/data-store.service";
import { ChatRoomListElementComponent } from "../chat-room-list-element/chat-room-list-element.component";

@Component({
  selector: 'app-chat-room-list',
  standalone: true,
  imports: [
    NgForOf,
    ChatRoomListElementComponent
  ],
  templateUrl: './chat-room-list.component.html',
  styleUrl: './chat-room-list.component.scss'
})
export class ChatRoomListComponent {
  protected chatRoomList: ChatRoom[] = []

  constructor(
    private chatRoomService: ChatRoomService,
    private dataStoreService: DataStoreService,
  ) {
    this.chatRoomService.getUserChatRooms().subscribe(userChatRooms => {
      this.dataStoreService.setChatRoomList(userChatRooms);
      if(userChatRooms.length > 0) {
        this.changeActiveChatRoom(userChatRooms[0])
      }
    });

    effect(() => {
      this.chatRoomList = this.dataStoreService.getChatRoomList();
    })
  }

  changeActiveChatRoom(chatRoom: ChatRoom) {
    this.dataStoreService.setCurrentlySelectedChatRoomId(chatRoom)
  }

  createNewRoom() {
    this.chatRoomService.createNewRoom("Default room name").subscribe((response) => {
      this.dataStoreService.setChatRoomList([response, ...this.chatRoomList]);
      this.dataStoreService.setCurrentlySelectedChatRoomId(response)
    })
  }
}
