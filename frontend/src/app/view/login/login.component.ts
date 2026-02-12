import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { AuthService } from "../../service/auth.service";
import { Router, RouterLink } from "@angular/router";
import { NgClass } from "@angular/common";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterLink,
    NgClass
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  protected submitted: boolean = false;
  protected loginForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  });

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
  }

  onSubmit() {
    this.submitted = true;
    if (this.loginForm.invalid) {
      return;
    }
    let username = this.loginForm.controls.username.value!!;
    let password = this.loginForm.controls.password.value!!;
    this.authService.login(username, password).subscribe(_ => {
      this.router.navigate(['/']);
    })
  }

  get f(): any { return this.loginForm.controls; }
}
