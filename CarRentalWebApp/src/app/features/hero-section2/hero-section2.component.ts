import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { ServicesSectionComponent } from "../services-section/services-section.component";
import { TestimonialSectionComponent } from "../testimonial-section/testimonial-section.component";
import { TotalcarComponent } from "../totalcar/totalcar.component";

@Component({
  selector: 'app-hero-section2',
  imports: [CommonModule, ServicesSectionComponent, TestimonialSectionComponent, TotalcarComponent],
  templateUrl: './hero-section2.component.html',
  styleUrl: './hero-section2.component.css',
})
export class HeroSection2Component {
  constructor(private router: Router, private authService:AuthService) {}

  userRole: string = 'USER'; // Default role

ngOnInit() {
  // Example: Fetch role from a service or localStorage
  const role = localStorage.getItem('userRole');
  this.userRole = role ? role : 'ROLE_USER';
}


  goToCarList(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/cars']);
    } else {
      this.router.navigate(['/login']);
    }
  }

  goToSuperAdmin(){}
  goToAdminPanel(){
    this.router.navigate(['/cars/new']);
  }
}
