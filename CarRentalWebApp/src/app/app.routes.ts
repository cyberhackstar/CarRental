import { Routes } from '@angular/router';
import { CarFormComponent } from './components/admin/car-form/car-form.component';
import { CarListComponent } from './components/user/car-list/car-list.component';
import { BookingFormComponent } from './components/user/booking-form/booking-form.component';
import { BookingListComponent } from './components/admin/booking-list/booking-list.component';
import { HeaderComponent } from './components/header/header.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/user/signup/signup.component';
import { DialogHeadlessDemo } from './components/dialog-headless-demo/dialog-headless-demo.component';
import { FooterComponent } from './components/footer/footer.component';
import { ServicesSectionComponent } from './components/services-section/services-section.component';
import { HeroSection2Component } from './components/hero-section2/hero-section2.component';
import { UserBookingsComponent } from './components/user/user-bookings/user-bookings.component';
import { AdminCarListComponent } from './components/admin/admin-car-list/admin-car-list.component';
import { AdminCarUpdateComponent } from './components/admin/admin-car-update/admin-car-update.component';
import { AdminRegisterComponent } from './components/super_admin/admin-register/admin-register.component';
import { RoleGuard } from './guards/role.guard';

export const routes: Routes = [
  // Public routes
  { path: '', component: HeroSection2Component },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'admin/create', component: AdminRegisterComponent , canActivate: [RoleGuard], data: { roles: ['ROLE_SUPER'] }},

  // Booking routes (no auth for now)
  { path: 'book/:carId', component: BookingFormComponent , canActivate: [RoleGuard], data: { roles: ['ROLE_USER'] } },
  { path: 'bookings', component: BookingListComponent , canActivate: [RoleGuard], data: { roles: ['ROLE_ADMIN'] } },
  { path: 'user/bookings', component: UserBookingsComponent , canActivate: [RoleGuard], data: { roles: ['ROLE_USER'] } },

  // Admin routes (auth removed for now)
  { path: 'cars/new', component: CarFormComponent , canActivate: [RoleGuard], data: { roles: ['ROLE_ADMIN'] }},
  { path: 'cars', component: CarListComponent , canActivate: [RoleGuard], data: { roles: ['ROLE_ADMIN', 'ROLE_USER'] } },
  { path: 'cars/all', component: AdminCarListComponent , canActivate: [RoleGuard], data: { roles: ['ROLE_ADMIN'] } },
  { path: 'cars/update/:id', component: AdminCarUpdateComponent , canActivate: [RoleGuard], data: { roles: ['ROLE_ADMIN'] } },

  // Shared components
  { path: 'header', component: HeaderComponent },
  { path: 'footer', component: FooterComponent },
  { path: 'services', component: ServicesSectionComponent },
  { path: 'dialog-demo', component: DialogHeadlessDemo },

  // Fallback route
  { path: '**', redirectTo: '' },
];
