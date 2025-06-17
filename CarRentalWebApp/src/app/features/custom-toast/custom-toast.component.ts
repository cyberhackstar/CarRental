import { Component } from '@angular/core';
import { ToastPackage, ToastrService } from 'ngx-toastr';

@Component({
  standalone: true,
  selector: 'app-custom-toast',
  templateUrl: './custom-toast.component.html',
  styleUrls: ['./custom-toast.component.css']
})
export class CustomToastComponent {
  constructor(
    private toastrService: ToastrService,
    public toastPackage: ToastPackage
  ) {}

  close() {
  console.log('Closing toast with ID:', this.toastPackage.toastId);
  this.toastrService.remove(this.toastPackage.toastId);
}

}
