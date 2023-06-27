import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UploadResult } from '../model/upload-result';
import { Observable, firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  imageData = "";

  constructor(private httpClient: HttpClient) { }

  upload(form: any, image: File, userId: string) {
    const formData = new FormData();
    formData.set("title", form['title']);
    formData.set("description", form['description']);
    formData.set("category", form['category']);

    formData.set("userId", userId);

    formData.set("image", image, image.name);


    const headers = new HttpHeaders();
    headers.set('Content-Type', image.type); 
    return firstValueFrom(this.httpClient.post<UploadResult>("/post-upload", formData, { headers }));
  }

  deletePost(postId: string): Observable<void> {
    const headers = new HttpHeaders();
    headers.set('Content-Type', 'application/json');

    return this.httpClient.delete<void>(`/delete-post/${postId}`, { headers });
  }
 
}
