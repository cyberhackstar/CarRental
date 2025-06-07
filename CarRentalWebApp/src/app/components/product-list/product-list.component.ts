import { Component } from '@angular/core';
import { CarRentalService } from '../../services/car-rental.service';
import { Car } from '../../models/car.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CalendarModule } from 'primeng/calendar';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule, CalendarModule],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent {
  cars: Car[] = [];
  errorMessage = '';
  startDate: Date | null = null;
  endDate: Date | null = null;

  carsLoaded = false;

  constructor(
    private carRentalService: CarRentalService,
    private router: Router
  ) {}

  searchAvailableCars(): void {
    if (!this.startDate || !this.endDate) return;

    const formattedStartDate = this.formatDate(this.startDate);
    const formattedEndDate = this.formatDate(this.endDate);

    this.carRentalService
      .getAvailableCarsByDate(formattedStartDate, formattedEndDate)
      .subscribe({
        next: (cars) => {
          this.cars = cars;
          this.carsLoaded = true;
          this.errorMessage = '';
        },
        error: () => {
          this.cars = [];
          this.carsLoaded = false;
          this.errorMessage = 'Failed to load available cars.';
        },
      });
  }

  getImageUrl(imageName: string | undefined | null): string {
    const isValidImagePath = imageName && imageName.trim() !== '';
    return isValidImagePath
      ? `http://localhost:8081/api/cars/image/${imageName}`
      : 'assets/default-car.jpg';
  }

  goToBooking(car: Car): void {
    
  console.log('Navigating to booking for car:', car);
  const formattedStartDate = this.formatDate(this.startDate as Date);
  const formattedEndDate = this.formatDate(this.endDate as Date);

  this.router.navigate(['/book', car.id], {
    queryParams: {
      startDate: formattedStartDate,
      endDate: formattedEndDate
    }
  });
}


  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`; // Format: YYYY-MM-DD
  }
}
