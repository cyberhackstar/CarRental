export class Booking {
  id?: number;
  carId!: number;
  customerName!: string;
  startDate!: string; // ISO format date string
  endDate!: string;
  totalAmount!: number;
}
