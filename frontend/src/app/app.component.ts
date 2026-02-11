import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { DataStoreService } from "./service/data-store.service";
import {UserService} from "./service/user.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  constructor(private userService: UserService, private dataStoreService: DataStoreService) {
    this.userService.getMe().subscribe(user => {
      this.dataStoreService.setUserProfile(
        {
          username: user.username!!,
          firstName: user.firstName!!,
          lastName: user.lastName!!,
          fullName: `${user.firstName!!} ${user.lastName!!}`
        }
      )
    })
  }

}
