import { User } from "./user";

export interface ChatMessage {
  data: string
  room: object
  sender: User
  timestamp: number
  id?: number | null
}
