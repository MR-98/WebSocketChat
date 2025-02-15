import { Injectable } from '@angular/core';
import { ChatRoom } from "../model/chat-room";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class InvitationsService {

  private url: string = `${environment.backendUrl}/invitations`;

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
