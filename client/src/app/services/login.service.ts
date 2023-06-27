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
    const { userId } = result;
    return { userId };
  }

  async getInfoByUserId(userId: string) {
    const result: UploadResult = await lastValueFrom(
      this.httpClient.get<UploadResult>('/get-details-by-userid/' + userId)
    );
    const { profilePicUrl, name, location, email, lat, lng } = result;
    return { userId, profilePicUrl, name, location, email, lat , lng };
  }
}
