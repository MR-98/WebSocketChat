import { Component, inject, OnInit } from '@angular/core';
import { ChatRoomListComponent } from "../../component/chat-room-list/chat-room-list.component";
import { ChatRoomComponent } from "../../component/chat-room/chat-room.component";
import { WebSocketService } from "../../service/web-socket.service";
import { NgClass, NgIf } from "@angular/common";
import { SidebarComponent } from "../../component/sidebar/sidebar.component";
import { MatSnackBar } from "@angular/material/snack-bar";

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
export class MainComponent implements OnInit {

  private _snackBar = inject(MatSnackBar);

  protected chatRoomActive = false;

  constructor(
    protected webSocketService: WebSocketService
  ) {
    this.webSocketService.connect(() => {});
  }

  ngOnInit() {
    this._snackBar.open(
      'Znajdujesz się w trybie demo aplikacji. Dokonane zmiany nie będą trwałe. Reset aplikacji następuje o każdej pełnej godzinie.',
      'Rozumiem',
      {
        horizontalPosition: 'center',
        verticalPosition: 'top',
      }
    );
  }

  onChatRoomActivated() {
    this.chatRoomActive = true;
  }

  onChatRoomDeactivated() {
    this.chatRoomActive = false;
  }
}
