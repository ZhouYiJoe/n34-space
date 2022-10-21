import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {
  constructor() { }

  //创建或修改键值对
  set(key: string, value: any): void {
    localStorage.setItem(key, JSON.stringify(value))
  }

  //获取键对应的值
  get(key: string): any {
    let item: any = localStorage.getItem(key)
    return item === null ? null : JSON.stringify(item)
  }

  //删除键值对
  remove(key: string): void {
    localStorage.removeItem(key)
  }
}
