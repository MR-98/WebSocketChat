import { Injectable } from '@angular/core';
import { Title } from "@angular/platform-browser";
import { ChatMessage } from "../model/chat-message";

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {

  private notificationSound = new Audio('assets/sounds/notification.mp3');
  private originalPageTitle = '';
  private currentTimeoutId: any;

  constructor(private pageTitle: Title) {
    this.originalPageTitle = this.pageTitle.getTitle();
  }

  sendNewMessageNotification(message: ChatMessage) {
    this.setPageTitle(message);
    this.sendSoundNotification();
  }

  private setPageTitle(message: ChatMessage) {
    let sender = message.sender.firstName + " " + message.sender.lastName;
    let data;
    if (message.data != "") {
      data = message.data;
    } else {
      data = "*Załącznik*"
    }
    this.pageTitle.setTitle(sender + ": " + data);

    if(this.currentTimeoutId) {
      clearTimeout(this.currentTimeoutId);
    }
    this.currentTimeoutId = setTimeout(() => {this.pageTitle.setTitle(this.originalPageTitle)}, 5000);
  }

  private sendSoundNotification() {
    this.notificationSound.play().catch(err => console.error(err));
  }
}
