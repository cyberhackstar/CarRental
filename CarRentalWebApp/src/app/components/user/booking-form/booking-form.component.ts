import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Car } from '../../../models/car.model';
import { Booking } from '../../../models/booking.model';
import { CarRentalService } from '../../../services/car-rental.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';

declare var Razorpay: any;

@Component({
  selector: 'app-booking-form',
  standalone: true,
  imports: [CommonModule, FormsModule, CalendarModule],
  templateUrl: './booking-form.component.html',
  styleUrls: ['./booking-form.component.css'],
})
export class BookingFormComponent implements OnInit {
  cars: Car[] = [];
  selectedCar: Car | undefined;
  booking: Booking = {
    carId: 0,
    customerName: '',
    customerEmail: '', 
    startDate: '',
    endDate: '',
    totalAmount: 0,
  };
  successMessage = '';
  errorMessage = '';
  loading: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private carRentalService: CarRentalService, private router:Router
  ) {}

  ngOnInit(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
    const carIdFromRoute = this.route.snapshot.paramMap.get('carId');
    const startDateFromQuery = this.route.snapshot.queryParamMap.get('startDate');
    const endDateFromQuery = this.route.snapshot.queryParamMap.get('endDate');

    if (carIdFromRoute) {
      this.booking.carId = +carIdFromRoute;
    }
    if (startDateFromQuery) {
      this.booking.startDate = startDateFromQuery;
    }
    if (endDateFromQuery) {
      this.booking.endDate = endDateFromQuery;
    }

    this.carRentalService.getAvailableCars().subscribe({
      next: (data) => {
        this.cars = data;
        this.selectedCar = this.cars.find(car => car.id === this.booking.carId);
        this.calculateTotalAmount();
      },
      error: () => this.errorMessage = 'Failed to load cars.'
    });
  }

  calculateTotalAmount(): void {
    const selectedCar = this.cars.find(car => car.id === this.booking.carId);
    if (selectedCar && this.booking.startDate && this.booking.endDate) {
      const start = new Date(this.booking.startDate);
      const end = new Date(this.booking.endDate);
      const days = (end.getTime() - start.getTime()) / (1000 * 3600 * 24) + 1;
      this.booking.totalAmount = days * selectedCar.pricePerDay;
    }
  }

  submitBooking(): void {
    this.calculateTotalAmount();
    this.carRentalService.bookCar(this.booking).subscribe({
      next: () => {
        this.successMessage = 'Booking successful!';
        this.errorMessage = '';
        this.router.navigate(['/bookings']); // Redirect to bookings page
      },
      error: () => {
        this.errorMessage = 'Booking failed. Please try again.';
        this.successMessage = '';
      }
    });
  }

  bookCar(): void {
  this.loading = true;
  this.calculateTotalAmount();

  this.carRentalService.bookCarWithPayment(this.booking).subscribe({
    next: (response) => {
      const booking = response.booking;
      const razorpayOrder = typeof response.razorpayOrder === 'string'
        ? JSON.parse(response.razorpayOrder)
        : response.razorpayOrder;

      const options: any = {
        key: 'rzp_test_wY3wb7z6dH7O5b', // Replace with your Razorpay key
        amount: razorpayOrder.amount,
        currency: razorpayOrder.currency,
        name: 'CarRental',
        description: 'Car Booking Payment',
        order_id: razorpayOrder.id,
        handler: (res: any) => {
          console.log('Payment response:', res);
          this.successMessage = 'Payment successful! Booking will be confirmed shortly.';
          this.errorMessage = '';
          this.loading = false;
          this.router.navigate(['/user/bookings']);
        },
        prefill: {
          name: booking.customerName,
          email: booking.customerEmail
        },
        theme: {
          color: '#007bff'
        }
      };

      const rzp = new Razorpay(options);
      rzp.open();
    },
    error: () => {
      this.errorMessage = 'Failed to initiate payment. Please try again.';
      this.successMessage = '';
      this.loading = false;
    }
  });
}


}






