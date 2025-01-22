import { Injectable, signal } from '@angular/core';
import { ChatRoom } from "../model/chat-room";

@Injectable({
  providedIn: 'root'
})
export class DataStoreService {
  private _currentlySelectedChatRoomId = signal<ChatRoom | undefined>(undefined)
  private _chatRoomList = signal<ChatRoom[]>([])

  constructor() {
  }

  getCurrentlySelectedChatRoom(): ChatRoom | undefined {
    return this._currentlySelectedChatRoomId();
  }

  setCurrentlySelectedChatRoom(value: ChatRoom) {
    this._currentlySelectedChatRoomId.set(value)
  }

  getChatRoomList(): ChatRoom[] {
    return this._chatRoomList();
  }

  setChatRoomList(value: ChatRoom[]) {
    this._chatRoomList.set(value)
  }
}
