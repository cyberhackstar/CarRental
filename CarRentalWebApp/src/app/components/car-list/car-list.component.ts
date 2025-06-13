import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CarRentalService } from '../../services/car-rental.service';
import { Car } from '../../models/car.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CalendarModule } from 'primeng/calendar';
import { LoadingComponent } from '../loading/loading.component';

@Component({
  selector: 'app-car-list',
  standalone: true,
  imports: [CommonModule, FormsModule, CalendarModule, LoadingComponent],
  templateUrl: './car-list.component.html',
  styleUrls: ['./car-list.component.css'],
})
export class CarListComponent implements OnInit {
  @ViewChild('carListSection') carListSection!: ElementRef;
  cars: Car[] = [];
  errorMessage = '';
  startDate: Date | null = null;
  endDate: Date | null = null;

  carsLoaded = false;

  today: Date = new Date();

  loading: boolean = false;

  constructor(
    private carRentalService: CarRentalService,
    private router: Router
  ) {}

  ngOnInit(): void {
 

  }

  searchAvailableCars(): void {
    if (!this.startDate || !this.endDate) return;

    this.loading = true; // Start loading
    const minTime = 1500; // 1 second minimum
    const startTime = Date.now();

    const formattedStartDate = this.formatDate(this.startDate);
    const formattedEndDate = this.formatDate(this.endDate);

    this.carRentalService
      .getAvailableCarsByDate(formattedStartDate, formattedEndDate)
      .subscribe({
        next: (cars) => {
          this.cars = cars;
          this.carsLoaded = true;
          this.errorMessage = '';

          const elapsed = Date.now() - startTime;
          const remaining = minTime - elapsed;

          setTimeout(
            () => {
              this.loading = false;
              this.carsLoaded = true; // Scroll to car list section

              setTimeout(() => {
                this.carListSection?.nativeElement.scrollIntoView({
                  behavior: 'smooth',
                });
              }, 100); // slight delay to ensure DOM is ready
            },
            remaining > 0 ? remaining : 0
          );
        },
        error: () => {
          this.cars = [];
          this.carsLoaded = false;
          this.errorMessage = 'Failed to load available cars.';

          this.loading = false;
        },
      });
  }

  getImageUrl(imageUrl: string | undefined | null): string {
  return imageUrl && imageUrl.trim() !== ''
    ? imageUrl
    : 'assets/default-car.jpg';
}


  goToBooking(car: Car): void {
    console.log('Navigating to booking for car:', car);
    const formattedStartDate = this.formatDate(this.startDate as Date);
    const formattedEndDate = this.formatDate(this.endDate as Date);

    this.router.navigate(['/book', car.id], {
      queryParams: {
        startDate: formattedStartDate,
        endDate: formattedEndDate,
      },
    });
  }

  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`; // Format: YYYY-MM-DD
  }
}
