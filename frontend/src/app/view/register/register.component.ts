import { Component } from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import { AuthService } from "../../service/auth.service";
import { Router, RouterLink } from "@angular/router";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    NgClass,
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  protected submitted: boolean = false;
  protected registerForm = new FormGroup({
    username: new FormControl('', Validators.required),
    firstName: new FormControl('', Validators.required),
    lastName: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    repeatPassword: new FormControl('', Validators.required)
  })

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
  }

  onSubmit() {
    this.submitted = true;
    if (this.registerForm.invalid) {
      return;
    }
    let username = this.registerForm.controls.username.value!!;
    let firstName = this.registerForm.controls.firstName.value!!;
    let lastName = this.registerForm.controls.lastName.value!!;
    let password = this.registerForm.controls.password.value!!;
    let repeatPassword = this.registerForm.controls.repeatPassword.value!!;
    if(password != repeatPassword) return;
    this.authService.register(username, firstName, lastName, password).subscribe(_ => {
      this.router.navigate(['/login']);
    })
  }

  get f(): any { return this.registerForm.controls; }
}
