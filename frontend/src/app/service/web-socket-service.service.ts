import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private client: Client;

  constructor() {
    this.client = new Client({
      brokerURL: 'ws://host.docker.internal:8080/ws-chat', // Adres WebSocket backendu
      connectHeaders: {},
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });
  }

  connect(): void {
    // this.client.connectHeaders = {
    //   Authorization: `Bearer ${token}`,
    // };

    this.client.onConnect = () => {
      console.log('Connected to WebSocket');

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

  subscribeToRoom(roomName: string, callback: (message: any) => void): void {
    console.log("SUBSCRIBING TO ROOM: ", roomName);
    this.client.subscribe(`/topic/${roomName}`, (message) => {
      callback(JSON.parse(message.body));
    });
  }

  sendMessage(roomName: string, content: string, username: string): void {
    const message = {
      data: content,
      room: roomName,
      sender: username,
    };

    this.client.publish({
      destination: `/app/chat.sendMessage`,
      body: JSON.stringify(message),
    });
  }
}
