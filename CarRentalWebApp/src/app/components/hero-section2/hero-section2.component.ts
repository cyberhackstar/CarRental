import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-hero-section2',
  imports: [],
  templateUrl: './hero-section2.component.html',
  styleUrl: './hero-section2.component.css',
})
export class HeroSection2Component {
  constructor(private router: Router, private authService:AuthService) {}

  goToCarList(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/cars']);
    } else {
      this.router.navigate(['/login']);
    }
  }
}
