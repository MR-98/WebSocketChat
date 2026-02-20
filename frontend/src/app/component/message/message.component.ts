import { Component, Input } from '@angular/core';
import { ChatMessage } from "../../model/chat-message";
import { NgClass, NgForOf, NgIf } from "@angular/common";
import { DataStoreService } from "../../service/data-store.service";
import { MatTooltip } from "@angular/material/tooltip";
import { AttachmentsService } from "../../service/attachments.service";

@Component({
  selector: 'app-message',
  standalone: true,
  imports: [
    NgClass,
    NgIf,
    MatTooltip,
    NgForOf
  ],
  templateUrl: './message.component.html',
  styleUrl: './message.component.scss'
})
export class MessageComponent{

  @Input() chatMessage!: ChatMessage;
  protected currentUser: string = '';

  constructor(
    private dataStoreService: DataStoreService,
    private attachmentsService: AttachmentsService,
  ) {
    this.currentUser = dataStoreService.getUserProfile()!!.username;
  }

  formatDate(timestamp: number): string {
    let date = new Date(timestamp);
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
  }

  downloadFile(id: number) {
    this.attachmentsService.downloadFile(id);
  }
}
