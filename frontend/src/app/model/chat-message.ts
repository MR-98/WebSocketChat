export interface ChatMessage {
  data: string
  room: object
  sender: string
  timestamp: number
  id: number | null
}
