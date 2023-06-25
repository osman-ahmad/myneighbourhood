import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loginError = false;

  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit() {
    this.buildLoginForm();
  }

  buildLoginForm() {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  async onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    const email = this.loginForm.controls['email'].value;
    const password = this.loginForm.controls['password'].value;

    try {
      const loginResult = await this.loginService.getInfo(email, password);
      // Process the login result here
      console.log(loginResult);
      // Redirect to the loggedin page with email and password parameters
      this.router.navigate(['/loggedin', loginResult.userId]);
    } catch (error) {
      // Handle any errors that occur during login
      console.error('Login error:', error);
      if (error) {
        this.loginError = true;
      }
    }

    // Reset the form
    this.loginForm.reset();
  }
}
