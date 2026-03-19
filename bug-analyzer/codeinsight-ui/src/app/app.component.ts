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
  readonly projectFileError = signal<string>('');
  readonly logFileError = signal<string>('');
  readonly debuggingResponse = signal<DebuggingResponse | null>(null);
  readonly isLoading = signal<boolean>(false);
  readonly canSubmit = computed(() =>
    !!this.file1() &&
    !!this.file2() &&
    !this.projectFileError() &&
    !this.logFileError() &&
    !this.isLoading()
  );

  constructor(private fileUploadService: FileUploadService) {}

  onFile1Selected(event: Event): void {
    const target = event.target as HTMLInputElement;
    const files = target.files;
    if (files && files.length > 0) {
      const selectedFile = files[0];
      if (this.isZipFile(selectedFile)) {
        this.file1.set(selectedFile);
        this.projectFileError.set('');
      } else {
        this.file1.set(null);
        this.projectFileError.set('Project file must be a .zip file.');
        target.value = '';
      }
    }
  }

  onFile2Selected(event: Event): void {
    const target = event.target as HTMLInputElement;
    const files = target.files;
    if (files && files.length > 0) {
      const selectedFile = files[0];
      if (this.isTxtFile(selectedFile)) {
        this.file2.set(selectedFile);
        this.logFileError.set('');
      } else {
        this.file2.set(null);
        this.logFileError.set('Error log file must be a .txt file.');
        target.value = '';
      }
    }
  }

  private isZipFile(file: File): boolean {
    return file.name.toLowerCase().endsWith('.zip');
  }

  private isTxtFile(file: File): boolean {
    return file.name.toLowerCase().endsWith('.txt');
  }

  onSubmit(): void {
    const file1 = this.file1();
    const file2 = this.file2();

    if (!file1) {
      this.projectFileError.set('Please select a .zip project file.');
    }

    if (!file2) {
      this.logFileError.set('Please select a .txt error log file.');
    }

    if (file1 && file2 && !this.projectFileError() && !this.logFileError()) {
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
