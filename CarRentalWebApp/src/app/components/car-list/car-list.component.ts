import { Component, OnInit } from '@angular/core';
import { Car } from '../../models/car.model';
import { CarRentalService } from '../../services/car-rental.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-car-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './car-list.component.html',
  styleUrls: ['./car-list.component.css']
})
export class CarListComponent implements OnInit {
  cars: Car[] = [];
  errorMessage = '';

  constructor(private carRentalService: CarRentalService) {}

  ngOnInit(): void {
    this.loadCars();
  }

  loadCars(): void {
    this.carRentalService.getAvailableCars().subscribe({
      next: (data) => this.cars = data,
      error: () => this.errorMessage = 'Failed to load cars.'
    });
  }

  getImageUrl(imageName: string | undefined | null): string {
  const isValidImagePath = imageName && imageName.startsWith('/api/cars/image/');
  return isValidImagePath ? `http://localhost:8081${imageName}` : 'assets/default-car.jpg';
}

}
