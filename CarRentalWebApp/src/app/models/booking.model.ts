export class Booking {
  id?: number;
  carId!: number;
  customerName!: string;
  customerEmail!: string;
  startDate!: string; // ISO format date string
  endDate!: string;
  paymentStatus?: string;
  totalAmount!: number;
}
