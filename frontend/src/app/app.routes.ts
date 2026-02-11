import { Routes } from '@angular/router';
import { AuthGuard } from "./guard/auth.guard";
import { MainComponent } from "./view/main/main.component";
import { LoginComponent } from "./view/login/login.component";
import { RegisterComponent } from "./view/register/register.component";

export const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  }
];
