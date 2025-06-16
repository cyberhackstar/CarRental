import { Component, HostListener } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { HeaderComponent } from './features/header/header.component';

import { FooterComponent } from './features/footer/footer.component';

import { filter } from 'rxjs';
import { CommonModule } from '@angular/common';
import { LoadingComponent } from "./features/loading/loading.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FooterComponent, CommonModule, RouterOutlet, HeaderComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'CarRentalWebApp';

  isScrolled = false;

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.isScrolled = window.scrollY > 50;
  }

  isHomePage = false;

  constructor(private router: Router) {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.isHomePage = event.urlAfterRedirects === '/';
      });
  }

  loading = true;

  ngOnInit(): void {
    // Simulate initial loading delay (e.g., 2 seconds)
    setTimeout(() => {
      this.loading = false;
    }, 2000);
  }
}
