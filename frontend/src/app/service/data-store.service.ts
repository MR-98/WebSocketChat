import { Injectable, signal } from '@angular/core';
import { ChatRoom } from "../model/chat-room";

@Injectable({
  providedIn: 'root'
})
export class DataStoreService {
  private _currentlySelectedChatRoomId = signal<ChatRoom | undefined>(undefined)

  constructor() {
  }

  getCurrentlySelectedChatRoom(): ChatRoom | undefined {
    return this._currentlySelectedChatRoomId();
  }

  setCurrentlySelectedChatRoomId(value: ChatRoom) {
    this._currentlySelectedChatRoomId.set(value)
  }
}
