import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { UploadResult } from '../model/upload-result';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  constructor(private httpClient: HttpClient) { }

  async getInfo(emailParam: string, password: string) {
    const result: UploadResult = await lastValueFrom(this.httpClient.get<UploadResult>('/get-details/' + emailParam + '/' + password));
    const { userId, imageUrl, name, location, email } = result;
    return { userId, imageUrl, name, location, email };
  }

  async getInfoByUserId(userId: number) {
    const result: UploadResult = await lastValueFrom(
      this.httpClient.get<UploadResult>('/get-details-by-userid/' + userId)
    );
    const { imageUrl, name, location, email } = result;
    return { userId, imageUrl, name, location, email };
  }
}
