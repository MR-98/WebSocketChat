import { Component, inject } from '@angular/core';
import { MatButton } from "@angular/material/button";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent, MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import { MatFormField, MatLabel } from "@angular/material/form-field";
import { MatInput } from "@angular/material/input";
import { FormControl, FormsModule, ReactiveFormsModule } from "@angular/forms";
import {
  MatAutocomplete,
  MatAutocompleteSelectedEvent,
  MatAutocompleteTrigger,
  MatOption
} from "@angular/material/autocomplete";
import { AsyncPipe, NgForOf } from "@angular/common";
import { User } from "../../model/user";
import { debounceTime, distinctUntilChanged, Observable, of, startWith, switchMap } from "rxjs";
import { UserService } from "../../service/user.service";

@Component({
  selector: 'app-invite-user-dialog',
  standalone: true,
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    FormsModule,
    MatAutocomplete,
    MatOption,
    MatAutocompleteTrigger,
    NgForOf,
    AsyncPipe
  ],
  templateUrl: './invite-user-dialog.component.html',
  styleUrl: './invite-user-dialog.component.scss'
})
export class InviteUserDialogComponent {
  protected myControl = new FormControl<string | User>('');
  protected input = '';
  protected selectedUsername: User | undefined = undefined;
  protected options: Observable<User[]> = of(
    [
      {
        username: "testowy",
        firstName: "JAN",
        lastName: "TESTOWY"
      }
    ]
  )

  constructor(
    public dialogRef: MatDialogRef<InviteUserDialogComponent>,
    private userService: UserService

  ) {
    this.options = this.myControl.valueChanges.pipe(
      startWith(''),
      debounceTime(400),
      distinctUntilChanged(),
      switchMap(val => {
        if(typeof val == "string" && val.length >= 3) {
          return this.userService.getUsers(val)
        }
        return of()
      })
    )
  }

  onApply() {
    this.dialogRef.close(this.selectedUsername);
  }

  onCancel() {
    this.dialogRef.close(undefined);
  }

  onOptionSelected($event: MatAutocompleteSelectedEvent) {
    this.selectedUsername = ($event.option.value as User);
  }

  displayFn(user: User): string {
    return user && user.firstName && user.lastName ? `${user.firstName} ${user.lastName}` : '';
  }
}
