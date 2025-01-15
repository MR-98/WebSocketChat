import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DataStoreService {
  private _currentlySelectedChatRoomId = signal<number>(0)

  constructor() { }

  getCurrentlySelectedChatRoomId(): number {
    return this._currentlySelectedChatRoomId();
  }

  setCurrentlySelectedChatRoomId(value: number) {
    this._currentlySelectedChatRoomId.set(value)
  }
}
