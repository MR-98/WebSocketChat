import { Injectable } from '@angular/core';
import { Client, StompSubscription } from '@stomp/stompjs';
import { ChatMessage } from "../model/chat-message";
import { DataStoreService } from "./data-store.service";
import { Invitation } from "../model/invitation";
import { ChatRoom } from "../model/chat-room";
import { User } from "../model/user";
import { environment } from "../../environments/environment";
import { AuthService } from "./auth.service";
import { ChatMessageToSave } from "../model/chat-message-to-save";
import {InvitationToSave} from "../model/invitations-to-save";

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private client: Client;
  constructor(
    private dataStoreService: DataStoreService,
    private authService: AuthService
  ) {
    this.client = new Client({
      brokerURL: environment.websocketUrl,
      connectHeaders: {
        Authorization: `Bearer ${this.authService.getToken()}`
      },
      reconnectDelay: 5000,
      heartbeatOutgoing: 30000,
    });
  }

  connect(onConnectCallback: () => void): void {
    this.client.onConnect = () => {
      onConnectCallback();
    };

    this.client.onStompError = (frame) => {
      console.error('STOMP Error:', frame);
      this.disconnect();
    };

    this.client.activate();
  }

  disconnect(): void {
    this.client.deactivate();
  }

  isConnected(): boolean {
    return this.client.connected;
  }

  subscribeToRoom(roomId: number, callback: (message: ChatMessage[] | ChatMessage) => void): StompSubscription {
    return this.client.subscribe(
      `/topic/chat.listen.${roomId}`,
      (message) => callback(JSON.parse(message.body)),
      {
        Authorization: `Bearer ${this.authService.getToken()}`
      }
    );
  }

  sendMessage(room: ChatRoom, content: string): void {
    let currentUserProfile = this.dataStoreService.getUserProfile()!!
    const message: ChatMessageToSave = {
      data: content,
      roomId: room.id,
      senderUsername: currentUserProfile.username,
      timestamp: Date.now()
    };

    this.client.publish({
      destination: `/app/chat.sendMessage`,
      body: JSON.stringify(message),
      headers: {
        Authorization: `Bearer ${this.authService.getToken()}`
      }
    });
  }

  subscribeInvitations(callback: (invitations: Invitation[] | Invitation) => void): StompSubscription {
    let currentUserProfile = this.dataStoreService.getUserProfile()!!
    return this.client.subscribe(
      `/topic/invitation.listen.${currentUserProfile.username}`,
      (invitations) => callback(JSON.parse(invitations.body)),
      {
        Authorization: `Bearer ${this.authService.getToken()}`
      }
    );
  }

  sendInvitation(userToInvite: User, room: ChatRoom) {
    let invitation: InvitationToSave = {
      invitedUser: userToInvite.username,
      roomId: room.id,
    }
    this.client.publish({
      destination: `/app/invitation.sendInvitation`,
      body: JSON.stringify(invitation),
      headers: {
        Authorization: `Bearer ${this.authService.getToken()}`
      }
    });
  }
}
