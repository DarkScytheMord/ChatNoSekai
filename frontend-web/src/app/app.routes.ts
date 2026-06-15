import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./home/home.page').then((m) => m.HomePage),
  },
  {
    path: 'home',
    redirectTo: '',
    pathMatch: 'full',
  },
  {
    path: 'main',
    loadComponent: () =>
      import('./main/main.page').then((m) => m.MainPage),
  },
  {
    path: 'chat/:conversationId',
    loadComponent: () =>
      import('./chat/chat.page').then((m) => m.ChatPage),
  },
  {
    path: 'bot',
    loadComponent: () =>
      import('./bot/bot.page').then((m) => m.BotPage),
  },
  {
    path: 'library',
    loadComponent: () => import('./library/library.page').then( m => m.LibraryPage)
  },
  {
    path: 'library-form',
    loadComponent: () => import('./library-form/library-form.page').then( m => m.LibraryFormPage)
  },
  {
    path: 'library',
    loadComponent: () =>
      import('./library/library.page').then((m) => m.LibraryPage),
  },
  {
    path: 'library-form',
    loadComponent: () =>
      import('./library-form/library-form.page').then((m) => m.LibraryFormPage),
  },
  {
    path: 'library-form/:id',
    loadComponent: () =>
      import('./library-form/library-form.page').then((m) => m.LibraryFormPage),
  },
];