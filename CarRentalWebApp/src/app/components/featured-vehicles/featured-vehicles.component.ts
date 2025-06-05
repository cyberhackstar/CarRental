import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { OwlOptions } from 'ngx-owl-carousel-o';
import { CarouselModule } from 'ngx-owl-carousel-o';
import { Car } from '../../models/car.model';
import { CarRentalService } from '../../services/car-rental.service';

@Component({
  selector: 'app-featured-vehicles',
  standalone: true,
  imports: [CommonModule, CarouselModule],
  templateUrl: './featured-vehicles.component.html',
  styleUrls: ['./featured-vehicles.component.css']
})
export class FeaturedVehiclesComponent implements OnInit {
  cars: Car[] = [];

  customOptions: OwlOptions = {
    loop: true,
    margin: 10,
    nav: true,
    dots: false,
    autoplay: true,
    autoplayTimeout: 3000,
    navText: ['<', '>'],
    responsive: {
      0: { items: 1 },
      600: { items: 2 },
      1000: { items: 3 }
    }
  };

  constructor(private carService: CarRentalService) {}

  ngOnInit(): void {
    this.carService.getAvailableCars().subscribe({
      next: (data) => this.cars = data,
      error: (err) => console.error('Failed to load cars:', err)
    });
  }
}
