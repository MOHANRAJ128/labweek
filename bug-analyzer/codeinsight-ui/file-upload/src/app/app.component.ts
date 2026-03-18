import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'file-upload';
  file1: File | null = null;
  file2: File | null = null;

  onFile1Selected(event: Event): void {
    const target = event.target as HTMLInputElement;
    const files = target.files;
    if (files && files.length > 0) {
      this.file1 = files[0];
    }
  }

  onFile2Selected(event: Event): void {
    const target = event.target as HTMLInputElement;
    const files = target.files;
    if (files && files.length > 0) {
      this.file2 = files[0];
    }
  }

  onSubmit(): void {
    if (this.file1 && this.file2) {
      console.log('File 1:', this.file1.name);
      console.log('File 2:', this.file2.name);
      alert(`Uploaded:\n1. ${this.file1.name}\n2. ${this.file2.name}`);
    } else {
      alert('Please select both files');
    }
  }
}
