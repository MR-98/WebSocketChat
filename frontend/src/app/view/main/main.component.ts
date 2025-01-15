import { Component } from '@angular/core';
import { ChatRoomListComponent } from "../../component/chat-room-list/chat-room-list.component";
import { ChatRoomComponent } from "../../component/chat-room/chat-room.component";
import { WebSocketService } from "../../service/web-socket.service";

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    ChatRoomListComponent,
    ChatRoomComponent
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {

  constructor(
    private webSocketService: WebSocketService
  ) {
    this.webSocketService.connect(
      () => {
        console.log("CONNECTED");
      }
    );
  }

}
