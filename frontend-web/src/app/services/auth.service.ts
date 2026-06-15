import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EMPTY, Observable, from, throwError, of } from 'rxjs';
import { catchError, concatMap, defaultIfEmpty, take, tap, timeout } from 'rxjs/operators';

import {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
} from '../models/auth.model';

import {
  API_CONFIG,
  clearActiveApiBaseUrl,
  getActiveApiBaseUrl,
  setActiveApiBaseUrl,
} from '../config/api.config';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  constructor(private http: HttpClient) {}

  login(request: LoginRequest): Observable<AuthResponse> {
    console.log('URLs disponibles:', API_CONFIG.BASE_URLS);

    return from(API_CONFIG.BASE_URLS).pipe(
      concatMap((baseUrl) => {
        console.log('Probando login con:', `${baseUrl}/api/auth/login`);

        return this.http.post<AuthResponse>(`${baseUrl}/api/auth/login`, request).pipe(
          timeout(6000),
          tap(() => {
            console.log('Backend conectado correctamente en:', baseUrl);
            setActiveApiBaseUrl(baseUrl);
          }),
          catchError((error) => {
            console.error('Falló URL:', baseUrl, error);
            return EMPTY;
          })
        );
      }),
      take(1),
      defaultIfEmpty(null as AuthResponse | null),
      concatMap((response) => {
        if (response) {
          return of(response);
        }

        return throwError(() => new Error('No se pudo conectar al backend.'));
      })
    );
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return from(API_CONFIG.BASE_URLS).pipe(
      concatMap((baseUrl) =>
        this.http.post<AuthResponse>(`${baseUrl}/api/auth/register`, request).pipe(
          timeout(6000),
          tap(() => setActiveApiBaseUrl(baseUrl)),
          catchError(() => EMPTY)
        )
      ),
      take(1),
      defaultIfEmpty(null as AuthResponse | null),
      concatMap((response) => {
        if (response) {
          return of(response);
        }

        return throwError(() => new Error('No se pudo conectar al backend.'));
      })
    );
  }

  saveSession(response: AuthResponse): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('userId', response.userId.toString());
    localStorage.setItem('fullName', response.fullName);
    localStorage.setItem('email', response.email);
    localStorage.setItem('role', response.role);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUserId(): number {
    return Number(localStorage.getItem('userId'));
  }

  getFullName(): string {
    return localStorage.getItem('fullName') || '';
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getApiBaseUrl(): string {
    return getActiveApiBaseUrl();
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    localStorage.removeItem('fullName');
    localStorage.removeItem('email');
    localStorage.removeItem('role');
    clearActiveApiBaseUrl();
  }
}