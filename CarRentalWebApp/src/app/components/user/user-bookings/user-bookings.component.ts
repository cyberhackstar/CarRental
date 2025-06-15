import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';
import { LoadingComponent } from '../../loading/loading.component';
import { Booking } from '../../../models/booking.model';
import { CarRentalService } from '../../../services/car-rental.service';


@Component({
  selector: 'app-user-bookings',
  standalone: true,
  imports: [CommonModule, LoadingComponent],
  templateUrl: './user-bookings.component.html',
  styleUrls: ['./user-bookings.component.css'],
})
export class UserBookingsComponent implements OnInit {
  bookings: Booking[] = [];
  errorMessage = '';
  loading = false;

  constructor(private carRentalService: CarRentalService) {}

  ngOnInit(): void {
    this.loading = true;
    const minTime = 1500;
    const startTime = Date.now();

    this.carRentalService.getBookingsByUsername().subscribe({
      next: (data) => {
        this.bookings = data;

        const elapsed = Date.now() - startTime;
        const remaining = minTime - elapsed;

        setTimeout(() => {
          this.loading = false;
        }, remaining > 0 ? remaining : 0);
      },
      error: () => {
        this.errorMessage = 'Failed to load user bookings.';
        this.loading = false;
      },
    });
  }

  downloadBookingsAsExcel(): void {
    const worksheet = XLSX.utils.json_to_sheet(this.bookings);
    const workbook = {
      Sheets: { 'User Bookings': worksheet },
      SheetNames: ['User Bookings'],
    };
    const excelBuffer: any = XLSX.write(workbook, {
      bookType: 'xlsx',
      type: 'array',
    });
    const data: Blob = new Blob([excelBuffer], {
      type: 'application/octet-stream',
    });
    FileSaver.saveAs(data, 'User_Bookings.xlsx');
  }
}
