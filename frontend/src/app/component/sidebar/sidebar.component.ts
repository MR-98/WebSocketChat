import { Component } from '@angular/core';
import { ChatRoomListElementComponent } from "../chat-room-list-element/chat-room-list-element.component";
import { NgForOf } from "@angular/common";
import { MatIconButton } from "@angular/material/button";
import { MatIcon } from "@angular/material/icon";

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    ChatRoomListElementComponent,
    NgForOf,
    MatIcon,
    MatIconButton
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {

}
