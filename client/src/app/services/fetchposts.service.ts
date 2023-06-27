import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UploadResult } from '../model/upload-result';
import { lastValueFrom } from 'rxjs';
import { PostResults } from '../model/post-results';

@Injectable({
  providedIn: 'root'
})
export class FetchpostsService {

  constructor(private httpClient: HttpClient) { }

  async getPosts(): Promise<PostResults[]> {
    const result: PostResults[] = await lastValueFrom(this.httpClient.get<PostResults[]>('/get-posts'));
    return result;
  }

  async getLatNLng(location: string) {
    const result:  PostResults = await  lastValueFrom(this.httpClient.get<PostResults>('/getLatNLng/' + location));
    const { lat , lng} = result;
    return { lat, lng };
  }

  
}
