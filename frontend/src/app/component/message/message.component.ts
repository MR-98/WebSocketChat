import { Component, Input } from '@angular/core';
import { ChatMessage } from "../../model/chat-message";
import { NgClass, NgIf } from "@angular/common";
import { DataStoreService } from "../../service/data-store.service";
import { MatTooltip } from "@angular/material/tooltip";

@Component({
  selector: 'app-message',
  standalone: true,
  imports: [
    NgClass,
    NgIf,
    MatTooltip
  ],
  templateUrl: './message.component.html',
  styleUrl: './message.component.scss'
})
export class MessageComponent{

  @Input() chatMessage!: ChatMessage;
  protected currentUser: string = '';

  constructor(dataStoreService: DataStoreService) {
    this.currentUser = dataStoreService.getUserProfile()!!.username;
  }

  formatDate(timestamp: number): string {
    let date = new Date(timestamp);
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
  }
}
