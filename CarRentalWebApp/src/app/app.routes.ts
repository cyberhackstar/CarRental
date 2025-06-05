import { Routes } from '@angular/router';
import { CarFormComponent } from './components/car-form/car-form.component';
import { CarListComponent } from './components/car-list/car-list.component';
import { BookingFormComponent } from './components/booking-form/booking-form.component';
import { BookingListComponent } from './components/booking-list/booking-list.component';
import { HeaderComponent } from './components/header/header.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';

import { RoleGuard } from'./guards/role.guard';
import { DialogHeadlessDemo } from './components/dialog-headless-demo/dialog-headless-demo.component';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },

  // User routes
  {
    path: 'book/:carId',
    component: BookingFormComponent,
    canActivate: [RoleGuard],
    data: { roles: ['USER'] }
  },
  {
    path: 'bookings',
    component: BookingListComponent,
    canActivate: [RoleGuard],
    data: { roles: ['USER'] }
  },

  // Admin routes
  {
    path: 'cars/new',
    component: CarFormComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'cars',
    component: CarListComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN'] }
  },

  // Shared or public
  { path: 'header', component: HeaderComponent },

  // Fallback
  { path: '**', redirectTo: '' }
];
