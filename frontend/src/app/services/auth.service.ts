import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthRequest } from '../dtos/auth-request';
import { Globals } from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly authBaseUri: string;

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
  ) {
    this.authBaseUri = this.globals.backendUri + '/authentication';
  }

  /**
   * Logs in the user and stores the JWT token and email on success.
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient
      .post(this.authBaseUri, authRequest, { responseType: 'text' })
      .pipe(
        tap(token => this.storeLogin(authRequest.email, token))
      );
  }

  /**
   * Returns true if a token is present.
   * Token validity itself is enforced by the backend.
   */
  isLoggedIn(): boolean {
    return !!localStorage.getItem('authToken');
  }

  /**
   * Logs out the user and clears local session data.
   */
  logoutUser(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userEmail');
  }

  /**
   * Returns the stored JWT token.
   */
  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  /**
   * Returns the email of the logged-in user (for UI purposes).
   */
  getUserEmail(): string | null {
    return localStorage.getItem('userEmail');
  }

  private storeLogin(email: string, token: string): void {
    localStorage.setItem('authToken', token);
    localStorage.setItem('userEmail', email);
  }
}
