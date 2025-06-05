import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-services-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './services-section.component.html',
  styleUrls: ['./services-section.component.css']
})
export class ServicesSectionComponent {
  services = [
    {
      icon: 'bi-car-front-fill',
      title: 'Wedding Ceremony',
      description: 'A small river named Duden flows by their place and supplies it with the necessary regelialia.'
    },
    {
      icon: 'bi-geo-alt-fill',
      title: 'City Transfer',
      description: 'A small river named Duden flows by their place and supplies it with the necessary regelialia.'
    },
    {
      icon: 'bi-airplane-engines',
      title: 'Airport Transfer',
      description: 'A small river named Duden flows by their place and supplies it with the necessary regelialia.'
    },
    {
      icon: 'bi-map-fill',
      title: 'Whole City Tour',
      description: 'A small river named Duden flows by their place and supplies it with the necessary regelialia.'
    }
  ];
}
