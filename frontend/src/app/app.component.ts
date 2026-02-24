import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { DataStoreService } from "./service/data-store.service";
import { UserService } from "./service/user.service";
import { AuthService } from "./service/auth.service";
import { NgIf } from "@angular/common";
import { LoadingComponent } from "./view/loading/loading.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgIf, LoadingComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  protected applicationLoaded = false;

  constructor(
    private userService: UserService,
    private dataStoreService: DataStoreService,
    private authService: AuthService,
  ) {

    this.authService.isAuthenticated$.subscribe(isAuthenticated => {
      if (isAuthenticated) {
        this.userService.getMe().subscribe(user => {
          this.dataStoreService.setUserProfile(
            {
              username: user.username!!,
              firstName: user.firstName!!,
              lastName: user.lastName!!,
              fullName: `${user.firstName!!} ${user.lastName!!}`
            }
          );
          this.applicationLoaded = true;
        })
      } else {
        this.applicationLoaded = true;
      }
    })
  }
}
