import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Car } from '../models/car.model';
import { Booking } from '../models/booking.model';
import { CarResponse } from '../models/car-response.model';

@Injectable({
  providedIn: 'root',
})
export class CarRentalService {
  private baseUrl = 'https://carrentalservice.help/carservice/api'; // Adjust if needed

  constructor(private http: HttpClient) {}

  // Car APIs
  getAvailableCars(): Observable<CarResponse[]> {
    return this.http.get<CarResponse[]>(`${this.baseUrl}/cars`);
  }

  getAllCars(): Observable<CarResponse[]> {
  return this.http.get<CarResponse[]>(`${this.baseUrl}/cars/all`);
}


  getAvailableCarsByDate(
    startDate: string,
    endDate: string
  ): Observable<CarResponse[]> {
    return this.http.get<CarResponse[]>(`${this.baseUrl}/available`, {
      params: { startDate, endDate },
    });
  }

  getCarById(id: number): Observable<Car> {
    return this.http.get<Car>(`${this.baseUrl}/cars/${id}`);
  }

  // ✅ Updated to support FormData (car + image)
  createCarWithImage(formData: FormData): Observable<CarResponse> {
    return this.http.post<CarResponse>(`${this.baseUrl}/cars`, formData);
  }
  
  updateCarWithImage(id: number, car: Car, imageFile?: File): Observable<CarResponse> {
  const formData = new FormData();
  formData.append('car', JSON.stringify(car));
  if (imageFile) {
    formData.append('image', imageFile);
  }

  return this.http.put<CarResponse>(`${this.baseUrl}/cars/${id}`, formData);
}

  deleteCar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/cars/${id}`);
  }

    getImageWithAuth(imageName: string): Observable<string> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http
      .get(`https://carservice.up.railway.app/api/cars/image/${imageName}`, {
        headers,
        responseType: 'blob',
      })
      .pipe(map((blob) => URL.createObjectURL(blob)));
  }

  // Booking APIs
  getBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.baseUrl}/bookings`);
  }

  getBookingsByUsername(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.baseUrl}/bookings/by-username`);
  }

  getBookingById(id: number): Observable<Booking> {
    return this.http.get<Booking>(`${this.baseUrl}/bookings/${id}`);
  }

  bookCar(booking: Booking): Observable<Booking> {
    return this.http.post<Booking>(`${this.baseUrl}/bookings`, booking);
  }



  deleteBooking(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/bookings/${id}`);
  }

  updateBooking(id: number, booking: Booking): Observable<Booking> {
    return this.http.put<Booking>(`${this.baseUrl}/bookings/${id}`, booking);
  }

  bookCarWithPayment(booking: Booking): Observable<any> {
  return this.http.post<any>(`${this.baseUrl}/bookings/with-payment`, booking);
}


  confirmBookingPayment(paymentConfirmation: {
    bookingId: number;
    orderId: string;
    paymentId: string;
  }): Observable<any> {
    return this.http.post<any>(
      `${this.baseUrl}/bookings/confirm-payment`,
      paymentConfirmation
    );
  }
}
