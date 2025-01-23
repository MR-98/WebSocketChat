import { Injectable } from '@angular/core';
import { Client, StompSubscription } from '@stomp/stompjs';
import { ChatMessage } from "../model/chat-message";
import { KeycloakService } from "keycloak-angular";
import { DataStoreService } from "./data-store.service";

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
      brokerURL: 'ws://host.docker.internal:8080/ws-chat', // Adres WebSocket backendu
      connectHeaders: {
        Authorization: `Bearer ${this.keycloakService.getKeycloakInstance().token}`
      },
      debug: (str) => console.log(str),
      reconnectDelay: 0,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });
  }

  connect(onConnectCallback: () => void): void {
    this.client.onConnect = () => {
      onConnectCallback();
    };

    this.client.onStompError = (frame) => {
      console.error('STOMP Error:', frame);
    };

    this.client.debug = (msg) => console.log('STOMP Debug:', msg);

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

  sendMessage(roomId: number, content: string): void {
    let currentUserProfile = this.dataStoreService.getUserProfile()!!
    const message: ChatMessage = {
      data: content,
      room: {
        id: roomId
      },
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
}
