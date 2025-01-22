import { Component, inject } from '@angular/core';
import { FormsModule } from "@angular/forms";
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

@Component({
  selector: 'app-yes-no-dialog',
  standalone: true,
  imports: [
    FormsModule,
    MatButton,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle,
    MatFormField,
    MatInput,
    MatLabel
  ],
  templateUrl: './yes-no-dialog.component.html',
  styleUrl: './yes-no-dialog.component.scss'
})
export class YesNoDialogComponent {
  protected titleText = '';
  protected contentText = '';
  protected yesButtonText = '';
  protected noButtonText = '';

  protected data = inject(MAT_DIALOG_DATA);

  constructor(public dialogRef: MatDialogRef<YesNoDialogComponent>) {
    this.titleText = this.data["titleText"] ? this.data["titleText"] : '';
    this.contentText = this.data["contentText"] ? this.data["contentText"] : '';
    this.yesButtonText = this.data["yesButtonText"] ? this.data["yesButtonText"] : 'Tak';
    this.noButtonText = this.data["noButtonText"] ? this.data["noButtonText"] : 'Nie';
  }

  onNo() {
    this.dialogRef.close(false);
  }

  onYes() {
    this.dialogRef.close(true);
  }
}
