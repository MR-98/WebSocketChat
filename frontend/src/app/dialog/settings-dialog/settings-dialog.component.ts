import { Component } from '@angular/core';
import { MatButton } from "@angular/material/button";
import { MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle } from "@angular/material/dialog";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { LocalStorageService } from "../../service/local-storage.service";
import { MatSlideToggle } from "@angular/material/slide-toggle";

@Component({
  selector: 'app-settings-dialog',
  standalone: true,
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle,
    ReactiveFormsModule,
    MatSlideToggle,
    FormsModule
  ],
  templateUrl: './settings-dialog.component.html',
  styleUrl: './settings-dialog.component.scss'
})
export class SettingsDialogComponent {

  protected invitationsEnabled: boolean | undefined;
  protected locale: string = "pl-PL";
  protected darkTheme: boolean | undefined;

  constructor(private localStorageService: LocalStorageService) {
    this.invitationsEnabled = localStorageService.getData("invitationsEnabled") != null ?
      JSON.parse(localStorageService.getData("invitationsEnabled")!!) : true;
    this.locale = localStorageService.getData("locale") != null ?
      localStorageService.getData("locale")!! : "pl-PL";
    this.darkTheme = localStorageService.getData("darkTheme") != null ?
      JSON.parse(localStorageService.getData("darkTheme")!!) : true;
  }

  onInvitationsSettingsChange() {
    this.localStorageService.saveData("invitationsEnabled", this.invitationsEnabled!!.toString());
  }

  onThemeSettingsChange() {
    this.localStorageService.saveData("darkTheme", this.darkTheme!!.toString());
  }
}
