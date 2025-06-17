import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CarouselModule } from 'primeng/carousel'; // ✅ PrimeNG Carousel

@Component({
  selector: 'app-testimonial-section',
  standalone: true,
  imports: [CommonModule, CarouselModule],
  templateUrl: './testimonial-section.component.html',
  styleUrls: ['./testimonial-section.component.css']
})
export class TestimonialSectionComponent {
  testimonials = [
    {
      image: 'assets/images/tm1.webp',
      name: 'Saloni Rathi',
      role: 'Business Traveler',
      feedback: 'The booking process was seamless and the car was in excellent condition.'
    },
    {
      image: 'assets/images/tm2.webp',
      name: 'Sachin Thakur',
      role: 'Vacationer',
      feedback: 'We rented a car for our family trip and it was perfect. Clean, spacious, and very affordable.'
    },
    {
      image: 'assets/images/tm3.webp',
      name: 'Rohit Kumar',
      role: 'Frequent Flyer',
      feedback: 'Airport pickup was on time and the driver was courteous. Great experience every time I travel.'
    },
    {
      image: 'assets/images/tm4.webp',
      name: 'Meghana Konka',
      role: 'Event Organizer',
      feedback: 'Used their luxury fleet for a wedding event. The cars were stunning and service was top-notch.'
    }
  ];

  numVisible: number = 3;

@HostListener('window:resize', ['$event'])
onResize(event: any) {
  const width = event.target.innerWidth;
  if (width >= 1200) {
    this.numVisible = 3;
  } else if (width >= 768) {
    this.numVisible = 2;
  } else {
    this.numVisible = 1;
  }
}

ngOnInit() {
  this.onResize({ target: window }); // Set initial value
}
}
