import { Component, inject } from '@angular/core';
import { MatButton, MatIconButton } from "@angular/material/button";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import { MatFormField, MatLabel } from "@angular/material/form-field";
import { MatInput } from "@angular/material/input";
import { ReactiveFormsModule } from "@angular/forms";
import { Invitation } from "../../model/invitation";
import { NgForOf } from "@angular/common";
import { InvitationsService } from "../../service/invitations.service";
import { ChatRoom } from "../../model/chat-room";
import { DataStoreService } from "../../service/data-store.service";
import { MatIcon } from "@angular/material/icon";

@Component({
  selector: 'app-invitations-dialog',
  standalone: true,
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle,
    MatFormField,
    MatInput,
    MatIcon,
    MatLabel,
    ReactiveFormsModule,
    NgForOf,
    MatIconButton
  ],
  templateUrl: './invitations-list-dialog.component.html',
  styleUrl: './invitations-list-dialog.component.scss'
})
export class InvitationsListDialogComponent {

  protected data = inject(MAT_DIALOG_DATA);
  protected invitations: Invitation[] = [];

  constructor(
    private invitationsService: InvitationsService,
    private dataStoreService: DataStoreService
  ) {
    this.invitations = this.data["invitations"] ? this.data["invitations"] : [];
  }

  acceptInvitation(invitation: Invitation) {
    this.invitationsService.acceptInvitation(invitation.room).subscribe( (newRoom: ChatRoom) => {
      this.invitations = this.invitations.filter(element => element.id != invitation.id);
      this.dataStoreService.setChatRoomList([...this.dataStoreService.getChatRoomList(), newRoom]);
    });
  }


  rejectInvitation(invitation: Invitation) {
    this.invitationsService.deleteInvitation(invitation.room).subscribe( _ => {
      this.invitations = this.invitations.filter(element => element.id != invitation.id);
    });
  }
}
