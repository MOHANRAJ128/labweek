import { Component, Input, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { marked } from 'marked';

export interface DebuggingResponse {
  projectName: string;
  debuggingResult: string;
}

interface DebuggingSection {
  title: string;
  content: string;
}

@Component({
  selector: 'app-debugging-results',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './debugging-results.component.html',
  styleUrls: ['./debugging-results.component.scss']
})
export class DebuggingResultsComponent {
  private readonly _response = signal<DebuggingResponse | null>(null);

  @Input() set response(value: DebuggingResponse | null) {
    this._response.set(value);
  }

  constructor(private sanitizer: DomSanitizer) {}

readonly formattedResult = computed<SafeHtml>(() => {
  const response = this._response();
  if (!response) return '';

  const raw = response.debuggingResult;

  const html = marked.parse(raw) as string;

  return this.sanitizer.bypassSecurityTrustHtml(html);
});
}
