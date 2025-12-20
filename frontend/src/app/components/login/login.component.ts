import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
  ReactiveFormsModule
} from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';
import { AuthRequest } from '../../dtos/auth-request';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  loginForm: UntypedFormGroup;
  submitted = false;
  error = false;
  errorMessage = '';

  constructor(
    private formBuilder: UntypedFormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  /**
   * Starts form validation and authentication.
   */
  loginUser(): void {
    this.submitted = true;

    if (!this.loginForm.valid) {
      return;
    }

    const authRequest = new AuthRequest(
      this.loginForm.controls.username.value,
      this.loginForm.controls.password.value
    );

    this.authenticateUser(authRequest);
  }

  /**
   * Sends authentication data to the backend.
   */
  private authenticateUser(authRequest: AuthRequest): void {
    this.authService.loginUser(authRequest).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: err => {
        this.error = true;
        this.errorMessage =
          typeof err.error === 'object' ? err.error.error : err.error;
      }
    });
  }

  /**
   * Clears the error state.
   */
  vanishError(): void {
    this.error = false;
  }
}
