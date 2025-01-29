import { Injectable } from '@angular/core';
import { Client, StompSubscription } from '@stomp/stompjs';
import { ChatMessage } from "../model/chat-message";
import { KeycloakService } from "keycloak-angular";
import { DataStoreService } from "./data-store.service";
import { Invitation } from "../model/invitation";
import { ChatRoom } from "../model/chat-room";
import { User } from "../model/user";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private client: Client;
  constructor(
    private keycloakService: KeycloakService,
    private dataStoreService: DataStoreService
  ) {
    this.client = new Client({
      brokerURL: environment.websocketUrl,
      connectHeaders: {
        Authorization: `Bearer ${this.keycloakService.getKeycloakInstance().token}`
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
        Authorization: `Bearer ${this.keycloakService.getKeycloakInstance().token}`
      }
    );
  }

  sendMessage(room: ChatRoom, content: string): void {
    let currentUserProfile = this.dataStoreService.getUserProfile()!!
    const message: ChatMessage = {
      data: content,
      room: room,
      sender: {
        username: currentUserProfile.username,
        firstName: currentUserProfile.firstName,
        lastName: currentUserProfile.lastName
      },
      timestamp: Date.now(),
      id: null
    };

    this.client.publish({
      destination: `/app/chat.sendMessage`,
      body: JSON.stringify(message),
      headers: {
        Authorization: `Bearer ${this.keycloakService.getKeycloakInstance().token}`
      }
    });
  }

  subscribeInvitations(callback: (invitations: Invitation[] | Invitation) => void): StompSubscription {
    return this.client.subscribe(
      `/topic/invitation.listen.${this.keycloakService.getUsername()}`,
      (invitations) => callback(JSON.parse(invitations.body)),
      {
        Authorization: `Bearer ${this.keycloakService.getKeycloakInstance().token}`
      }
    );
  }

  sendInvitation(userToInvite: User, room: ChatRoom) {
    let currentUserProfile = this.dataStoreService.getUserProfile()!!
    let invitation: Invitation = {
      invitedUser: userToInvite,
      invitingUser: {
        username: currentUserProfile.username,
        firstName: currentUserProfile.firstName,
        lastName: currentUserProfile.lastName
      },
      room: room,
    }
    this.client.publish({
      destination: `/app/invitation.sendInvitation`,
      body: JSON.stringify(invitation),
      headers: {
        Authorization: `Bearer ${this.keycloakService.getKeycloakInstance().token}`
      }
    });
  }
}
