import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contact',
  imports: [CommonModule, FormsModule],
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.css'
})
export class ContactComponent {
contact = {
  name: '',
  email: '',
  subject: '',
  message: ''
};

contactSuccess = false;
contactError = false;

submitContactForm() {
  // Simulate form submission
  if (this.contact.name && this.contact.email && this.contact.message) {
    this.contactSuccess = true;
    this.contactError = false;
    // Reset form or send to backend
  } else {
    this.contactError = true;
    this.contactSuccess = false;
  }
}

}
