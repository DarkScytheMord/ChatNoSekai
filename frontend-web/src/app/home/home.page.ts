import { Component } from '@angular/core';
import { Router } from '@angular/router';
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
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
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
export class HomePage {
  isRegisterMode = false;

  fullName = '';
  email = '';
  password = '';

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  changeMode(): void {
    this.isRegisterMode = !this.isRegisterMode;
    this.errorMessage = '';
    this.successMessage = '';
  }

  submit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.email || !this.password) {
      this.errorMessage = 'Debes ingresar correo y contraseña.';
      return;
    }

    if (this.isRegisterMode && !this.fullName) {
      this.errorMessage = 'Debes ingresar tu nombre completo.';
      return;
    }

    this.loading = true;

    if (this.isRegisterMode) {
      this.register();
    } else {
      this.login();
    }
  }

  private login(): void {
    this.authService
      .login({
        email: this.email,
        password: this.password,
      })
      .subscribe({
        next: (response) => {
          this.authService.saveSession(response);

          this.loading = false;
          this.successMessage = `Bienvenido, ${response.fullName}. Login correcto.`;

          setTimeout(() => {
            this.router.navigateByUrl('/main', { replaceUrl: true });
          }, 700);
        },
        error: () => {
          this.loading = false;
          this.errorMessage = 'Correo o contraseña incorrectos.';
          this.successMessage = '';
        },
      });
  }

  private register(): void {
    this.authService
      .register({
        fullName: this.fullName,
        email: this.email,
        password: this.password,
      })
      .subscribe({
        next: (response) => {
          this.authService.saveSession(response);
          this.loading = false;
          this.successMessage = `Cuenta creada correctamente. Bienvenido, ${response.fullName}.`;

          setTimeout(() => {
            this.router.navigate(['/main']);
          }, 700);
        },
        error: () => {
          this.loading = false;
          this.errorMessage =
            'No se pudo registrar el usuario. Puede que el correo ya exista.';
        },
      });
  }
}