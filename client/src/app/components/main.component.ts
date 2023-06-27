import { Component } from '@angular/core';
import { FetchpostsService } from '../services/fetchposts.service';
import { Router } from '@angular/router';

interface Post {
  imageUrl: string;
  title: string;
  category: string;
  location: string;
  postDate: String;
  description: string;
  lat: number;
  lng: number;
  distance: string;
}

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {
  title = 'client';
  searchAddress = '';
  selectedCategory = '';
  showDistance = false;
  currentPage = 1;
  totalPages = 0;
  pageSize = 5;
  posts: Post[] = [];
  filteredPosts: Post[] = [];
  latResult: number = 0;
  lngResult: number = 0;

  constructor(
    private router: Router,
    private fetchpostsSvc: FetchpostsService
    ) {}

  ngOnInit() {
    this.fetchPosts();
  }

  async fetchPosts() {
    try {
      const data = await this.fetchpostsSvc.getPosts();
      this.posts = data;
      this.posts.forEach((post) => this.calculateDistance(post));
      this.filterPosts();
    } catch (error) {
      
    }
  }

  filterPosts() {
    this.filteredPosts = this.posts.filter((post) => {
      if (this.selectedCategory && post.category !== this.selectedCategory) {
        return false;
      }
      console.log(this.filteredPosts);
     
      return true;
    });

    this.totalPages = Math.ceil(this.filteredPosts.length / this.pageSize);
    this.currentPage = 1;
    this.showDistance = false;
  }
  
  async searchPosts() {
    this.filterPosts();
    this.showDistance = !!this.searchAddress;

    try {
    const locationResult = await this.fetchpostsSvc.getLatNLng(this.searchAddress);
    this.latResult = locationResult.lat;
    this.lngResult = locationResult.lng;

    this.filteredPosts.forEach((post) => this.calculateDistance(post));
    
    console.log(locationResult);
  } catch (error) {
    
    console.error('Login error:', error);
    if (error) {
      
    }
  }
  this.searchAddress = '';
  }

  calculateDistance(post: Post) {
    const userLatitude = this.latResult; 
    const userLongitude = this.lngResult; 
  
    const postLatitude = post.lat;
    const postLongitude = post.lng;
  
    const earthRadius = 6371; 
  
    const userLatRad = this.degreesToRadians(userLatitude);
    const userLngRad = this.degreesToRadians(userLongitude);
    const postLatRad = this.degreesToRadians(postLatitude);
    const postLngRad = this.degreesToRadians(postLongitude);
  
    const latDiff = postLatRad - userLatRad;
    const lngDiff = postLngRad - userLngRad;

    const a =
      Math.sin(latDiff / 2) ** 2 +
      Math.cos(userLatRad) *
        Math.cos(postLatRad) *
        Math.sin(lngDiff / 2) ** 2;
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const distance = earthRadius * c;
  
    if (distance < 1) {
    post.distance = +(distance * 1000).toFixed(0) + 'm';
  } else {
    post.distance = +(distance).toFixed(2) + 'km';
  }
  const postDate = new Date(post.postDate.toString());
  const formattedDate = `${postDate.getDate()}-${postDate.getMonth() + 1}-${postDate.getFullYear()}`;
  post.postDate = formattedDate;
}

  private degreesToRadians(degrees: number) {
    return (degrees * Math.PI) / 180;
  }
  


  

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }

  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }
}
