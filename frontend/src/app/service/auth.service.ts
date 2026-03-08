import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { BehaviorSubject, Observable, tap } from "rxjs";
import { LoginResponse } from "../model/login-response";
import { ConfigService } from "./config.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly url: string;
  private readonly TOKEN_KEY = 'jwt_token';
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router,
    private configService: ConfigService,
  ) {
    this.url = this.configService.restUrl;
  }

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.url + '/auth/login', { username, password })
      .pipe(
        tap(response => {
          this.setToken(response.token);
          this.isAuthenticatedSubject.next(true);
        })
      );
  }

  register(username: string, firstName: string, lastName: string, password: string) {
    return this.http.post(this.url + '/auth/register', { username, firstName, lastName, password })
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
    this.isAuthenticatedSubject.next(true);
  }

  isTokenValid(): boolean {
    const token = this.getToken();

    if (!token) {
      return false;
    }

    const decoded = this.decodeToken(token);

    if (!decoded || !decoded.exp) {
      return false;
    }

    const expirationDate = decoded.exp * 1000;
    const now = Date.now();

    if (now >= expirationDate) {
      return false;
    }

    return true;
  }

  isAuthenticated(): boolean {
    return this.isTokenValid();
  }

  private hasToken(): boolean {
    return !!this.getToken();
  }

  private decodeToken(token: string): DecodedToken | null {
    try {
      const payload = token.split('.')[1];
      const decoded = atob(payload);
      return JSON.parse(decoded);
    } catch (error) {
      return null;
    }
  }
}

interface DecodedToken {
  exp: number;
  [key: string]: any;
}
