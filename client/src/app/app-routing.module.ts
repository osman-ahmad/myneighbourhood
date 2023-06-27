import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SigninComponent } from './components/signin.component';
import { LoggedinComponent } from './components/loggedin.component';
import { MainComponent } from './components/main.component';
import { LoginComponent } from './components/login.component';

const routes: Routes = [
  { path: "", component: MainComponent},
  { path: "signin", component: SigninComponent},
  { path: "login", component: LoginComponent},
  { path: 'loggedin/:userId', component: LoggedinComponent},
  { path: "**", redirectTo: "/", pathMatch: "full"}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
