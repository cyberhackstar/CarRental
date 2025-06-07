import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-login2',
  imports: [],
  templateUrl: './login2.component.html',
  styleUrl: './login2.component.css'
})
export class Login2Component {

  user: User = {
    username: '',
    password: ''
  };

  errorMessage = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {}

  login() {
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
      }
    });
  }

}
