import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private dataSource = new BehaviorSubject<string>("");
  currentData = this.dataSource.asObservable();

  constructor() { }

  updateData(message: string) {
    this.dataSource.next(message)
  }
}
