import { Component } from '@angular/core';
import { ChatRoomListComponent } from "../../component/chat-room-list/chat-room-list.component";
import { ChatRoomComponent } from "../../component/chat-room/chat-room.component";
import { WebSocketService } from "../../service/web-socket.service";
import { NgClass, NgIf } from "@angular/common";
import { SidebarComponent } from "../../component/sidebar/sidebar.component";

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    ChatRoomListComponent,
    ChatRoomComponent,
    NgIf,
    SidebarComponent,
    NgClass
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {

  protected chatRoomActive = false;

  constructor(
    protected webSocketService: WebSocketService
  ) {
    this.webSocketService.connect(() => {});
  }

  onChatRoomActivated() {
    this.chatRoomActive = true;
  }

  onChatRoomDeactivated() {
    this.chatRoomActive = false;
  }
}
