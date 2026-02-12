import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { AuthService } from "../../service/auth.service";
import { Router, RouterLink } from "@angular/router";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  protected loginForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  })

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
  }

  onSubmit() {
    let username = this.loginForm.controls.username.value!!;
    let password = this.loginForm.controls.password.value!!;
    this.authService.login(username, password).subscribe(_ => {
      this.router.navigate(['/']);
    })
  }
}
