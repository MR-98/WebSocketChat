import { Injectable } from '@angular/core';

declare global {
  interface Window {
    __env: {
      restUrl: string;
      websocketUrl: string;
      production: boolean;
    };
  }
}

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  get restUrl(): string {
    return window.__env?.restUrl;
  }

  get websocketUrl(): string {
    return window.__env?.websocketUrl;
  }

  get isProduction(): boolean {
    return window.__env?.production;
  }
}
