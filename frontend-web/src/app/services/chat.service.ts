import { Injectable } from '@angular/core';
import { API_CONFIG } from '../config/api.config';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  ConversationResponse,
  CreateConversationRequest,
  MessageResponse,
} from '../models/chat.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private get apiUrl(): string {
    return `${this.authService.getApiBaseUrl()}/api`;
  }

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  createConversation(
    request: CreateConversationRequest
  ): Observable<ConversationResponse> {
    return this.http.post<ConversationResponse>(
      `${this.apiUrl}/conversations`,
      request,
      { headers: this.getAuthHeaders() }
    );
  }

  getMyConversations(): Observable<ConversationResponse[]> {
    return this.http.get<ConversationResponse[]>(
      `${this.apiUrl}/conversations/my`,
      { headers: this.getAuthHeaders() }
    );
  }

  getMessages(conversationId: number): Observable<MessageResponse[]> {
    return this.http.get<MessageResponse[]>(
      `${this.apiUrl}/messages/conversation/${conversationId}`,
      { headers: this.getAuthHeaders() }
    );
  }

  sendMessage(
    conversationId: number,
    content: string
  ): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(
      `${this.apiUrl}/messages/send`,
      {
        conversationId,
        content,
      },
      { headers: this.getAuthHeaders() }
    );
  }

  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`,
    });
  }
}