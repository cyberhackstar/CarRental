import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CarRentalService } from '../../services/car-rental.service';
import { Booking } from '../../models/booking.model';

@Component({
  selector: 'app-booking-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './booking-list.component.html',
  styleUrls: ['./booking-list.component.css']
})
export class BookingListComponent implements OnInit {
  bookings: Booking[] = [];
  errorMessage = '';

  constructor(private carRentalService: CarRentalService) {}

  ngOnInit(): void {
    this.carRentalService.getBookings().subscribe({
      next: (data) => this.bookings = data,
      error: () => this.errorMessage = 'Failed to load bookings.'
    });
  }
}
