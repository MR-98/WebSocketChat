import { User } from "./user";
import { ChatRoom } from "./chat-room";

export interface Invitation {
  invitedUser: User,
  room: ChatRoom,
  id?: number
}
