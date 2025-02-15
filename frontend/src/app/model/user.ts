import { ChatRoom } from "./chat-room";

export interface User {
  username: string,
  firstName?: string,
  lastName?: string,
  rooms?: ChatRoom[]
}
