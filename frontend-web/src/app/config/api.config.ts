import { Capacitor } from '@capacitor/core';

const isNative = Capacitor.isNativePlatform();

const PC_LOCAL_IP = '192.168.1.9'; // CAMBIA ESTO POR TU IPv4 REAL

export const API_CONFIG = {
  BASE_URLS: isNative
    ? [
        `http://${PC_LOCAL_IP}:8080`, 
        'http://10.0.2.2:8080',
      ]
    : [
        'http://localhost:8080',
        `http://${PC_LOCAL_IP}:8080`,
      ],

  DEFAULT_BASE_URL: isNative
    ? `http://${PC_LOCAL_IP}:8080`
    : 'http://localhost:8080',

  STORAGE_KEY: 'activeApiBaseUrl',
};

export function getActiveApiBaseUrl(): string {
  return localStorage.getItem(API_CONFIG.STORAGE_KEY) || API_CONFIG.DEFAULT_BASE_URL;
}

export function setActiveApiBaseUrl(baseUrl: string): void {
  localStorage.setItem(API_CONFIG.STORAGE_KEY, baseUrl);
}

export function clearActiveApiBaseUrl(): void {
  localStorage.removeItem(API_CONFIG.STORAGE_KEY);
}