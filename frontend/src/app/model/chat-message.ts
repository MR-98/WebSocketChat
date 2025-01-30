import { User } from "./user";
import { ChatRoom } from "./chat-room";

export interface ChatMessage {
  data: string
  room: ChatRoom
  sender: User
  timestamp: number
  id?: number
}
