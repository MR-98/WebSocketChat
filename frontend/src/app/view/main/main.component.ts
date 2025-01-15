import { Component } from '@angular/core';
import { ChatRoomListComponent } from "../../component/chat-room-list/chat-room-list.component";
import { ChatRoomComponent } from "../../component/chat-room/chat-room.component";
import { WebSocketService } from "../../service/web-socket.service";
import { NgIf } from "@angular/common";

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    ChatRoomListComponent,
    ChatRoomComponent,
    NgIf
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {

  constructor(
    protected webSocketService: WebSocketService
  ) {
    this.webSocketService.connect(
      () => {
        console.log("CONNECTED");
      }
    );
  }

}
