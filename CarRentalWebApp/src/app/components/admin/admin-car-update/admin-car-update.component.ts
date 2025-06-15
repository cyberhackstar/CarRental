import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { LoadingComponent } from '../../loading/loading.component';
import { CarRentalService } from '../../../services/car-rental.service';
import { Car } from '../../../models/car.model';

@Component({
  selector: 'app-admin-car-update',
  templateUrl: './admin-car-update.component.html',
  styleUrls: ['./admin-car-update.component.css'],
  standalone: true,
  imports: [LoadingComponent, CommonModule, ReactiveFormsModule]
})
export class AdminCarUpdateComponent implements OnInit {
  carForm!: FormGroup;
  carId!: number;
  selectedFile: File | null = null;
  selectedFileName: string = '';
  responseMessage = '';
  loading = false;
  dragging = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private carService: CarRentalService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carId = Number(this.route.snapshot.paramMap.get('id'));
    this.carForm = this.fb.group({
      brand: ['', Validators.required],
      model: ['', Validators.required],
      pricePerDay: [0, [Validators.required, Validators.min(1)]],
      available: [true],
      image: [null]
    });

    this.loadCarDetails();
  }

  loadCarDetails(): void {
    this.carService.getCarById(this.carId).subscribe({
      next: (car: Car) => {
        this.carForm.patchValue({
          brand: car.brand,
          model: car.model,
          pricePerDay: car.pricePerDay,
          available: car.available
        });
      },
      error: () => {
        this.responseMessage = 'Failed to load car details.';
      }
    });
  }

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.selectedFile = input.files[0];
      this.selectedFileName = this.selectedFile.name;
      this.carForm.patchValue({ image: this.selectedFile });
    }
  }

  onSubmit(): void {
    if (this.carForm.invalid) {
      this.responseMessage = 'Please fill all required fields.';
      return;
    }

    const carData: Car = {
      brand: this.carForm.value.brand,
      model: this.carForm.value.model,
      pricePerDay: this.carForm.value.pricePerDay,
      available: this.carForm.value.available
    };

    this.loading = true;

    this.carService.updateCarWithImage(this.carId, carData, this.selectedFile ?? undefined).subscribe({
      next: () => {
        this.responseMessage = 'Car updated successfully!';
        this.router.navigate(['/carList']);
      },
      error: () => {
        this.responseMessage = 'Failed to update car.';
        this.loading = false;
      }
    });
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.dragging = true;
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    this.dragging = false;
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.dragging = false;

    if (event.dataTransfer?.files.length) {
      this.selectedFile = event.dataTransfer.files[0];
      this.selectedFileName = this.selectedFile.name;
      this.carForm.patchValue({ image: this.selectedFile });
    }
  }
}
