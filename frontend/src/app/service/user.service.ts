import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { User } from "../model/user";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private url: string = `${environment.backendUrl}/users`;

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
