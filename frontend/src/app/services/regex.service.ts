import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RegexService {
  public usernameRegex: RegExp = new RegExp('^\\w{1,64}$')
  public passwordRegex: RegExp = new RegExp('^\\w{6,16}$')
  public nicknameRegex: RegExp = new RegExp('^\\S{1,64}$')
  public emailRegex: RegExp = new RegExp('^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$')

  constructor() {
  }

  checkUsername(str: string): boolean {
    return this.usernameRegex.test(str)
  }

  checkPassword(str: string): boolean {
    return this.passwordRegex.test(str)
  }

  checkNickname(str: string): boolean {
    return this.nicknameRegex.test(str)
  }

  checkEmail(str: string): boolean {
    return this.emailRegex.test(str)
  }
}
