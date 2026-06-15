import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { IonicModule } from '@ionic/angular';

import { LibraryService } from '../services/library.service';
import {
  CreateLibraryItemRequest,
  LibraryItemStatus,
  LibraryItemType,
  UpdateLibraryItemRequest,
} from '../models/library.model';

@Component({
  selector: 'app-library-form',
  templateUrl: './library-form.page.html',
  styleUrls: ['./library-form.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    IonicModule,
  ],
})
export class LibraryFormPage {
  itemId?: number;
  editMode = false;

  loading = false;
  saving = false;
  errorMessage = '';
  successMessage = '';
  selectedImageName = '';

  form = {
    title: '',
    type: 'ANIME' as LibraryItemType,
    status: 'PENDING' as LibraryItemStatus,
    description: '',
    imageUrl: '',
    rating: 0,
  };

  types: { value: LibraryItemType; label: string }[] = [
    { value: 'ANIME', label: 'Anime' },
    { value: 'MANGA', label: 'Manga' },
  ];

  statuses: { value: LibraryItemStatus; label: string }[] = [
    { value: 'PENDING', label: 'Pendiente' },
    { value: 'WATCHING', label: 'Viendo / Leyendo' },
    { value: 'COMPLETED', label: 'Terminado' },
    { value: 'DROPPED', label: 'Abandonado' },
    { value: 'FAVORITE', label: 'Favorito' },
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private libraryService: LibraryService
  ) {}

  ionViewWillEnter(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      this.itemId = Number(idParam);
      this.editMode = true;
      this.loadItem(this.itemId);
      return;
    }

    this.editMode = false;
    this.itemId = undefined;
    this.resetForm();
  }

  loadItem(itemId: number): void {
    this.loading = true;
    this.errorMessage = '';

    this.libraryService.getItemById(itemId).subscribe({
      next: (item) => {
        this.form = {
          title: item.title,
          type: item.type,
          status: item.status,
          description: item.description || '',
          imageUrl: item.imageUrl || '',
          rating: item.rating || 0,
        };
        this.selectedImageName = this.isLocalImage()
          ? 'Imagen guardada desde archivo'
          : '';
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'No se pudo cargar el registro.';
      },
    });
  }

  save(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.form.title.trim()) {
      this.errorMessage = 'El título es obligatorio.';
      return;
    }

    if (this.form.rating < 0 || this.form.rating > 10) {
      this.errorMessage = 'La calificación debe estar entre 0 y 10.';
      return;
    }

    this.saving = true;

    if (this.editMode && this.itemId) {
      this.updateItem(this.itemId);
      return;
    }

    this.createItem();
  }

  createItem(): void {
    const request: CreateLibraryItemRequest = {
      title: this.form.title.trim(),
      type: this.form.type,
      status: this.form.status,
      description: this.form.description.trim(),
      imageUrl: this.form.imageUrl.trim(),
      rating: Number(this.form.rating),
    };

    this.libraryService.createItem(request).subscribe({
      next: () => {
        this.saving = false;
        this.successMessage = 'Registro creado correctamente.';

        setTimeout(() => {
          this.router.navigate(['/library']);
        }, 600);
      },
      error: () => {
        this.saving = false;
        this.errorMessage = 'No se pudo crear el registro.';
      },
    });
  }

  updateItem(itemId: number): void {
    const request: UpdateLibraryItemRequest = {
      title: this.form.title.trim(),
      type: this.form.type,
      status: this.form.status,
      description: this.form.description.trim(),
      imageUrl: this.form.imageUrl.trim(),
      rating: Number(this.form.rating),
    };

    this.libraryService.updateItem(itemId, request).subscribe({
      next: () => {
        this.saving = false;
        this.successMessage = 'Registro actualizado correctamente.';

        setTimeout(() => {
          this.router.navigate(['/library']);
        }, 600);
      },
      error: () => {
        this.saving = false;
        this.errorMessage = 'No se pudo actualizar el registro.';
      },
    });
  }

  resetForm(): void {
    this.form = {
      title: '',
      type: 'ANIME',
      status: 'PENDING',
      description: '',
      imageUrl: '',
      rating: 0,
    };

    this.selectedImageName = '';
    this.errorMessage = '';
    this.successMessage = '';
  }

  goBack(): void {
    this.router.navigate(['/library']);
  }

  getTypeLabel(type: LibraryItemType): string {
    return type === 'ANIME' ? 'Anime' : 'Manga';
  }

  getStatusLabel(status: LibraryItemStatus): string {
    const labels: Record<LibraryItemStatus, string> = {
      PENDING: 'Pendiente',
      WATCHING: 'Viendo / Leyendo',
      COMPLETED: 'Terminado',
      DROPPED: 'Abandonado',
      FAVORITE: 'Favorito',
    };

    return labels[status];
  }
  onImageSelected(event: Event): void {
    this.errorMessage = '';

    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      return;
    }

    if (!file.type.startsWith('image/')) {
      this.errorMessage = 'El archivo seleccionado debe ser una imagen.';
      return;
    }

    const maxSizeInMB = 2;
    const maxSizeInBytes = maxSizeInMB * 1024 * 1024;

    if (file.size > maxSizeInBytes) {
      this.errorMessage = `La imagen no debe superar los ${maxSizeInMB} MB.`;
      return;
    }

    const reader = new FileReader();

    reader.onload = () => {
      this.form.imageUrl = reader.result as string;
      this.selectedImageName = file.name;
    };

    reader.onerror = () => {
      this.errorMessage = 'No se pudo leer la imagen seleccionada.';
    };

    reader.readAsDataURL(file);
  }

  clearImage(fileInput?: HTMLInputElement): void {
    this.form.imageUrl = '';
    this.selectedImageName = '';

    if (fileInput) {
      fileInput.value = '';
    }
  }

  isLocalImage(): boolean {
    return this.form.imageUrl.startsWith('data:image');
  }
}