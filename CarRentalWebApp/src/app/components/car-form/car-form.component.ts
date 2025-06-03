import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CarRentalService } from '../../services/car-rental.service';
import { Car } from '../../models/car.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-car-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './car-form.component.html',
  styleUrls: ['./car-form.component.css']
})
export class CarFormComponent {
  carForm: FormGroup;
  successMessage = '';
  errorMessage = '';

  constructor(private fb: FormBuilder, private carService: CarRentalService) {
    this.carForm = this.fb.group({
      brand: ['', Validators.required],
      model: ['', Validators.required],
      available: [true],
      pricePerDay: [0, [Validators.required, Validators.min(1)]]
    });
  }

  onSubmit(): void {
    if (this.carForm.valid) {
      const newCar: Car = this.carForm.value;
      this.carService.createCar(newCar).subscribe({
        next: () => {
          this.successMessage = 'Car registered successfully!';
          this.errorMessage = '';
          this.carForm.reset({ available: true });
        },
        error: () => {
          this.errorMessage = 'Failed to register car. Please try again.';
          this.successMessage = '';
        }
      });
    }
  }
}
