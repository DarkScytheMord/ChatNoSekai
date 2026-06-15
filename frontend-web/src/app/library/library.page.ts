import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { IonicModule } from '@ionic/angular';

import { LibraryService } from '../services/library.service';
import {
  LibraryItemResponse,
  LibraryItemType,
} from '../models/library.model';

@Component({
  selector: 'app-library',
  templateUrl: './library.page.html',
  styleUrls: ['./library.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    IonicModule,
  ],
})
export class LibraryPage {
  items: LibraryItemResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  searchTitle = '';
  selectedType: 'ALL' | LibraryItemType = 'ALL';

  constructor(
    private libraryService: LibraryService,
    private router: Router
  ) {}

  ionViewWillEnter(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const title = this.searchTitle.trim();

    if (title.length > 0) {
      this.searchItems(title);
      return;
    }

    if (this.selectedType !== 'ALL') {
      this.loadItemsByType(this.selectedType);
      return;
    }

    this.libraryService.getMyItems().subscribe({
      next: (response) => {
        this.items = response;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'No se pudo cargar tu biblioteca.';
      },
    });
  }

  searchItems(title: string): void {
    this.libraryService.searchByTitle(title).subscribe({
      next: (response) => {
        this.items = response;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'No se pudo realizar la búsqueda.';
      },
    });
  }

  loadItemsByType(type: LibraryItemType): void {
    this.libraryService.getItemsByType(type).subscribe({
      next: (response) => {
        this.items = response;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'No se pudo filtrar la biblioteca.';
      },
    });
  }

  changeType(type: 'ALL' | LibraryItemType): void {
    this.selectedType = type;
    this.searchTitle = '';
    this.loadItems();
  }

  clearSearch(): void {
    this.searchTitle = '';
    this.selectedType = 'ALL';
    this.loadItems();
  }

  goToCreate(): void {
    this.router.navigate(['/library-form']);
  }

  goToEdit(itemId: number): void {
    this.router.navigate(['/library-form', itemId]);
  }

  deleteItem(itemId: number): void {
    const confirmDelete = confirm('¿Seguro que quieres eliminar este registro?');

    if (!confirmDelete) {
      return;
    }

    this.libraryService.deleteItem(itemId).subscribe({
      next: () => {
        this.successMessage = 'Registro eliminado correctamente.';
        this.loadItems();
      },
      error: () => {
        this.errorMessage = 'No se pudo eliminar el registro.';
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/main']);
  }

  getTypeLabel(type: LibraryItemType): string {
    if (type === 'ANIME') {
      return 'Anime';
    }

    return 'Manga';
  }

  getStatusLabel(status: string): string {
    const labels: Record<string, string> = {
      WATCHING: 'Viendo / Leyendo',
      COMPLETED: 'Terminado',
      PENDING: 'Pendiente',
      DROPPED: 'Abandonado',
      FAVORITE: 'Favorito',
    };

    return labels[status] || status;
  }

  getTotalByType(type: LibraryItemType): number {
    return this.items.filter((item) => item.type === type).length;
  }
}