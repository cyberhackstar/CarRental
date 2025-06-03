import { Routes } from '@angular/router';
import { CarFormComponent } from './components/car-form/car-form.component';
import { CarListComponent } from './components/car-list/car-list.component';
import { BookingFormComponent } from './components/booking-form/booking-form.component';
import { BookingListComponent } from './components/booking-list/booking-list.component';


export const routes: Routes = [
  { path: '', component: BookingListComponent },
  { path: 'book/:carId', component: BookingFormComponent },
];
