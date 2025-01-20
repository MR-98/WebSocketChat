import { Component } from '@angular/core';
import { ChatRoomListElementComponent } from "../chat-room-list-element/chat-room-list-element.component";
import { NgForOf } from "@angular/common";
import { MatIconButton } from "@angular/material/button";
import { MatIcon } from "@angular/material/icon";
import { MatMenu, MatMenuItem, MatMenuTrigger } from "@angular/material/menu";
import { KeycloakService } from "keycloak-angular";
import { MatDivider } from "@angular/material/divider";
import { MatListItem } from "@angular/material/list";

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    ChatRoomListElementComponent,
    NgForOf,
    MatIcon,
    MatIconButton,
    MatMenu,
    MatMenuTrigger,
    MatDivider,
    MatListItem,
    MatMenuItem
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {

  constructor(
    private keycloakService: KeycloakService
  ) {}

  signOut() {
    this.keycloakService.logout().then();
  }

  redirectToKeycloakAccountSettings() {
    this.keycloakService.getKeycloakInstance().accountManagement().then();
  }
}
