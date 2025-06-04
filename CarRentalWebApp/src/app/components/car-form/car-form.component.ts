import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { CarRentalService } from '../../services/car-rental.service';

@Component({
  selector: 'app-car-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterModule, HttpClientModule],
  templateUrl: './car-form.component.html',
  styleUrls: ['./car-form.component.css']
})
export class CarFormComponent {
  carForm: FormGroup;
  submitted = false;
  responseMessage = '';
  selectedFile: File | null = null;

  constructor(
    private fb: FormBuilder,
    private carService: CarRentalService,
    private router: Router
  ) {
    this.carForm = this.fb.group({
      brand: ['', Validators.required],
      model: ['', Validators.required],
      available: [true],
      pricePerDay: [0, [Validators.required, Validators.min(1)]],
      image: [null, Validators.required]
    });
  }

  get f() {
    return this.carForm.controls;
  }

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.carForm.patchValue({ image: this.selectedFile });
      this.carForm.get('image')?.updateValueAndValidity();
    }
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.carForm.invalid || !this.selectedFile) {
      this.responseMessage = 'Please fill all fields and select an image.';
      return;
    }

    const formData = new FormData();
    const carData = {
      brand: this.carForm.value.brand,
      model: this.carForm.value.model,
      available: this.carForm.value.available,
      pricePerDay: this.carForm.value.pricePerDay
    };

    formData.append('car', JSON.stringify(carData));
    formData.append('image', this.selectedFile);

    this.carService.createCarWithImage(formData).subscribe({
      next: () => {
        this.responseMessage = 'Car registered successfully!';
        this.carForm.reset({ available: true });
        this.selectedFile = null;
        this.submitted = false;
        this.router.navigate(['/carList']); // Adjust route as needed
      },
      error: (err) => {
        this.responseMessage = 'Car registration failed. Please try again.';
        console.error(err);
      }
    });
  }
}
