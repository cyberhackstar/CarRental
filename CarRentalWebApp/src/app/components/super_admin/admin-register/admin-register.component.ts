import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-admin-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './admin-register.component.html',
  styleUrls: ['./admin-register.component.css']
})
export class AdminRegisterComponent {
  admin = {
    username: '',
    password: '',
    confirmPassword: '',
    userRole: 'ADMIN'
  };
  errorMessage: string = '';
  successMessage: string = '';
  constructor(private http: HttpClient, private router: Router, private authService: AuthService) {}
 

  registerAdmin(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.admin.username || this.admin.username.length < 4) {
      this.errorMessage = 'Username must be at least 4 characters long.';
      return;
    }

    if (!this.admin.password || this.admin.password.length < 6) {
      this.errorMessage = 'Password must be at least 6 characters long.';
      return;
    }

    if (this.admin.password !== this.admin.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    const payload = {
      username: this.admin.username,
      password: this.admin.password,
      userRole: this.admin.userRole
    };

    this.authService.register(payload).subscribe({
      next: () => {
        this.successMessage = 'Admin registered successfully';
        this.router.navigate(['/superadmin/dashboard']);
      },
      error: (err) => {
        this.errorMessage = 'Registration failed: ' + (err.error?.message || 'Unknown error');
      }
    });
  }
}
