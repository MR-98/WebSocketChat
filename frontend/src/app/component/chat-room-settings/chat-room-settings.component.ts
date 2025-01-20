import { Component, inject, Input } from '@angular/core';
import { ChatRoom } from "../../model/chat-room";
import { ChatRoomService } from "../../service/chat-room.service";
import { MatDialog } from "@angular/material/dialog";
import { ChangeRoomNameDialogComponent } from "../../dialog/change-room-name-dialog/change-room-name-dialog.component";
import { MatIcon } from "@angular/material/icon";

@Component({
  selector: 'app-chat-room-settings',
  standalone: true,
  imports: [
    MatIcon
  ],
  templateUrl: './chat-room-settings.component.html',
  styleUrl: './chat-room-settings.component.scss'
})
export class ChatRoomSettingsComponent {

  @Input() chatRoom!: ChatRoom;
  readonly dialog = inject(MatDialog);

  constructor(
    private chatRoomService: ChatRoomService
  ) {}


  changeRoomName() {
    this.dialog.open(
      ChangeRoomNameDialogComponent,
      {
        data: {
          currentRoomName: this.chatRoom.name,
        },
      }
    ).afterClosed().subscribe((newRoomName: string) => {
      if(newRoomName == undefined) {
        return
      }
      this.chatRoomService.changeRoomName(newRoomName, this.chatRoom).subscribe();
    });
  }
}
