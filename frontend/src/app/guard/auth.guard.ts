import {
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  CanActivate, Router
} from '@angular/router';
import { Injectable } from "@angular/core";
import {AuthService} from "../service/auth.service";

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    if (this.authService.isAuthenticated()) {
      return true;
    }

    this.router.navigate(['/login']);
    return false;
  }


}
