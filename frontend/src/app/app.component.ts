import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { WebSocketService } from "./service/web-socket-service.service";
import { FormsModule } from "@angular/forms";
import { NgForOf, NgIf } from "@angular/common";
import { ChatMessage } from "./model/chat-message";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, NgIf, NgForOf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  roomName: string = '';
  message: string = '';
  messages: string[] = [];
  connectedRoom: string | null = null;
  username: string = 'user1';

  constructor(
    private webSocketService: WebSocketService
  ) {}

  connectToRoom(): void {
    if (!this.roomName) {
      alert('Room name is required');
      return;
    }

    this.webSocketService.connect(
      () => {
        this.webSocketService.subscribeToRoom(
          this.roomName,
          (message: ChatMessage) => {
            this.messages.push(message.data);
          }
        );
      }
    );

    this.connectedRoom = this.roomName;
  }

  sendMessage(): void {
    if (this.connectedRoom && this.message) {
      this.webSocketService.sendMessage(this.connectedRoom, this.message, this.username);
      this.message = '';
    }
  }

  logout(): void {
    this.webSocketService.disconnect();
  }
}
