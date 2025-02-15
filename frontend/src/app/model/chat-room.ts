import { User } from "./user";

export interface ChatRoom {
  name: string
  users?: User[],
  id: number
}
