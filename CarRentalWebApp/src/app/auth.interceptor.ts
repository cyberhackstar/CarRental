import { Injectable, Injector } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CustomToastComponent } from './features/custom-toast/custom-toast.component';


@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private injector: Injector) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');

    if (req.url.includes('/login') || req.url.includes('/register')) {
      return next.handle(req).pipe(
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
    }

    let authReq = req;
    if (token) {
      authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => this.handleError(error))
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    
console.log('Interceptor caught error:', error);

    if (error.status === 401) {
      localStorage.removeItem('token');

      const router = this.injector.get(Router);
      const toastr = this.injector.get(ToastrService);

      toastr.show(
  'Your session has expired. Please log in again.',
  'Session Timeout',
  {
    toastComponent: CustomToastComponent,
    positionClass: 'toast-center',
    timeOut: 0, // prevent auto-dismiss
    closeButton: false
  }
);


      router.navigate(['/login']);
    }

    return throwError(() => error);
  }
}
