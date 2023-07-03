import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { LoginService } from '../services/login.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UploadService } from '../services/upload.service';
import { MatTabsModule } from '@angular/material/tabs';
import { FetchpostsService } from '../services/fetchposts.service';
import { MatSnackBar } from '@angular/material/snack-bar';

interface Post {
  imageUrl: string;
  title: string;
  category: string;
  location: string;
  postDate: Date;
  userId: string;
  description: string;
  lat: number;
  lng: number;
  distance: string;
  postId: string;
}

@Component({
  selector: 'app-loggedin',
  templateUrl: './loggedin.component.html',
  styleUrls: ['./loggedin.component.css']
})
export class LoggedinComponent implements OnInit, OnDestroy {
  imageData = '';
  form!: FormGroup;
  param$!: Subscription;
  profilePicUrl = "";
  name = "";
  location = "";
  email = "";
  password = "";
  userId = "";
  activeTab: number = 0;
  showDistance = true;
  currentPage = 1;
  totalPages = 0;
  posts: Post[] = [];
  userPosts: Post[] = [];
  neighbourPosts: Post[] = [];
  latResult = "";
  lngResult = "";
  selectedCategory = '';
  pageSize = 5;
  postId: string = '';

  constructor(
    private actRoute: ActivatedRoute,
    private loginSvc: LoginService,
    private uploadSvc: UploadService,
    private formBuilder: FormBuilder,
    private fetchpostsSvc: FetchpostsService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.createForm();
    this.fetchPosts();
    this.param$ = this.actRoute.params.subscribe(async (params) => {
      this.userId = params['userId'];
      let r = await this.loginSvc.getInfoByUserId(this.userId);
      console.log(r);
      this.profilePicUrl = r.profilePicUrl;
      this.name = r.name;
      this.location = r.location;
      this.email = r.email;
      this.latResult = r.lat;
      this.lngResult = r.lng;

      
      });
  }

  ngOnDestroy(): void {
    this.param$.unsubscribe();
  }

  async fetchPosts(): Promise<void> {
    try {
      const data = await this.fetchpostsSvc.getPosts();
      this.posts = data;
  
      this.posts.forEach((post) => this.calculateDistance(post));
      this.posts.sort((a, b) => {
        const distanceA = parseFloat(a.distance);
        const distanceB = parseFloat(b.distance);
        return distanceA - distanceB;
      });
  
      this.userPost();
      this.neighbourPost();
  
      

      
    } catch (error) {
      console.error('Login error:', error);
    }
  }

  userPost(): void {
    const userIdInt = parseInt(this.userId, 10); 

    this.userPosts = this.posts.filter((post) => {
      const postUserIdInt = parseInt(post.userId, 10);
      if (userIdInt && postUserIdInt !== userIdInt) {
        return false;
      }
      return true;
    });

    console.log(this.userPosts);
    this.totalPages = Math.ceil(this.userPosts.length / this.pageSize);
    this.currentPage = 1;

  }

  neighbourPost(): void {
    const userIdInt = parseInt(this.userId, 10); 
  
    this.neighbourPosts = this.posts.filter((post) => {
      const postUserIdInt = parseInt(post.userId, 10);
      if (userIdInt && postUserIdInt !== userIdInt) {
        return true;
      }
      return false;
    });
  
    
  
    this.totalPages = Math.ceil(this.neighbourPosts.length / this.pageSize);
    this.currentPage = 1;
    this.showDistance = true;
  }
  

  handleFileInput(event: any): void {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = (e) => {
      const imageData = e.target?.result as string;
      this.imageData = imageData;
    };

    reader.readAsDataURL(file);
  }

  upload(): void {
    if (this.form.valid) {
      const formValue = this.form.value;
      const file = this.dataURItoFile(this.imageData, 'image');
      const userId = this.userId;

      this.uploadSvc
        .upload(formValue, file, userId)
        .then((result) => {
          this.router.navigate(['/loggedin/', userId]);
          this.form.reset();
          this.imageData = '';
          location.reload();
        })
        .catch((error) => console.log(error));
    }
  }

  private createForm(): void {
    this.form = this.formBuilder.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      category: ['', Validators.required]
    });
  }

  private dataURItoFile(dataURI: string, fileName: string): File {
    const byteString = atob(dataURI.split(',')[1]);
    const mimeString = dataURI
      .split(',')[0]
      .split(';')[0]
      .split(':')[1]
      .split('/')[1];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    const file = new File([ab], fileName, { type: `image/${mimeString}` });
    return file;
  }

  calculateDistance(post: Post): void {
    const userLatitude = parseFloat(this.latResult); 
    const userLongitude = parseFloat(this.lngResult);
    console.log(userLatitude);
    console.log(userLongitude);
    const postLatitude = post.lat;
    const postLongitude = post.lng;
    console.log(postLatitude);
    console.log(postLongitude);  
    const earthRadius = 6371; 
    const userLatRad = this.degreesToRadians(userLatitude);
    const userLngRad = this.degreesToRadians(userLongitude);
    const postLatRad = this.degreesToRadians(postLatitude);
    const postLngRad = this.degreesToRadians(postLongitude);

    const latDiff = postLatRad - userLatRad;
    const lngDiff = postLngRad - userLngRad;

    const a =
      Math.sin(latDiff / 2) ** 2 +
      Math.cos(userLatRad) * Math.cos(postLatRad) * Math.sin(lngDiff / 2) ** 2;
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const distance = earthRadius * c;

    post.distance = +(distance * 1000).toFixed(0) + 'm';
    console.log(post.distance);
  }

  private degreesToRadians(degrees: number): number {
    return (degrees * Math.PI) / 180;
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  deletePost(postId: string): void {
    const confirmDelete = confirm('Are you sure you want to delete this post?');
    console.log(postId);
    if (!confirmDelete) {
      return; 
    }

    const index = this.userPosts.findIndex(post => post.postId === postId);
        if (index > -1) {
        this.userPosts.splice(index, 1);
      }
        

      this.uploadSvc.deletePost(postId)
      .subscribe(
        () => {
          
          console.log('Post deleted successfully');
          this.snackBar.open('Post deleted successfully.', 'Close', {
            duration: 3000
          });
        },
        error => {
          
          console.error('Error deleting post:', error);
        }
      );
      
  }
}

    

