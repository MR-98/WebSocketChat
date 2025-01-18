import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ChatRoom } from "../model/chat-room";

@Injectable({
  providedIn: 'root'
})
export class ChatRoomService {

  private url: string = 'http://host.docker.internal:8080/';

  constructor(
    private http: HttpClient
  ) { }

  getUserChatRooms(): Observable<ChatRoom[]> {
    return this.http.get<ChatRoom[]>(
      this.url + "rooms"
    )
  }

  createNewRoom(defaultRoomName: string) {
    return this.http.post<ChatRoom>(
      this.url + "rooms",
      defaultRoomName
    )
  }
}
