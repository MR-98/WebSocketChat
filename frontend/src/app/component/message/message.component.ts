import { Component, Input } from '@angular/core';
import { ChatMessage } from "../../model/chat-message";
import { NgClass, NgIf } from "@angular/common";
import { DataStoreService } from "../../service/data-store.service";

@Component({
  selector: 'app-message',
  standalone: true,
  imports: [
    NgClass,
    NgIf
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

}
