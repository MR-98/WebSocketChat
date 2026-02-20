import { Component, Input } from '@angular/core';
import { NgIf } from "@angular/common";
import { Attachment } from "../../model/attachment";
import { AttachmentsService } from "../../service/attachments.service";
import { MatIcon } from "@angular/material/icon";

@Component({
  selector: 'app-attachment',
  standalone: true,
  imports: [
    NgIf,
    MatIcon
  ],
  templateUrl: './attachment.component.html',
  styleUrl: './attachment.component.scss'
})
export class AttachmentComponent {
  @Input() attachment!: Attachment;

  constructor(private attachmentsService: AttachmentsService) {
  }

  downloadFile(id: number) {
    this.attachmentsService.downloadFile(id);
  }
}
