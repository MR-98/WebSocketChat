import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { ChatMessage } from "../model/chat-message";

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private client: Client;
  private token: string = '<token>'
  constructor() {
    this.client = new Client({
      brokerURL: 'ws://host.docker.internal:8080/ws-chat', // Adres WebSocket backendu
      connectHeaders: {
        Authorization: `Bearer ${this.token}`
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

  subscribeToRoom(roomId: number, callback: (message: ChatMessage) => void): void {
    this.client.subscribe(
      `/topic/${roomId}`,
      (message) => callback(JSON.parse(message.body)),
      {
        Authorization: `Bearer ${this.token}`
      }
    );
  }

  sendMessage(roomId: number, content: string, username: string): void {
    const message = {
      data: content,
      room: {
        id: roomId
      },
      sender: username,
    };

    this.client.publish({
      destination: `/app/chat.sendMessage`,
      body: JSON.stringify(message),
      headers: {
        Authorization: `Bearer ${this.token}`
      }
    });
  }
}
