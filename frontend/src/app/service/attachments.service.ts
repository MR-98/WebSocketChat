import { Injectable } from '@angular/core';
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Attachment } from "../model/attachment";
import { DataStoreService } from "./data-store.service";

@Injectable({
  providedIn: 'root'
})
export class AttachmentsService {

  private url: string = `${environment.backendUrl}/attachments`;

  constructor(
    private http: HttpClient,
    private dataStoreService: DataStoreService,
  ) { }

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
}
