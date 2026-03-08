import { Injectable } from '@angular/core';
import { ChatRoom } from "../model/chat-room";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { ConfigService } from "./config.service";

@Injectable({
  providedIn: 'root'
})
export class InvitationsService {

  private readonly url: string;

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
  ) {
    this.url = `${this.configService.restUrl}/invitations`;
  }

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
