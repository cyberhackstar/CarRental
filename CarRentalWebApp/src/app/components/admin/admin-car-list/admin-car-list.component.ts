import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';;
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LoadingComponent } from '../../../features/loading/loading.component';
import { CarRentalService } from '../../../services/car-rental.service';
import { Car } from '../../../models/car.model';

@Component({
  selector: 'app-admin-car-list',
  imports: [CommonModule, FormsModule,LoadingComponent],
  templateUrl: './admin-car-list.component.html',
  styleUrl: './admin-car-list.component.css',
})
export class AdminCarListComponent {
  @ViewChild('carListSection') carListSection!: ElementRef;
  cars: Car[] = [];
  errorMessage = '';
  carsLoaded = false;
  loading = false;

  constructor(
    private carRentalService: CarRentalService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fetchAllCars();
  }

  fetchAllCars(): void {
    this.loading = true;
    const minTime = 1500;
    const startTime = Date.now();

    this.carRentalService.getAllCars().subscribe({
      next: (cars) => {
        this.cars = cars;
        this.errorMessage = '';
        const elapsed = Date.now() - startTime;
        const remaining = minTime - elapsed;

        setTimeout(
          () => {
            this.loading = false;
            this.carsLoaded = true;

            setTimeout(() => {
              this.carListSection?.nativeElement.scrollIntoView({
                behavior: 'smooth',
              });
            }, 100);
          },
          remaining > 0 ? remaining : 0
        );
      },
      error: () => {
        this.cars = [];
        this.carsLoaded = false;
        this.errorMessage = 'Failed to load cars.';
        this.loading = false;
      },
    });
  }

  getImageUrl(imageUrl: string | undefined | null): string {
    return imageUrl && imageUrl.trim() !== ''
      ? imageUrl
      : 'assets/default-car.jpg';
  }

  updateCar(car: Car): void {
  this.router.navigate(['/cars/update', car.id]);
}

deleteCar(carId: number): void {
  if (confirm('Are you sure you want to delete this car?')) {
    this.carRentalService.deleteCar(carId).subscribe({
      next: () => {
        this.cars = this.cars.filter(c => c.id !== carId);
      },
      error: () => {
        alert('Failed to delete car.');
      }
    });
  }
}

}
