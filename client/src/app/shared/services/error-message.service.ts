import { Injectable } from '@angular/core';

interface MessageDict {
  [key: number]: string;
}

@Injectable({
  providedIn: 'root',
})
export class ErrorMessageService {
  messages: MessageDict = {
    401: 'ユーザが認証されていません',
    403: 'その動作は許可されていません',
    404: '存在しないURLへはアクセスできません',
    500: '予期せぬエラーが発生しました',
    1000: '予期せぬエラーが発生しました',
    1101: '存在しないユーザです',
    1102: '存在しないロールです',
    1103: '存在しない予約です',
    1104: 'APIが見つかりません',
    1200: 'メールアドレスは既に登録済みです',
    1201: 'その時刻は既に予約済みです',
    1300: 'その動作は許可されていません',
    1400: '無効なリクエストです',
    1401: 'その予約は登録できません',
    1402: '予約時間が長すぎます',
    1403: 'パスワードが短すぎます',
    1500: 'ユーザはログインしていません',
    1501: 'パスワードが間違っています',
    1502: '無効なアクセストークンです',
    1503: '期限切れのアクセストークンです',
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
