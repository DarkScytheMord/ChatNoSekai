import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AuthService } from './auth.service';
import { API_CONFIG } from '../config/api.config';

import {
  CreateLibraryItemRequest,
  LibraryItemResponse,
  LibraryItemType,
  UpdateLibraryItemRequest,
} from '../models/library.model';

@Injectable({
  providedIn: 'root',
})
export class LibraryService {
  private get apiUrl(): string {
    return `${this.authService.getApiBaseUrl()}/api/library/items`;
  }

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getMyItems(): Observable<LibraryItemResponse[]> {
    return this.http.get<LibraryItemResponse[]>(
      this.apiUrl,
      { headers: this.getAuthHeaders() }
    );
  }

  getItemById(itemId: number): Observable<LibraryItemResponse> {
    return this.http.get<LibraryItemResponse>(
      `${this.apiUrl}/${itemId}`,
      { headers: this.getAuthHeaders() }
    );
  }

  getItemsByType(type: LibraryItemType): Observable<LibraryItemResponse[]> {
    return this.http.get<LibraryItemResponse[]>(
      `${this.apiUrl}/type/${type}`,
      { headers: this.getAuthHeaders() }
    );
  }

  searchByTitle(title: string): Observable<LibraryItemResponse[]> {
    return this.http.get<LibraryItemResponse[]>(
      `${this.apiUrl}/search?title=${encodeURIComponent(title)}`,
      { headers: this.getAuthHeaders() }
    );
  }

  createItem(request: CreateLibraryItemRequest): Observable<LibraryItemResponse> {
    return this.http.post<LibraryItemResponse>(
      this.apiUrl,
      request,
      { headers: this.getAuthHeaders() }
    );
  }

  updateItem(
    itemId: number,
    request: UpdateLibraryItemRequest
  ): Observable<LibraryItemResponse> {
    return this.http.put<LibraryItemResponse>(
      `${this.apiUrl}/${itemId}`,
      request,
      { headers: this.getAuthHeaders() }
    );
  }

  deleteItem(itemId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${itemId}`,
      { headers: this.getAuthHeaders() }
    );
  }

  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`,
    });
  }
}