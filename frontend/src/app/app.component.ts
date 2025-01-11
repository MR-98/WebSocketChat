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

  roomId: number = 1;
  message: string = '';
  messages: ChatMessage[] = [];
  connectedRoom: number | null = null;

  constructor(
    private webSocketService: WebSocketService
  ) {}

  connectToRoom(): void {
    if (!this.roomId) {
      alert('Room name is required');
      return;
    }

    this.webSocketService.connect(
      () => {
        this.webSocketService.subscribeToRoom(
          this.roomId,
          (message: ChatMessage[] | ChatMessage) => {
            if(Array.isArray(message)) {
              this.messages.push(...message);
            } else {
              this.messages.push(message);
            }
          }
        );
      }
    );

    this.connectedRoom = this.roomId;
  }

  sendMessage(): void {
    if (this.connectedRoom && this.message) {
      this.webSocketService.sendMessage(this.connectedRoom, this.message);
      this.message = '';
    }
  }

  logout(): void {
    this.webSocketService.disconnect();
  }
}
