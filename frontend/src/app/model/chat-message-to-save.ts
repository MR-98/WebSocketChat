export interface ChatMessageToSave {
  data: string
  roomId: number
  senderUsername: string
  timestamp: number,
  attachmentIds: number[]
}
