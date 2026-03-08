import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Attachment } from "../model/attachment";
import { DataStoreService } from "./data-store.service";
import { GetDownloadUrlResponse } from "../model/get-download-url-response";
import { Observable } from "rxjs";
import { ConfigService } from "./config.service";

@Injectable({
  providedIn: 'root'
})
export class AttachmentsService {

  private readonly url: string = '';

  constructor(
    private http: HttpClient,
    private dataStoreService: DataStoreService,
    private configService: ConfigService,
  ) {
    this.url = `${this.configService.restUrl}/attachments`;
  }

  uploadAttachments(files: File[], chatRoomId: number) {
    let currentUserProfile = this.dataStoreService.getUserProfile()!!
    const formData = new FormData();
    for (const file of files) {
      formData.append("attachments", file);
    }
    formData.append("chatRoomId", String(chatRoomId));
    formData.append("uploaderUsername", currentUserProfile.username);
    formData.append("uploadTimestamp", String(Date.now()));

    return this.http.post<Attachment[]>(
      this.url,
      formData
    )
  }

  downloadFile(attachmentId: number) {
    this.http
      .get<GetDownloadUrlResponse>(`${this.url}/download-url/${attachmentId}`)
      .subscribe((res: GetDownloadUrlResponse) => {
        window.location.href = res.downloadURL;
      });
  }

  deleteFile(attachmentId: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${attachmentId}`)
  }
}
