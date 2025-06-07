import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ViewChild, ElementRef } from '@angular/core';

@Component({
  selector: 'app-loading',
  imports: [CommonModule],
  templateUrl: './loading.component.html',
  styleUrl: './loading.component.css'
})
export class LoadingComponent {
   @ViewChild('spinnerVideo') spinnerVideo!: ElementRef<HTMLVideoElement>;

  isLoading=true;

  ngAfterViewInit() {
    const video = this.spinnerVideo.nativeElement;
    video.play().catch((err) => {
      console.error('Autoplay failed:', err);
    });
  }

}
