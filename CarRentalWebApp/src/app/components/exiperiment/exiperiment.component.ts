
import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ViewChild, ElementRef } from '@angular/core';
import { Car } from '../../models/car.model';
import { CarRentalService } from '../../services/car-rental.service';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-exiperiment',
  imports: [CommonModule,FormsModule],
  templateUrl: './exiperiment.component.html',
  styleUrls: ['./exiperiment.component.css'],
})
export class ExiperimentComponent {

 startDate: string = '';
 endDate: string = '';
 availableCars: Car[] = [];
  errorMessage = '';


 constructor(private carService: CarRentalService) {}

 searchAvailableCars(): void {
  if (!this.startDate || !this.endDate) return;

 this.carService.getAvailableCarsByDate(this.startDate, this.endDate).subscribe({
 next: (cars) => this.availableCars = cars,
 error: () => this.availableCars = []
 });
 }

getImageUrl(imageName: string | undefined | null): string {
  const isValidImagePath = imageName && imageName.startsWith('/api/cars/image/');
  return isValidImagePath ? `http://localhost:8081${imageName}` : 'assets/default-car.jpg';
}

}
