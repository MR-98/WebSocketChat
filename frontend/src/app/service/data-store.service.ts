import { Injectable, signal } from '@angular/core';
import { ChatRoom } from "../model/chat-room";
import { UserProfile } from "../model/user-profile";

@Injectable({
  providedIn: 'root'
})
export class DataStoreService {
  private _currentlySelectedChatRoomId = signal<ChatRoom | undefined>(undefined)
  private _chatRoomList = signal<ChatRoom[]>([])
  private _userProfile = signal<UserProfile | undefined>(undefined)

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

  getUserProfile(): UserProfile | undefined {
    return this._userProfile();
  }

  setUserProfile(value: UserProfile) {
    this._userProfile.set(value);
  }
}
