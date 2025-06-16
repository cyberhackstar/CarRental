import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { CarRentalService } from '../../../services/car-rental.service';
import { LoadingComponent } from "../../../features/loading/loading.component";

@Component({
  selector: 'app-car-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterModule, HttpClientModule, LoadingComponent],
  templateUrl: './car-form.component.html',
  styleUrls: ['./car-form.component.css']
})
export class CarFormComponent {
  carForm: FormGroup;
  submitted = false;
  responseMessage = '';
  selectedFile: File | null = null;
  selectedFileName: string = '';
  dragging = false;
  loading: boolean = false;

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
      image: [null]
    });
  }

  get f() {
    return this.carForm.controls;
  }

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.selectedFile = file;
      this.selectedFileName = file.name;
      this.carForm.patchValue({ image: file });
      this.carForm.get('image')?.updateValueAndValidity();
    }
  }

  onSubmit(): void {
    this.loading = true;
    this.submitted = true;

    if (this.carForm.invalid || !this.selectedFile) {
      this.responseMessage = 'Please fill all fields and select an image.';
      return;
    }

    const carData = {
      brand: this.carForm.value.brand,
      model: this.carForm.value.model,
      available: this.carForm.value.available,
      pricePerDay: this.carForm.value.pricePerDay
    };

    const formData = new FormData();
    formData.append('car', JSON.stringify(carData));
    formData.append('image', this.selectedFile);

    this.carService.createCarWithImage(formData).subscribe({
      next: () => {
        this.responseMessage = 'Car registered successfully!';
        this.carForm.reset({ available: true });
        this.selectedFile = null;
        this.selectedFileName = '';
        this.submitted = false;
        this.loading = false;
        this.router.navigate(['/cars/all']); // Adjust route as needed
      },
      error: (err) => {
        this.responseMessage = 'Car registration failed. Please try again.';
        console.error(err);
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
      const file = event.dataTransfer.files[0];
      this.selectedFile = file;
      this.selectedFileName = file.name;

      this.carForm.patchValue({ image: file });
      this.carForm.get('image')?.updateValueAndValidity();
    }
  }
}
