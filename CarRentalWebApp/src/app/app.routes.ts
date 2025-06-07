import { Routes } from '@angular/router';
import { CarFormComponent } from './components/car-form/car-form.component';
import { CarListComponent } from './components/car-list/car-list.component';
import { BookingFormComponent } from './components/booking-form/booking-form.component';
import { BookingListComponent } from './components/booking-list/booking-list.component';
import { HeaderComponent } from './components/header/header.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { DialogHeadlessDemo } from './components/dialog-headless-demo/dialog-headless-demo.component';
import { FooterComponent } from './components/footer/footer.component';
import { ServicesSectionComponent } from './components/services-section/services-section.component';
import { HeroSection2Component } from './components/hero-section2/hero-section2.component';

export const routes: Routes = [
  // Public routes
  { path: '', component: HeroSection2Component },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },

  // Booking routes (no auth for now)
  { path: 'book/:carId', component: BookingFormComponent },
  { path: 'bookings', component: BookingListComponent },

  // Admin routes (auth removed for now)
  { path: 'cars/new', component: CarFormComponent },
  { path: 'cars', component: CarListComponent },

  // Shared components
  { path: 'header', component: HeaderComponent },
  { path: 'footer', component: FooterComponent },
  { path: 'services', component: ServicesSectionComponent },
  { path: 'dialog-demo', component: DialogHeadlessDemo },

  // Fallback route
  { path: '**', redirectTo: '' }
];
