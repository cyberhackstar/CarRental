import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';
import { PasswordModule } from 'primeng/password';
import { FloatLabelModule } from 'primeng/floatlabel';

declare const google: any;

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

  isUsernameFocused: boolean = true;
  isPasswordFocused: boolean = true;
  showPassword = false;

  errorMessage = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    google.accounts.id.initialize({
      client_id:
        '448352300820-hi510d2gaf720i4nhqo9kko5865h2rlo.apps.googleusercontent.com',
      callback: this.handleCredentialResponse.bind(this),
    });
  }

  ngAfterViewInit(): void {
    google.accounts.id.renderButton(
      document.getElementById('google-signin-button'),
      {
        theme: 'filled_black', // Dark theme
        size: 'large',
        type: 'standard',
        shape: 'pill',
        text: 'signin_with',
        logo_alignment: 'center',
      }
    );
  }

  handleGoogleLogin(): void {
  google.accounts.id.initialize({
    client_id: 'YOUR_CLIENT_ID',
    callback: this.handleCredentialResponse.bind(this),
  });

  google.accounts.id.prompt(); // Opens the Google login popup
}



  handleCredentialResponse(response: any) {
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

  login() {
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

  navigateByRole(role: string) {
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
