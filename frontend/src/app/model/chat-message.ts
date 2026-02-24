import { User } from "./user";
import { ChatRoom } from "./chat-room";
import { Attachment } from "./attachment";

export interface ChatMessage {
  data: string
  room: ChatRoom
  sender: User
  timestamp: number,
  attachments: Attachment[],
  id: number
}
