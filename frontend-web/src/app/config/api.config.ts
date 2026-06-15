import { Capacitor } from '@capacitor/core';

const isNative = Capacitor.isNativePlatform();
const RAILWAY_API_URL = 'https://api-gateway-production-b3d3.up.railway.app';
const PC_LOCAL_IP = '192.168.1.9';

export const API_CONFIG = {
  BASE_URLS: isNative
    ? [
        RAILWAY_API_URL,
        `http://${PC_LOCAL_IP}:8080`, 
        'http://10.0.2.2:8080',
      ]
    : [
        RAILWAY_API_URL,
        'http://localhost:8080',
        `http://${PC_LOCAL_IP}:8080`,
      ],

  DEFAULT_BASE_URL: RAILWAY_API_URL,
    

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