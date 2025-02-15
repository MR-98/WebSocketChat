import { Routes } from '@angular/router';
import { AuthGuard } from "./guard/auth.guard";
import { MainComponent } from "./view/main/main.component";

export const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    canActivate: [AuthGuard]
  }
];
