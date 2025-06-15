import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';
import { User } from '../../../models/user.model';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-admin-register',
  standalone: true,
  imports: [FormsModule, CommonModule,ButtonModule],
  templateUrl: './admin-register.component.html',
  styleUrls: ['./admin-register.component.css']
})
export class AdminRegisterComponent {
user: User = {
    username: '',
    password: '',
    userRole: 'ROLE_USER', // default
  };

  isUsernameFocused: boolean = false;
  isEmailFocused: boolean = false;
  isPasswordFocused: boolean = false;
  isConfirmPasswordFocused: boolean = false;
  agreed: boolean = false;
  confirmPassword: string = ''
  loading = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  register(): void {
    this.loading = true;
    this.authService.register(this.user).subscribe({
      next: () => {
        this.successMessage =
          'Registration successful! Redirecting to login...';
        this.router.navigate(['/login']);
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Registration failed. Please try again.';
        console.error(err);
      },
    });
  }

  showPassword = false;
  showConfirmPassword = false;

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }



}
