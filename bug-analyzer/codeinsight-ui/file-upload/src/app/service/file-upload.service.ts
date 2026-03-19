import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  baseUrl = 'http://localhost:8080';

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
