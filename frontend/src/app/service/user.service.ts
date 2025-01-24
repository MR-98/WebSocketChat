import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { User } from "../model/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private url: string = 'http://host.docker.internal:8080/users';

  constructor(
    private http: HttpClient
  ) { }

  getUsers(query: string): Observable<User[]> {
    return this.http.get<User[]>(
      this.url + "/search",
      {
        params: {
          query: query
        }
      }
    )
  }
}
