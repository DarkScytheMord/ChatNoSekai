import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonButton,
  IonCard,
  IonCardContent,
  IonContent,
  IonInput,
  IonItem,
  IonText,
} from '@ionic/angular/standalone';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ChatService } from '../services/chat.service';
import { ConversationResponse } from '../models/chat.model';

@Component({
  selector: 'app-main',
  templateUrl: './main.page.html',
  styleUrls: ['./main.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    IonButton,
    IonCard,
    IonCardContent,
    IonContent,
    IonInput,
    IonItem,
    IonText,
  ],
})
export class MainPage {
  fullName = '';
  currentUserId = 0;

  otherUserId?: number;
  conversations: ConversationResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private authService: AuthService,
    private chatService: ChatService,
    private router: Router
  ) {}

  ionViewWillEnter(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigateByUrl('/', { replaceUrl: true });
      return;
    }

    this.fullName = this.authService.getFullName();
    this.currentUserId = this.authService.getUserId();

    this.otherUserId = undefined;
    this.errorMessage = '';
    this.successMessage = '';
    this.conversations = [];

    this.loadConversations();
  }

  loadConversations(): void {
    this.chatService.getMyConversations().subscribe({
      next: (response) => {
        this.conversations = response;
      },
      error: (error) => {
        console.error('Error cargando conversaciones:', error);
        this.errorMessage = 'No se pudieron cargar tus conversaciones.';
      },
    });
  }

  createConversation(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.otherUserId) {
      this.errorMessage = 'Debes ingresar el ID del otro usuario.';
      return;
    }

    if (this.otherUserId === this.currentUserId) {
      this.errorMessage = 'No puedes crear una conversación contigo mismo.';
      return;
    }

    this.loading = true;

    this.chatService
      .createConversation({
        title: 'Chat Anime',
        userIds: [this.otherUserId],
      })
      .subscribe({
        next: () => {
          this.loading = false;
          this.successMessage = 'Conversación creada correctamente.';
          this.otherUserId = undefined;
          this.loadConversations();
        },
        error: (error) => {
          console.error('Error creando conversación:', error);
          this.loading = false;
          this.errorMessage =
            error?.error?.message ||
            error?.error?.error ||
            'No se pudo crear la conversación.';
        },
      });
  }

  openConversation(conversationId: number): void {
    this.router.navigate(['/chat', conversationId]);
  }

  openBot(): void {
    this.router.navigate(['/bot']);
  }

  openLibrary(): void {
    this.router.navigate(['/library']);
  }

  logout(): void {
    this.authService.logout();

    this.fullName = '';
    this.currentUserId = 0;
    this.otherUserId = undefined;
    this.conversations = [];
    this.errorMessage = '';
    this.successMessage = '';

    this.router.navigateByUrl('/', { replaceUrl: true });
  }
}