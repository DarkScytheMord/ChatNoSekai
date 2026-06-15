import { Injectable } from '@angular/core';
import { API_CONFIG } from '../config/api.config';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Subject } from 'rxjs';
import { MessageResponse } from '../models/chat.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class WebsocketService {
  private stompClient?: Client;
  private messagesSubject = new Subject<MessageResponse>();

  messages$ = this.messagesSubject.asObservable();

  constructor(private authService: AuthService) {}

  connect(conversationId: number): void {
    const token = this.authService.getToken();

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(`${this.authService.getApiBaseUrl()}/ws`),

      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },

      debug: (message) => {
        console.log('[STOMP]', message);
      },

      reconnectDelay: 5000,

      onConnect: () => {
        console.log('WebSocket conectado');

        this.stompClient?.subscribe(
          `/topic/conversations/${conversationId}`,
          (message: IMessage) => {
            const body = JSON.parse(message.body) as MessageResponse;
            this.messagesSubject.next(body);
          }
        );
      },

      onStompError: (frame) => {
        console.error('Error STOMP:', frame);
      },

      onWebSocketError: (error) => {
        console.error('Error WebSocket:', error);
      },
    });

    this.stompClient.activate();
  }

  sendMessage(conversationId: number, content: string): void {
    if (!this.stompClient || !this.stompClient.connected) {
      console.error('WebSocket no conectado');
      return;
    }

    this.stompClient.publish({
      destination: '/app/chat.send',
      body: JSON.stringify({
        conversationId,
        content,
      }),
    });
  }

  disconnect(): void {
    this.stompClient?.deactivate();
  }
}