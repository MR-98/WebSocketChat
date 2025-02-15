import { Component, inject } from '@angular/core';
import { ChatRoomListElementComponent } from "../chat-room-list-element/chat-room-list-element.component";
import { NgForOf } from "@angular/common";
import { MatIconButton } from "@angular/material/button";
import { MatIcon } from "@angular/material/icon";
import { MatMenu, MatMenuItem, MatMenuTrigger } from "@angular/material/menu";
import { KeycloakService } from "keycloak-angular";
import { MatDivider } from "@angular/material/divider";
import { MatListItem } from "@angular/material/list";
import { DataStoreService } from "../../service/data-store.service";
import { MatDialog } from "@angular/material/dialog";
import { InvitationsListDialogComponent } from "../../dialog/invitations-list-dialog/invitations-list-dialog.component";
import { WebSocketService } from "../../service/web-socket.service";
import { Invitation } from "../../model/invitation";
import { MatBadge } from "@angular/material/badge";
import { SettingsDialogComponent } from "../../dialog/settings-dialog/settings-dialog.component";
import { LocalStorageService } from "../../service/local-storage.service";

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
    MatMenuItem,
    MatBadge
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {

  protected currentUserFullName: string = '';
  readonly dialog = inject(MatDialog);
  protected invitations: Invitation[] = [];

  constructor(
    private keycloakService: KeycloakService,
    private dataStoreService: DataStoreService,
    private webSocketService: WebSocketService,
    private localStorageService: LocalStorageService
  ) {
    this.currentUserFullName = this.dataStoreService.getUserProfile()!!.fullName;
    if(this.isReceivingInvitationsEnabled()) {
      this.loadInvitations();
    }
  }

  signOut() {
    this.keycloakService.logout().then();
  }

  redirectToKeycloakAccountSettings() {
    this.keycloakService.getKeycloakInstance().accountManagement().then();
  }

  showInvitationsDialog() {
    this.dialog.open(
      InvitationsListDialogComponent,
      {
        data: {
          invitations: this.invitations
        }
      }
    ).afterClosed().subscribe( (invitations: Invitation[]) => {
      this.invitations = invitations;
    });
  }

  loadInvitations() {
    this.webSocketService.subscribeInvitations(
      (invitations: Invitation[] | Invitation) => {
        if (Array.isArray(invitations)) {
          this.invitations.unshift(...invitations);
        } else {
          this.invitations.unshift(invitations);
        }
      }
    )
  }

  showSettingsDialog() {
    this.dialog.open(
      SettingsDialogComponent
    );
  }

  isReceivingInvitationsEnabled() {
    return this.localStorageService.getData("invitationsEnabled") != null ?
      JSON.parse(this.localStorageService.getData("invitationsEnabled")!!) : true;
  }
}
