import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CarRentalService } from '../../services/car-rental.service';
import { Booking } from '../../models/booking.model';

import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';
import { LoadingComponent } from '../loading/loading.component';

@Component({
  selector: 'app-booking-list',
  standalone: true,
  imports: [CommonModule, LoadingComponent],
  templateUrl: './booking-list.component.html',
  styleUrls: ['./booking-list.component.css'],
})
export class BookingListComponent implements OnInit {
  bookings: Booking[] = [];
  errorMessage = '';
  loading = false; // <-- Add this line

  constructor(private carRentalService: CarRentalService) {}

  ngOnInit(): void {
    this.loading = true; // Start loading
    const minTime = 1500; // 1 second minimum
    const startTime = Date.now();

    this.carRentalService.getBookings().subscribe({
      next: (data) => {
        this.bookings = data;

        const elapsed = Date.now() - startTime;
        const remaining = minTime - elapsed;

        setTimeout(
          () => {
            this.loading = false;
          },
          remaining > 0 ? remaining : 0
        );
      },
      error: () => {
        this.errorMessage = 'Failed to load bookings.';
        this.loading = false; // Stop loading
      },
    });
  }

  downloadBookingsAsExcel(): void {
    const worksheet = XLSX.utils.json_to_sheet(this.bookings);
    const workbook = {
      Sheets: { Bookings: worksheet },
      SheetNames: ['Bookings'],
    };
    const excelBuffer: any = XLSX.write(workbook, {
      bookType: 'xlsx',
      type: 'array',
    });
    const data: Blob = new Blob([excelBuffer], {
      type: 'application/octet-stream',
    });
    FileSaver.saveAs(data, 'Bookings_List.xlsx');
  }
}
