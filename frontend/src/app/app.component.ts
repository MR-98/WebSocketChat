import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { KeycloakService } from "keycloak-angular";
import { KeycloakProfile } from "keycloak-js";
import { DataStoreService } from "./service/data-store.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  constructor(
    private keycloakService: KeycloakService,
    private dataStoreService: DataStoreService
  ) {
    this.keycloakService.loadUserProfile().then((userProfile: KeycloakProfile) => {
      this.dataStoreService.setUserProfile(
        {
          username: userProfile.username!!,
          firstName: userProfile.firstName!!,
          lastName: userProfile.lastName!!
        }
      )
    })
  }

}
