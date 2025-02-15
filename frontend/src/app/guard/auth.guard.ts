import {
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  CanActivate
} from '@angular/router';
import { Injectable } from "@angular/core";
import { KeycloakService } from "keycloak-angular";

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {

  constructor(private keycloakService: KeycloakService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    return this.keycloakService.isLoggedIn();
  }


}
