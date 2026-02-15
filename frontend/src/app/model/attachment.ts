export interface Attachment {
  fileName: string,
  type: 'IMAGE' | 'FILE',
  url: string,
  size?: number,
  contentType?: string
  id: number
}
