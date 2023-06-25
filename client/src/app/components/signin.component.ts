import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SigninService } from '../services/signin.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {
  imageData = '';
  form!: FormGroup;

  constructor(private router: Router, private formBuilder: FormBuilder, private signinSvc: SigninService) {}

  ngOnInit(): void {
    this.createForm();
  }

  handleFileInput(event: any) {
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

      this.signinSvc.upload(formValue, file)
        .then((result) => {
          this.router.navigate(['/']);
        })
        .catch((error) => console.log(error));
    }
  }

  private createForm(): void {
    this.form = this.formBuilder.group({
      name: ['', Validators.required],
      location: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
     
    });
  }

  private dataURItoFile(dataURI: string, fileName: string): File {
    const byteString = atob(dataURI.split(',')[1]);
    const mimeString = dataURI.split(',')[0].split(';')[0].split(':')[1].split('/')[1];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    const file = new File([ab], fileName, { type: `image/${mimeString}` });
    return file;
  }
}
