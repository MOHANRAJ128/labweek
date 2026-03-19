import { Component, computed, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FileUploadService } from './service/file-upload.service';
import { DebuggingResultsComponent } from './components/debugging-results/debugging-results.component';

export interface DebuggingResponse {
  projectName: string;
  debuggingResult: string;
}

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, FormsModule, DebuggingResultsComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'file-upload';
  readonly file1 = signal<File | null>(null);
  readonly file2 = signal<File | null>(null);
  readonly debuggingResponse = signal<DebuggingResponse | null>(null);
  readonly isLoading = signal<boolean>(false);
  readonly canSubmit = computed(() => !!this.file1() && !!this.file2() && !this.isLoading());

  constructor(private fileUploadService: FileUploadService) {}

  onFile1Selected(event: Event): void {
    const target = event.target as HTMLInputElement;
    const files = target.files;
    if (files && files.length > 0) {
      this.file1.set(files[0]);
    }
  }

  onFile2Selected(event: Event): void {
    const target = event.target as HTMLInputElement;
    const files = target.files;
    if (files && files.length > 0) {
      this.file2.set(files[0]);
    }
  }

  onSubmit(): void {
    const file1 = this.file1();
    const file2 = this.file2();

    if (file1 && file2) {
      this.isLoading.set(true);
      this.debuggingResponse.set(null);
      console.log('File 1:', file1.name);
      console.log('File 2:', file2.name);
      this.fileUploadService.debug(file1, file2).subscribe(
        (response: any) => {
          console.log('Server response:', response);
          this.debuggingResponse.set(response);
          this.isLoading.set(false);
        },
        (error: any) => {
          console.error('Upload error:', error);
          this.isLoading.set(false);
        }
      );
    } else {
      console.error('Both files must be selected before submitting.');
    }
  }
}
