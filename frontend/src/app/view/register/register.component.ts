import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from "@angular/forms";
import { AuthService } from "../../service/auth.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  protected registerForm = new FormGroup({
    username: new FormControl(''),
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    password: new FormControl(''),
    repeatPassword: new FormControl('')
  })

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
  }

  onSubmit() {
    let username = this.registerForm.controls.username.value!!;
    let firstName = this.registerForm.controls.firstName.value!!;
    let lastName = this.registerForm.controls.lastName.value!!;
    let password = this.registerForm.controls.password.value!!;
    let repeatPassword = this.registerForm.controls.repeatPassword.value!!;
    if(password != repeatPassword) return;
    this.authService.register(username, firstName, lastName, password).subscribe(_ => {
      this.router.navigate(['/']);
    })
  }
}
