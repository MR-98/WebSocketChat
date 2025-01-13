import { Component, Input } from '@angular/core';
import { ChatMessage } from "../../model/chat-message";
import { KeycloakService } from "keycloak-angular";
import { NgClass, NgIf } from "@angular/common";

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
export class MessageComponent {

  @Input() chatMessage!: ChatMessage;
  protected currentUser: string = '';

  constructor(keycloakService: KeycloakService) {
    this.currentUser = keycloakService.getUsername();
  }

}
