import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { LoginService } from '../services/login.service';

@Component({
  selector: 'app-loggedin',
  templateUrl: './loggedin.component.html',
  styleUrls: ['./loggedin.component.css']
})
export class LoggedinComponent implements OnInit, OnDestroy {
  param$!: Subscription;
  imageUrl = "";
  name = "";
  location = "";
  email = "";
  password = "";
  userId: number | undefined;
  

  constructor(
    private actRoute: ActivatedRoute,
    private loginSvc: LoginService
  ) {}

  ngOnInit(): void {
    this.param$ = this.actRoute.params.subscribe(async (params) => {
      this.userId = +params['userId'];
      let r = await this.loginSvc.getInfoByUserId(this.userId);
      console.log(r);
      this.imageUrl = r.imageUrl;
      this.name = r.name;
      this.location = r.location;
      this.email = r.email;
      this.userId = r.userId;
    });
  }

  ngOnDestroy(): void {
    this.param$.unsubscribe();
  }
}
