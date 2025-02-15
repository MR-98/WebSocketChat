import {
  AfterViewInit,
  Component,
  effect,
  ElementRef,
  EventEmitter,
  OnDestroy,
  Output,
  ViewChild
} from '@angular/core';
import { ChatMessage } from "../../model/chat-message";
import { MessageComponent } from "../message/message.component";
import { NgClass, NgForOf, NgIf } from "@angular/common";
import { WebSocketService } from "../../service/web-socket.service";
import { DataStoreService } from "../../service/data-store.service";
import { StompSubscription } from "@stomp/stompjs";
import { FormsModule } from "@angular/forms";
import { ChatRoom } from "../../model/chat-room";
import { ChatRoomSettingsComponent } from "../chat-room-settings/chat-room-settings.component";
import { MatIcon } from "@angular/material/icon";
import { MatIconButton } from "@angular/material/button";
import { debounceTime, fromEvent } from "rxjs";
import { ChatService } from "../../service/chat.service";
import { MatProgressSpinner } from "@angular/material/progress-spinner";

@Component({
  selector: 'app-chat-room',
  standalone: true,
  imports: [
    MessageComponent,
    NgForOf,
    FormsModule,
    NgIf,
    NgClass,
    ChatRoomSettingsComponent,
    MatIcon,
    MatIconButton,
    MatProgressSpinner
  ],
  templateUrl: './chat-room.component.html',
  styleUrl: './chat-room.component.scss'
})
export class ChatRoomComponent implements AfterViewInit, OnDestroy {

  protected currentChatRoom: ChatRoom | undefined;
  private currentSubscription: StompSubscription | undefined;
  protected chatMessages: ChatMessage[] = []
  protected message: string = '';
  protected chatRoomName: string = '';
  protected roomSettingVisible: boolean = false;
  protected oldMessagesLoading: boolean = false;
  protected noMoreOldMessages: boolean = false;

  @ViewChild('messagesContainer', { static: true }) messagesContainer!: ElementRef;
  @Output() chatRoomClosed = new EventEmitter<null>();

  constructor(
    private websocketService: WebSocketService,
    private dataStoreService: DataStoreService,
    private chatService: ChatService
  ) {
    effect(() => {
      if (!this.websocketService.isConnected()) return

      this.currentChatRoom = this.dataStoreService.getCurrentlySelectedChatRoom();
      this.reloadCurrentChatRoom();
    })
  }

  ngAfterViewInit() {
    this.initLazyLoadingOfOldMessages();
  }

  private reloadCurrentChatRoom() {
    if (this.currentSubscription != undefined) {
      this.currentSubscription.unsubscribe();
    }
    this.chatMessages = [];
    this.oldMessagesLoading = false;
    this.noMoreOldMessages = false;
    if (this.currentChatRoom != undefined) {
      this.chatRoomName = this.currentChatRoom.name;
      this.subscribeToMessagesTopic();
    }
  }

  private subscribeToMessagesTopic() {
    this.currentSubscription = this.websocketService.subscribeToRoom(
      // @ts-ignore
      this.currentChatRoom.id,
      (message: ChatMessage[] | ChatMessage) => {
        if (Array.isArray(message)) {
          this.chatMessages.unshift(...message);
        } else {
          this.chatMessages.unshift(message);
        }
      }
    )
  }

  protected sendMessage() {
    if(this.message == "" || this.message == " ") return;
    this.websocketService.sendMessage(this.currentChatRoom!!, this.message);
    this.message = '';
  }

  protected toggleRoomSettings() {
    this.roomSettingVisible = !this.roomSettingVisible;
  }

  protected updateChatRoomName(newName: string) {
    this.chatRoomName = newName;
  }

  private initLazyLoadingOfOldMessages() {
    fromEvent(this.messagesContainer?.nativeElement, 'scroll')
      .pipe(debounceTime(100))
      .subscribe((e: any) => {
      if (this.shouldLoadOldMessages(e)) {
        this.callApiToGetOldMessages();
      }
    });
  }

  private shouldLoadOldMessages(scrollEvent: any): boolean {
    let scrollTop = scrollEvent?.target['scrollTop'];
    let scrollHeight = scrollEvent?.target['scrollHeight'];
    let offsetHeight = scrollEvent?.target['offsetHeight'];
    const LOAD_OLD_MESSAGES_SCROLL_TRIGGER_OFFSET = 5;
    return !this.oldMessagesLoading &&
      !this.noMoreOldMessages &&
      (scrollHeight + scrollTop - offsetHeight) < LOAD_OLD_MESSAGES_SCROLL_TRIGGER_OFFSET &&
      this.chatMessages.length >= 50
  }

  private callApiToGetOldMessages() {
    this.oldMessagesLoading = true;
    this.chatService.loadOldMessagesForRoom(
      this.currentChatRoom!!.id,
      this.chatMessages.at(-1)!!.id!!
    ).subscribe({
      next: (messages: ChatMessage[]) => {
        if (messages.length == 0) {
          this.noMoreOldMessages = true;
        }
        this.chatMessages.push(...messages);
        this.oldMessagesLoading = false;
      },
      error: (error) => {
        this.oldMessagesLoading = false;
      }
    });
  }

  closeRoom() {
    this.chatRoomClosed.emit();
  }

  ngOnDestroy() {
    if (this.currentSubscription != undefined) {
      this.currentSubscription.unsubscribe();
    }
    this.chatMessages = [];
    this.oldMessagesLoading = false;
    this.noMoreOldMessages = false;
    this.currentChatRoom = undefined;
  }
}
