import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ChatRoom } from "../model/chat-room";

@Injectable({
  providedIn: 'root'
})
export class ChatRoomService {

  private url: string = 'http://host.docker.internal:8080/rooms';

  constructor(
    private http: HttpClient
  ) { }

  getUserChatRooms(): Observable<ChatRoom[]> {
    return this.http.get<ChatRoom[]>(
      this.url
    )
  }

  createNewRoom(defaultRoomName: string) {
    return this.http.post<ChatRoom>(
      this.url,
      defaultRoomName
    )
  }

  changeRoomName(newName: string, room: ChatRoom) {
    room.name = newName;
    return this.http.put<ChatRoom>(
      this.url,
      room
    )
  }

  deleteRoom(chatRoom: ChatRoom) {
    return this.http.delete<ChatRoom>(
      this.url,
      {
        body: chatRoom
      }
    )
  }
}
