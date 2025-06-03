import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Car } from '../models/car.model';
import { Booking } from '../models/booking.model';

@Injectable({
  providedIn: 'root'
})
export class CarRentalService {
  private baseUrl = 'http://localhost:8081/api'; // Adjust if your backend runs on a different port

  constructor(private http: HttpClient) {}

  // Car APIs
  getAvailableCars(): Observable<Car[]> {
    return this.http.get<Car[]>(`${this.baseUrl}/cars`);
  }

  getCarById(id: number): Observable<Car> {
    return this.http.get<Car>(`${this.baseUrl}/cars/${id}`);
  }

  createCar(car: Car): Observable<Car> {
    return this.http.post<Car>(`${this.baseUrl}/cars`, car);
  }

  // Booking APIs
  getBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.baseUrl}/bookings`);
  }

  getBookingById(id: number): Observable<Booking> {
    return this.http.get<Booking>(`${this.baseUrl}/bookings/${id}`);
  }

  bookCar(booking: Booking): Observable<Booking> {
    return this.http.post<Booking>(`${this.baseUrl}/bookings`, booking);
  }
}
