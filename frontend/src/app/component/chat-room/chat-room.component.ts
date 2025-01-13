import { Component } from '@angular/core';
import { ChatMessage } from "../../model/chat-message";
import { MessageComponent } from "../message/message.component";
import { NgForOf } from "@angular/common";

@Component({
  selector: 'app-chat-room',
  standalone: true,
  imports: [
    MessageComponent,
    NgForOf
  ],
  templateUrl: './chat-room.component.html',
  styleUrl: './chat-room.component.scss'
})
export class ChatRoomComponent {

  protected chatMessages: ChatMessage[] = [
    {
      data: "TESTOWA WIADOMOŚĆfsa fasfasf as fgasg asg as g ",
      room: {id: 1},
      sender: "user1",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user2",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user1",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user2",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user2",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user2",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user2",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user2",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user1",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆfsa fasfasf as fgasg asg as g ",
      room: {id: 1},
      sender: "user1",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user2",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user1",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user2",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user2",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user2",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user2",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user2",
      timestamp: Date.now()
    },
    {
      data: "TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ TESTOWA WIADOMOŚĆ",
      room: {id: 1},
      sender: "user1",
      timestamp: Date.now()
    },
  ]

}
