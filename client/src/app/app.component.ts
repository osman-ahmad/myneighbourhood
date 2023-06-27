import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'client';
  showSignOutLink: boolean = false;

  constructor(private router: Router) {}

  isLoggedinPage(): boolean {
    return this.router.url.includes('/loggedin/');
  }

  isMainPage(): boolean {
    return this.router.url  === '/' ;
  }


  
}
