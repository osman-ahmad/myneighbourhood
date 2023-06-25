import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SigninService } from '../services/signin.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit, OnDestroy{

  // width = 400;
  // height = 400;
  

  constructor(private router: Router, private signinsvc: SigninService) { }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }



}

