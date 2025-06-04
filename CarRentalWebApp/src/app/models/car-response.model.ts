// car-response.model.ts
export interface CarResponse {
  id: number;
  brand: string;
  model: string;
  available: boolean;
  pricePerDay: number;
  imageUrl: string; // This will be a full URL like /api/cars/image/filename.jpg
}
