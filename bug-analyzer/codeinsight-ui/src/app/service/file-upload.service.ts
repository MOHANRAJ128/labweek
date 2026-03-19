import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  // Empty baseUrl → requests are relative to the current host.
  // In Docker, nginx proxies /ai/* /api/* /logs/* to the backend container.
  // For local dev (ng serve), change this to 'http://localhost:8080'.
  baseUrl = '';

  constructor(private http: HttpClient) { }

  debug(projectFile: File, logFile: File) {
    const message = `Project File: ${projectFile.name}, Error Log: ${logFile.name}`;
    console.log(message);
    const formData = new FormData();
    formData.append('projectName', projectFile.name);
    formData.append('projectFile', projectFile);
    formData.append('logFile', logFile);
    return this.http.post<any>(`${this.baseUrl}/ai/debug`, formData);
  }
}
