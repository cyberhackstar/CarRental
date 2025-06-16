import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-totalcar',
  imports: [CommonModule,FormsModule],
  templateUrl: './totalcar.component.html',
  styleUrl: './totalcar.component.css',
})
export class TotalcarComponent {
  stats = [
    {
      icon: 'pi pi-calendar',
      color: 'text-warning',
      label: 'Years of Experience',
      value: 60,
      start: 0,
    },
    {
      icon: 'pi pi-car',
      color: 'text-primary',
      label: 'Fleet',
      value: 1090,
      start: 0,
    },
    {
      icon: 'pi pi-users',
      color: 'text-success',
      label: 'Happy Customers',
      value: 2590,
      start: 0,
    },
    {
      icon: 'pi pi-map-marker',
      color: 'text-danger',
      label: 'Branches Nationwide',
      value: 67,
      start: 0,
    },
  ];

  ngAfterViewInit(): void {
    this.animateCounters();
  }

  animateCounters(): void {
    const counters = document.querySelectorAll('.counter');
    counters.forEach((counter) => {
      const target = +counter.getAttribute('data-target')!;
      let count = 0;
      const increment = target / 100;

      const updateCount = () => {
        count += increment;
        if (count < target) {
          counter.textContent = Math.ceil(count).toString();
          requestAnimationFrame(updateCount);
        } else {
          counter.textContent = target.toString();
        }
      };

      updateCount();
    });
  }
}
