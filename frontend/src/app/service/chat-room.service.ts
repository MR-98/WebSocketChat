import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ChatRoom } from "../model/chat-room";
import { ConfigService } from "./config.service";

@Injectable({
  providedIn: 'root'
})
export class ChatRoomService {

  private readonly url: string;

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
  ) {
    this.url = `${this.configService.restUrl}/rooms`;
  }

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

  changeRoomName(newName: string, roomId: number) {
    return this.http.put<ChatRoom>(
      this.url,
      {
        roomId: roomId,
        newRoomName: newName
      }
    )
  }

  deleteRoom(roomId: number) {
    return this.http.delete<ChatRoom>(
      this.url + "/" + roomId
    )
  }

  leaveRoom(room: ChatRoom) {
    return this.http.post<null>(
      this.url + "/leave/" + room.id,
      null
    )
  }
}
