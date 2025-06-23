import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';
import { PasswordModule } from 'primeng/password';
import { FloatLabelModule } from 'primeng/floatlabel';

declare const google: any;

declare global {
  interface Window {
    google: any;
  }
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, PasswordModule, FloatLabelModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit, AfterViewInit {
  user: User = {
    username: '',
    password: '',
  };

  isUsernameFocused = false;
  isPasswordFocused = false;
  showPassword = false;
  errorMessage = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    if (window.google && window.google.accounts) {
      google.accounts.id.initialize({
        client_id: '448352300820-hi510d2gaf720i4nhqo9kko5865h2rlo.apps.googleusercontent.com',
        callback: this.handleCredentialResponse.bind(this),
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
            text: 'signin_with',
            logo_alignment: 'center',
          }
        );
        clearInterval(interval);
      }
    }, 100);
  }

  handleCredentialResponse(response: any): void {
    const idToken = response.credential;

    this.authService.googleLogin(idToken).subscribe({
      next: ({ token, username, userRole }) => {
        localStorage.setItem('token', token);
        localStorage.setItem('username', username);
        localStorage.setItem('userRole', userRole || '');
        this.navigateByRole(userRole);
      },
      error: () => {
        this.errorMessage = 'Google login failed';
      },
    });
  }

  login(): void {
    if (!this.user.username || !this.user.password) {
      this.errorMessage = 'Please enter both username and password.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService.login(this.user).subscribe({
      next: ({ token, username, userRole }) => {
        localStorage.setItem('token', token);
        localStorage.setItem('username', username);
        localStorage.setItem('userRole', userRole || '');
        this.navigateByRole(userRole);
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Invalid username or password';
      },
    });
  }

  navigateByRole(role: string): void {
    switch (role) {
      case 'USER':
        this.router.navigate(['/user/dashboard']);
        break;
      case 'ADMIN':
        this.router.navigate(['/admin/dashboard']);
        break;
      case 'SUPER_ADMIN':
        this.router.navigate(['/superadmin/dashboard']);
        break;
      default:
        this.router.navigate(['/unauthorized']);
    }
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }
}
