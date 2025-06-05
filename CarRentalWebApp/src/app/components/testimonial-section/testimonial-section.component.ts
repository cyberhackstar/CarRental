import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CarouselModule } from 'ngx-owl-carousel-o';

@Component({
  selector: 'app-testimonial-section',
  imports: [CommonModule,CarouselModule],
  templateUrl: './testimonial-section.component.html',
  styleUrl: './testimonial-section.component.css',
})
export class TestimonialSectionComponent {
  testimonials = [
  {
    image: 'https://via.placeholder.com/100',
    text: 'Far far away, behind the word mountains...',
    name: 'Roger Scott',
    position: 'Marketing Manager'
  },
  {
    image: 'https://via.placeholder.com/100',
    text: 'Far far away, behind the word mountains...',
    name: 'Roger Scott',
    position: 'Interface Designer'
  },
  {
    image: 'https://via.placeholder.com/100',
    text: 'Far far away, behind the word mountains...',
    name: 'Roger Scott',
    position: 'UI Designer'
  }
];

  customOptions = {
    loop: true,
    margin: 30,
    nav: false,
    dots: true,
    autoplay: true,
    autoplayTimeout: 3000,
    responsive: {
      0: { items: 1 },
      768: { items: 2 },
      992: { items: 3 },
    },
  };
}
