import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ChatMessage } from "../model/chat-message";
import { Observable } from "rxjs";
import { ConfigService } from "./config.service";

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private readonly url: string;

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
  ) {
    this.url = `${this.configService.restUrl}/chat`;
  }

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
