import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { AdminRegisterComponent } from "./components/super_admin/admin-register/admin-register.component";
import { SignupComponent } from "./components/signup/signup.component";
import { HeroComponent } from "./components/hero/hero.component";
import { FeaturedVehiclesComponent } from "./components/featured-vehicles/featured-vehicles.component";
import { ServicesSectionComponent } from "./components/services-section/services-section.component";
import { TestimonialSectionComponent } from './components/testimonial-section/testimonial-section.component';
import { LoginComponent } from "./components/login/login.component";
import { DialogHeadlessDemo } from './components/dialog-headless-demo/dialog-headless-demo.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'CarRentalWebApp';
}
