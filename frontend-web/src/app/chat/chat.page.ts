import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonButton,
  IonContent,
  IonInput,
  IonItem,
  IonText,
} from '@ionic/angular/standalone';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { MessageResponse } from '../models/chat.model';
import { ChatService } from '../services/chat.service';
import { WebsocketService } from '../services/websocket.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.page.html',
  styleUrls: ['./chat.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    IonButton,
    IonContent,
    IonInput,
    IonItem,
    IonText,
  ],
})
export class ChatPage implements OnInit, OnDestroy {
  conversationId = 0;
  currentUserId = 0;

  messages: MessageResponse[] = [];
  newMessage = '';

  loading = false;
  errorMessage = '';

  private messageSubscription?: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private chatService: ChatService,
    private websocketService: WebsocketService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
      return;
    }

    this.conversationId = Number(
      this.route.snapshot.paramMap.get('conversationId')
    );

    this.currentUserId = this.authService.getUserId();

    this.loadHistory();
    this.connectWebSocket();
  }

  loadHistory(): void {
    this.loading = true;
    this.errorMessage = '';

    this.chatService.getMessages(this.conversationId).subscribe({
      next: (response) => {
        this.messages = response;
        this.loading = false;
        setTimeout(() => this.scrollToBottom(), 100);
      },
      error: (error) => {
        console.error('Error cargando historial:', error);
        this.loading = false;
        this.errorMessage = 'No se pudo cargar el historial del chat.';
      },
    });
  }

  connectWebSocket(): void {
    this.websocketService.connect(this.conversationId);

    this.messageSubscription = this.websocketService.messages$.subscribe(
      (message) => {
        if (message.conversationId === this.conversationId) {
          this.messages.push(message);
          setTimeout(() => this.scrollToBottom(), 100);
        }
      }
    );
  }

  sendMessage(): void {
    const content = this.newMessage.trim();

    if (!content) {
      return;
    }

    this.websocketService.sendMessage(this.conversationId, content);
    this.newMessage = '';
  }

  goBack(): void {
    this.router.navigate(['/main']);
  }

  isMyMessage(message: MessageResponse): boolean {
    return message.senderId === this.currentUserId;
  }

  private scrollToBottom(): void {
    const chatBody = document.querySelector('.messages-container');

    if (chatBody) {
      chatBody.scrollTop = chatBody.scrollHeight;
    }
  }

  ngOnDestroy(): void {
    this.messageSubscription?.unsubscribe();
    this.websocketService.disconnect();
  }
}