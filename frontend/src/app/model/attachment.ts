export interface Attachment {
  fileName: string,
  type: AttachmentType,
  url: string,
  size?: number,
  contentType?: string
  id: number
}

export enum AttachmentType {
  IMAGE = "IMAGE",
  FILE = "FILE",
}
