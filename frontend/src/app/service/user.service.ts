import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { User } from "../model/user";
import { ConfigService } from "./config.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly url: string;

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
  ) {
    this.url = `${this.configService.restUrl}/users`;
  }

  getMe(): Observable<User> {
    return this.http.get<User>(
      this.url + "/me"
    )
  }

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
