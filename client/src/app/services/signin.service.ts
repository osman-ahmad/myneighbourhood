import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UploadResult } from '../model/upload-result';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class SigninService {
  imageData = "";

  constructor(private httpClient: HttpClient) { }

  upload(form: any, image: File) {
    const formData = new FormData();
    formData.set("username", form['name']);
    formData.set("location", form['location']);
    formData.set("email", form['email']);
    formData.set("password", form['password']);
    // formData.set("profilePic", form['profilePic']);
    formData.set("profilePic", image, image.name); // Set the image file with its name

    const headers = new HttpHeaders();
    headers.set('Content-Type', image.type); // Set the Content-Type dynamically based on the image file

    return firstValueFrom(this.httpClient.post<UploadResult>("/signin", formData, { headers }));
  }
}