import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { User } from '../models/user.model';

export interface LoginResponse {
  token: string;
  username: string;
  userRole: 'USER' | 'ADMIN' | 'SUPER_ADMIN';
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = 'https://carrentalservice.help/carservice/api';

  constructor(private http: HttpClient) {}

  login(user: User, rememberMe: boolean = false): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, user);
  }

  googleLogin(idToken: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/google-login`, {
      idToken,
    });
  }

  // Google Signup
  googleSignUp(idToken: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/google-signup`, {
      idToken,
    });
  }

  register(user: User): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/register`, user);
  }

  logout(): void {
    localStorage.clear();
    sessionStorage.clear();
  }

  getToken(): string | null {
    return localStorage.getItem('token') || sessionStorage.getItem('token');
  }

  getUserRole(): string | null {
    return (
      localStorage.getItem('userRole') || sessionStorage.getItem('userRole')
    );
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
