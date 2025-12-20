// Pfad: src/app/app-routing.module.ts

import { Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './guards/auth.guard';

/**
 * Application routes.
 *
 * <p>Routing rules:
 * <ul>
 *   <li>Unauthenticated users are redirected to /login</li>
 *   <li>Authenticated users may access the home route (/)</li>
 * </ul>
 */
export const routes: Routes = [

  // Default route: Home (only if logged in)
  {
    path: '',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },

  // Login route (public)
  {
    path: 'login',
    component: LoginComponent
  },

  // Fallback: redirect everything unknown to home
  {
    path: '**',
    redirectTo: ''
  }
];
