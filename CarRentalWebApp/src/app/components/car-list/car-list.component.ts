import { Component, OnInit } from '@angular/core';
import { Car } from '../../models/car.model';
import { CarRentalService } from '../../services/car-rental.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-car-list',
  imports: [CommonModule, RouterModule],
  standalone: true,
  templateUrl: './car-list.component.html',
  styleUrls: ['./car-list.component.css']
})
export class CarListComponent implements OnInit {
  cars: Car[] = [];
  errorMessage = '';

  constructor(private carRentalService: CarRentalService) {}

  ngOnInit(): void {
    this.carRentalService.getAvailableCars().subscribe({
      next: (data) => this.cars = data,
      error: (err) => this.errorMessage = 'Failed to load cars.'
    });
  }
}
