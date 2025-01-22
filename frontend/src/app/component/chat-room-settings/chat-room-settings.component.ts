import { Component, EventEmitter, inject, Input, output, Output } from '@angular/core';
import { ChatRoom } from "../../model/chat-room";
import { ChatRoomService } from "../../service/chat-room.service";
import { MatDialog } from "@angular/material/dialog";
import { ChangeRoomNameDialogComponent } from "../../dialog/change-room-name-dialog/change-room-name-dialog.component";
import { MatIcon } from "@angular/material/icon";
import { YesNoDialogComponent } from "../../dialog/yes-no-dialog/yes-no-dialog.component";
import { DataStoreService } from "../../service/data-store.service";

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
  @Output() chatRoomNameUpdated = new EventEmitter<string>();
  readonly dialog = inject(MatDialog);

  constructor(
    private chatRoomService: ChatRoomService,
    private dataStoreService: DataStoreService
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
      this.chatRoomNameUpdated.emit(newRoomName);
    });
  }

  deleteRoom() {
    this.dialog.open(
      YesNoDialogComponent,
      {
        data: {
          titleText: "Usuń pokój",
          contentText: "Czy jesteś pewien że chcesz usunąć pokój? Wszystkie wiadomości zostaną usunięte i nie będzie można ich przywrócić.",
          yesButtonText: "Tak",
          noButtonText: "Nie"
        },
      }
    ).afterClosed().subscribe((result: boolean) => {
      if(result) {
        this.chatRoomService.deleteRoom(this.chatRoom).subscribe(_ => {
          let chatRoomList = this.dataStoreService.getChatRoomList();
          let newChatRoomList = chatRoomList.filter(chatRoomListElement => chatRoomListElement != this.chatRoom);
          this.dataStoreService.setChatRoomList(newChatRoomList);
          if(newChatRoomList.length > 0) {
            this.dataStoreService.setCurrentlySelectedChatRoom(newChatRoomList[0])
          }
        })
      }
    });
  }
}
