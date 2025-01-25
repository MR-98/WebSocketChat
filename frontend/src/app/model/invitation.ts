import { User } from "./user";
import { ChatRoom } from "./chat-room";

export interface Invitation {
  invitedUser: User,
  invitingUser: User,
  room: ChatRoom,
  id?: number
}
