import { Injectable } from '@angular/core';
import { API_CONFIG } from '../config/api.config';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BotHistoryResponse, BotResponse } from '../models/bot.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class BotService {
  private get apiUrl(): string {
    return `${this.authService.getApiBaseUrl()}/api/bot`;
  }

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  ask(question: string): Observable<BotResponse> {
    return this.http.post<BotResponse>(
      `${this.apiUrl}/ask`,
      { question },
      { headers: this.getAuthHeaders() }
    );
  }

  getHistory(): Observable<BotHistoryResponse[]> {
    return this.http.get<BotHistoryResponse[]>(
      `${this.apiUrl}/history`,
      { headers: this.getAuthHeaders() }
    );
  }

  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`,
    });
  }
}