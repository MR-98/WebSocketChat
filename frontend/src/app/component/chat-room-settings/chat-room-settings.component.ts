import { Component, Input } from '@angular/core';
import { ChatRoom } from "../../model/chat-room";
import { ChatRoomService } from "../../service/chat-room.service";

@Component({
  selector: 'app-chat-room-settings',
  standalone: true,
	imports: [],
  templateUrl: './chat-room-settings.component.html',
  styleUrl: './chat-room-settings.component.scss'
})
export class ChatRoomSettingsComponent {

  @Input() chatRoom!: ChatRoom;

  constructor(private chatRoomService: ChatRoomService) {
  }




}
