import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonButton,
  IonContent,
  IonItem,
  IonText,
  IonTextarea,
} from '@ionic/angular/standalone';
import { Router } from '@angular/router';
import { BotService } from '../services/bot.service';
import { AuthService } from '../services/auth.service';
import { BotHistoryResponse } from '../models/bot.model';

@Component({
  selector: 'app-bot',
  templateUrl: './bot.page.html',
  styleUrls: ['./bot.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    IonButton,
    IonContent,
    IonItem,
    IonText,
    IonTextarea,
  ],
})
export class BotPage implements OnInit {
  question = '';
  history: BotHistoryResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private botService: BotService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
      return;
    }

    this.loadHistory();
  }

  ask(): void {
    const text = this.question.trim();

    this.errorMessage = '';
    this.successMessage = '';

    if (!text) {
      this.errorMessage = 'Debes escribir una pregunta para el bot.';
      return;
    }

    this.loading = true;

    this.botService.ask(text).subscribe({
      next: () => {
        this.question = '';
        this.loading = false;
        this.successMessage = 'Respuesta generada correctamente.';
        this.loadHistory();
      },
      error: (error) => {
        console.error('Error consultando chatbot:', error);
        this.loading = false;
        this.errorMessage =
          error?.error?.message ||
          error?.error?.error ||
          'No se pudo consultar el chatbot.';
      },
    });
  }

  loadHistory(): void {
    this.botService.getHistory().subscribe({
      next: (response) => {
        this.history = response;
      },
      error: (error) => {
        console.error('Error cargando historial del bot:', error);
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/main']);
  }

  clearQuestion(): void {
    this.question = '';
  }
}