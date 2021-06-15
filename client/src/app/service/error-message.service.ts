import { Injectable } from '@angular/core';

interface MessageDict {
  [key: number]: string;
}

@Injectable({
  providedIn: 'root',
})
export class ErrorMessageService {
  messages: MessageDict = {
    403: 'その動作は許可されていません',
    404: '存在しないURLへはアクセスできません',
    500: '予期せぬエラーが発生しました',
  };

  constructor() {}

  getErrorMessage(statusCode: number): string {
    if (statusCode in this.messages) {
      return this.messages[statusCode];
    } else {
      return this.messages[500];
    }
  }
}
