import { Component, HostListener, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { filter } from 'rxjs/operators';
import { Offcanvas } from 'bootstrap';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent {
  @ViewChild('offcanvasNavbar') offcanvasNavbar!: ElementRef;

  userRole: string | null = null;
  username: string | null = null;
  isLoggedIn: boolean = false;
  isScrolled: boolean = false;
  isHomePage: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.updateNavbarState();
  }

  ngOnInit(): void {
    this.updateUserState();

    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.isHomePage = event.urlAfterRedirects === '/';
        this.updateNavbarState();
        this.updateUserState(); // Refresh user state on route change
      });
  }

  updateUserState(): void {
    this.userRole = this.authService.getUserRole();
    this.username =
      localStorage.getItem('username') || sessionStorage.getItem('username');
    this.isLoggedIn = this.authService.isLoggedIn();
  }

  updateNavbarState(): void {
    const scrollY = window.scrollY || 0;
    this.isScrolled = this.isHomePage && scrollY > 80;
  }

  logout(): void {
    this.authService.logout();
    window.location.href = '/login';
  }

  navigateTo(route: string): void {
  this.router.navigate([route]).then(() => {
    const offcanvasElement = this.offcanvasNavbar?.nativeElement;
    if (offcanvasElement) {
      const bsOffcanvas = Offcanvas.getInstance(offcanvasElement) || new Offcanvas(offcanvasElement);
      bsOffcanvas.hide();

      // Manually remove the backdrop if it remains
      const backdrop = document.querySelector('.offcanvas-backdrop');
      if (backdrop) {
        backdrop.remove();
        document.body.classList.remove('offcanvas-backdrop');
        document.body.style.overflow = ''; // Restore scroll
      }
    }
  });
}

}
