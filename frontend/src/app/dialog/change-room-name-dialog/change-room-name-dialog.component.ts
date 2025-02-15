import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose, MatDialogContainer,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import { MatButton } from "@angular/material/button";
import { MatFormField, MatLabel } from "@angular/material/form-field";
import { MatInput } from "@angular/material/input";
import { FormsModule } from "@angular/forms";

@Component({
  selector: 'app-change-room-name-dialog',
  standalone: true,
  imports: [
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    MatButton,
    MatDialogTitle,
    MatFormField,
    MatInput,
    FormsModule,
    MatLabel,
    MatDialogContainer
  ],
  templateUrl: './change-room-name-dialog.component.html',
  styleUrl: './change-room-name-dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ChangeRoomNameDialogComponent {

  protected chatRoomNameValue: string = '';
  protected data = inject(MAT_DIALOG_DATA);

  constructor(public dialogRef: MatDialogRef<ChangeRoomNameDialogComponent>) {
    this.chatRoomNameValue = this.data["currentRoomName"] ? this.data["currentRoomName"] : '';
  }

  onApply() {
    this.dialogRef.close(this.chatRoomNameValue);
  }

  onCancel() {
    this.dialogRef.close(undefined);
  }
}
