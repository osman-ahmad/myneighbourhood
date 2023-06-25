import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs/internal/lastValueFrom';
import { UploadResult } from '../model/upload-result';
import { from } from 'rxjs';
import { map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class FileuploadService {

  constructor(private httpClient: HttpClient) { }

  // getImage(postId: string){
  //   //by right  must use httpparams
  //   return lastValueFrom(this.httpClient.get<UploadResult>('/get-image/' + postId));
  // }
  async getImage(postId: string) {
    const result: UploadResult = await lastValueFrom(this.httpClient.get<UploadResult>('/get-image/' + postId));
    const { imageUrl, title, complain } = result;
    return { imageUrl, title, complain };
  }
}
