import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';
import { PasswordModule } from 'primeng/password';
import { FloatLabelModule } from 'primeng/floatlabel';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, PasswordModule, FloatLabelModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  user: User = {
    username: '',
    password: '',
  };

  isUsernameFocused: boolean = false;
  isPasswordFocused: boolean = false;
  showPassword = false;

  errorMessage = '';
  loading = false;
  value!: string;
  constructor(private authService: AuthService, private router: Router) {}

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

        switch (userRole) {
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

        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = 'Invalid username or password';
        console.error('Login error:', err);
      },
    });
  }

  
  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  
}
