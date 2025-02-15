import { Injectable } from '@angular/core';
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { ChatMessage } from "../model/chat-message";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private url: string = `${environment.backendUrl}/chat`;

  constructor(
    private http: HttpClient
  ) { }

  loadOldMessagesForRoom(roomId: number, oldestMessageId: number): Observable<ChatMessage[]> {
    return this.http.post<ChatMessage[]>(
      this.url + "/loadOldMessages",
      {
        roomId: roomId,
        olderThanMessageId: oldestMessageId
      }
    )
  }
}
