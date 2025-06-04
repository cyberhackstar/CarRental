import { Routes } from '@angular/router';
import { CarFormComponent } from './components/car-form/car-form.component';
import { CarListComponent } from './components/car-list/car-list.component';
import { BookingFormComponent } from './components/booking-form/booking-form.component';
import { BookingListComponent } from './components/booking-list/booking-list.component';
import { HeaderComponent } from './components/header/header.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';


export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  { path: 'book/:carId', component: BookingFormComponent },
  { path: 'cars/new', component: CarFormComponent },
  { path: 'cars', component: CarListComponent },
  { path: 'bookings', component: BookingListComponent },
  { path: 'header', component: HeaderComponent},
  { path: 'signup', component: SignupComponent},
  { path: '**', redirectTo: '' } // Redirect to home for any unknown routes
];
