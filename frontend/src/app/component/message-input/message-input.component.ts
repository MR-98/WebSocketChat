import {Component, effect, OnDestroy} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {NgForOf, NgIf} from "@angular/common";
import {ImagePreview} from "../../model/image-preview";
import {AttachmentsService} from "../../service/attachments.service";
import {WebSocketService} from "../../service/web-socket.service";
import {ChatRoom} from "../../model/chat-room";
import {DataStoreService} from "../../service/data-store.service";

@Component({
  selector: 'app-message-input',
  standalone: true,
  imports: [
    FormsModule,
    MatIcon,
    MatIconButton,
    NgForOf,
    NgIf
  ],
  templateUrl: './message-input.component.html',
  styleUrl: './message-input.component.scss'
})
export class MessageInputComponent implements OnDestroy {

  protected currentChatRoom: ChatRoom | undefined;
  protected message: string = '';
  protected uploadedFileIds: number[] = [];
  protected uploadedFiles: ImagePreview[] = [];
  protected uploadingFilesInProgress: boolean = false;

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
    if((this.message == "" || this.message == " ") && this.uploadedFileIds.length == 0) return;
    this.websocketService.sendMessage(this.currentChatRoom!!, this.message, this.uploadedFileIds);
    this.message = '';
    this.uploadedFileIds = [];
    this.uploadedFiles = [];
  }

  onFilesUpload($event: Event) {
    const input = $event.target as HTMLInputElement;
    if (input.files) {
      this.uploadingFilesInProgress = true;
      this.uploadedFiles.push(...Array.from(input.files).map(file => {
        return {
          file: file,
          url: URL.createObjectURL(file)
        }
      }));
      this.attachmentsService.uploadAttachments(
        Array.from(input.files),
        this.currentChatRoom!!.id
      ).subscribe(attachments => {
        this.uploadedFileIds.push(...attachments.map(attachment => attachment.id));
        this.uploadingFilesInProgress = false;
      });
    }
    input.value = '';
  }

  removeImage(index: number): void {
    const image = this.uploadedFiles[index];

    URL.revokeObjectURL(image.url);
    this.uploadedFiles.splice(index, 1);
  }

  ngOnDestroy(): void {
    this.uploadedFiles.forEach(img => URL.revokeObjectURL(img.url));
  }

}
