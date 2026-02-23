export interface ErrorMessage {
  errorCode: ErrorCode;
}

export enum ErrorCode {
  ATTACHMENT_TOO_BIG = 'ATTACHMENT_TOO_BIG',
  UNSUPPORTED_ATTACHMENT_EXTENSION = 'UNSUPPORTED_ATTACHMENT_EXTENSION',
}
