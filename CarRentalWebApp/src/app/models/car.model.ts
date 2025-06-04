export class Car {
  id?: number;
  brand!: string;
  model!: string;
  available!: boolean;
  pricePerDay!: number;
  imageUrl?: string;       // Optional URL to the image
  imageData?: Blob;        // Optional raw image data as a Blob
}
