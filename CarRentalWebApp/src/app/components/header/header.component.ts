import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent {
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
}
