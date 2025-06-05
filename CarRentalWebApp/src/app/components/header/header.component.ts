import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Dialog } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PrimeIcons, MenuItem } from 'primeng/api';
import { MenubarModule } from 'primeng/menubar';
import {
  trigger,
  state,
  style,
  animate,
  transition,
} from '@angular/animations';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule, Dialog, ButtonModule, InputTextModule, MenubarModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],

  animations: [
    trigger('fadeSlide', [
      state('void', style({ opacity: 0, transform: 'translateY(-10px)' })),
      transition(':enter', [
        animate(
          '300ms ease-out',
          style({ opacity: 1, transform: 'translateY(0)' })
        ),
      ]),
      transition(':leave', [
        animate(
          '200ms ease-in',
          style({ opacity: 0, transform: 'translateY(-10px)' })
        ),
      ]),
    ]),
  ],
})

export class HeaderComponent {
  userRole: string | null = null;
  username: string | null = null;
  isLoggedIn: boolean = false;
  isScrolled: boolean = false;

  visible: boolean = false;

  items: MenuItem[] = [];

  

    showDialog() {
        this.visible = true;
    }

    closeDialog() {
        this.visible = false;
    }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.isScrolled = window.scrollY > 50;
  }

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.userRole = this.authService.getUserRole();
    this.username =
      localStorage.getItem('username') || sessionStorage.getItem('username');
    this.isLoggedIn = this.authService.isLoggedIn();

    this.items = [
            {
                label: 'New',
                icon: PrimeIcons.PLUS,
            },
            {
                label: 'Delete',
                icon: PrimeIcons.TRASH
            }
        ];
  }

  logout(): void {
    this.authService.logout();
    window.location.href = '/login';
  }
}
