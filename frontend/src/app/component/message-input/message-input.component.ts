import { Component, effect, OnDestroy } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { MatIcon } from "@angular/material/icon";
import { MatIconButton } from "@angular/material/button";
import { NgForOf, NgIf } from "@angular/common";
import { AttachmentsService } from "../../service/attachments.service";
import { WebSocketService } from "../../service/web-socket.service";
import { ChatRoom } from "../../model/chat-room";
import { DataStoreService } from "../../service/data-store.service";
import { Attachment, AttachmentType } from "../../model/attachment";
import { MatProgressSpinner } from "@angular/material/progress-spinner";
import { MatTooltip } from "@angular/material/tooltip";
import { HttpErrorResponse } from "@angular/common/http";
import { ErrorCode } from "../../model/error-message";

@Component({
  selector: 'app-message-input',
  standalone: true,
  imports: [
    FormsModule,
    MatIcon,
    MatIconButton,
    NgForOf,
    NgIf,
    MatProgressSpinner,
    MatTooltip
  ],
  templateUrl: './message-input.component.html',
  styleUrl: './message-input.component.scss'
})
export class MessageInputComponent implements OnDestroy {

  protected currentChatRoom: ChatRoom | undefined;
  protected message: string = '';
  protected uploadedFiles: Attachment[] = [];
  protected uploadingFilesInProgress: boolean = false;
  protected readonly AttachmentType = AttachmentType;

  private MAX_NUMBER_OF_ATTACHMENTS = 5;
  private MAX_ATTACHMENT_SIZE_IN_MB = 5;

  constructor(
    private websocketService: WebSocketService,
    private dataStoreService: DataStoreService,
    private attachmentsService: AttachmentsService,
  ) {
    effect(() => {
      if (!this.websocketService.isConnected()) return

      this.currentChatRoom = this.dataStoreService.getCurrentlySelectedChatRoom();
    })
  }

  protected sendMessage() {
    if ((this.message == "" || this.message == " ") && this.uploadedFiles.length == 0) return;
    let attachmentIds = this.uploadedFiles.map(file => file.id);
    this.websocketService.sendMessage(this.currentChatRoom!!, this.message, attachmentIds);
    this.message = '';
    this.uploadedFiles = [];
  }

  onFilesUpload($event: Event) {
    const input = $event.target as HTMLInputElement;
    if (!input.files) return;
    if (this.uploadedFiles.length + input.files.length > this.MAX_NUMBER_OF_ATTACHMENTS) {
      // TODO: snack bar
      alert("Błąd! Maksymalna liczba załączników: " + this.MAX_NUMBER_OF_ATTACHMENTS);
      return;
    }
    this.uploadingFilesInProgress = true;
    this.attachmentsService.uploadAttachments(
      Array.from(input.files),
      this.currentChatRoom!!.id
    ).subscribe({
      next: (attachments: Attachment[]) => {
        this.uploadedFiles.push(...attachments);
        this.uploadingFilesInProgress = false;
      },
      error: (response: HttpErrorResponse) => {
        // TODO: snack bar
        if(response.status == 413 || response.error.errorCode == ErrorCode.ATTACHMENT_TOO_BIG) {
          alert("Błąd! Załącznik zbyt duży. Maksymalny rozmiar załącznika to " + this.MAX_ATTACHMENT_SIZE_IN_MB + "MB.");
        } else if (response.error.errorCode == ErrorCode.UNSUPPORTED_ATTACHMENT_EXTENSION) {
          alert("Błąd! Nieznane rozszerzenie załącznika.");
        }
        this.uploadingFilesInProgress = false;
      }
    });
    input.value = '';
  }

  removeImage(index: number): void {
    let removedFile = this.uploadedFiles.splice(index, 1).pop()!!;
    this.attachmentsService.deleteFile(removedFile.id).subscribe();
  }

  ngOnDestroy(): void {

  }
}
