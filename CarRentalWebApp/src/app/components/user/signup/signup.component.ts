import { Component, AfterViewInit, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { User } from '../../../models/user.model';
import { AuthService } from '../../../services/auth.service';

declare const google: any;

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [FormsModule, CommonModule, ButtonModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent implements OnInit, AfterViewInit {
  user: User = {
    username: '',
    password: '',
    email: '',
    userRole: 'ROLE_USER',
  };

  isUsernameFocused = false;
  isEmailFocused = false;
  isConfirmPasswordFocused = false;
  isPasswordFocused = false;
  agreed = false;
  confirmPassword = '';
  loading = false;
  errorMessage = '';
  successMessage = '';
  showPassword = false;
  showConfirmPassword = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    if (window.google && window.google.accounts) {
      google.accounts.id.initialize({
        client_id: '448352300820-hi510d2gaf720i4nhqo9kko5865h2rlo.apps.googleusercontent.com',
        callback: this.handleGoogleCredentialResponse.bind(this),
      });
    } else {
      console.warn('Google Identity Services script not loaded yet.');
    }
  }

  ngAfterViewInit(): void {
    const interval = setInterval(() => {
      if (window.google && window.google.accounts) {
        google.accounts.id.renderButton(
          document.getElementById('google-signin-button'),
          {
            theme: 'filled_black',
            size: 'large',
            type: 'standard',
            shape: 'pill',
            text: 'signup_with',
            logo_alignment: 'center',
          }
        );
        clearInterval(interval);
      }
    }, 100);
  }

  register(): void {
    if (!this.user.username || !this.user.email || !this.user.password || this.user.password !== this.confirmPassword || !this.agreed) {
      this.errorMessage = 'Please fill all fields correctly.';
      return;
    }

    this.loading = true;
    this.authService.register(this.user).subscribe({
      next: () => {
        this.successMessage = 'Registration successful! Redirecting to login...';
        this.router.navigate(['/login']);
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Registration failed. Please try again.';
        console.error(err);
        this.loading = false;
      },
    });
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  handleGoogleCredentialResponse(response: any): void {
    const idToken = response.credential;
    this.authService.googleSignUp(idToken).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.errorMessage = err.error || 'Google signup failed';
      },
    });
  }
}
