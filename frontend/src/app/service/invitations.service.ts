import { Injectable } from '@angular/core';
import { ChatRoom } from "../model/chat-room";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class InvitationsService {

  private url: string = 'https://mr98.site/api/invitations';

  constructor(
    private http: HttpClient
  ) { }

  acceptInvitation(chatRoom: ChatRoom): Observable<ChatRoom> {
    return this.http.post<ChatRoom>(
      this.url + "/accept/" + chatRoom.id,
      null
    )
  }

  deleteInvitation(chatRoom: ChatRoom): Observable<null> {
    return this.http.delete<null>(
      this.url + "/reject/" + chatRoom.id
    )
  }
}
