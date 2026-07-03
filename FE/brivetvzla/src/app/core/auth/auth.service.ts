import { Injectable, signal, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap, map } from 'rxjs/operators';
import { Observable, merge, fromEvent, Subscription } from 'rxjs';
import { throttleTime } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'vpv_token';
  private readonly USER_KEY = 'vpv_user';
  private readonly LAST_ACTIVITY_KEY = 'vpv_last_activity';
  private _isLoggedIn = signal(!!localStorage.getItem(this.TOKEN_KEY));
  readonly isLoggedIn = this._isLoggedIn.asReadonly();

  private currentUser = signal<any>(this.getUserFromStorage());
  readonly user = this.currentUser.asReadonly();

  private timeoutSeconds = 30 * 60; // 30 minutes
  private timer: any;
  private activitySubscription?: Subscription;

  constructor(
    private http: HttpClient,
    private router: Router,
    private ngZone: NgZone
  ) {
    if (this._isLoggedIn()) {
      const lastActivity = localStorage.getItem(this.LAST_ACTIVITY_KEY);
      if (lastActivity) {
        const elapsedMs = Date.now() - parseInt(lastActivity, 10);
        if (elapsedMs >= this.timeoutSeconds * 1000) {
          this.logoutDueToInactivity();
          return;
        }
      }
      this.startActivityMonitor();
    }
  }

  private getUserFromStorage(): any {
    const u = localStorage.getItem(this.USER_KEY);
    if (!u) return null;
    try {
      return JSON.parse(u);
    } catch {
      return null;
    }
  }

  login(email: string, password: string): Observable<void> {
    return this.http
      .post<{ token: string, usuario: any }>(`${environment.apiUrl}/auth/login`, { email, password })
      .pipe(
        tap(res => {
          localStorage.setItem(this.TOKEN_KEY, res.token);
          localStorage.setItem(this.USER_KEY, JSON.stringify(res.usuario));
          this._isLoggedIn.set(true);
          this.currentUser.set(res.usuario);
          this.startActivityMonitor();
        }),
        map(() => void 0)
      );
  }

  logout(): void {
    this.stopActivityMonitor();
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    localStorage.removeItem(this.LAST_ACTIVITY_KEY);
    this._isLoggedIn.set(false);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  private startActivityMonitor() {
    this.stopActivityMonitor();
    this.resetTimer();

    this.ngZone.runOutsideAngular(() => {
      const activity$ = merge(
        fromEvent(window, 'mousemove'),
        fromEvent(window, 'click'),
        fromEvent(window, 'keypress'),
        fromEvent(window, 'scroll'),
        fromEvent(window, 'touchstart')
      ).pipe(
        throttleTime(2000)
      );

      this.activitySubscription = activity$.subscribe(() => {
        this.ngZone.run(() => {
          this.resetTimer();
        });
      });
    });
  }

  private stopActivityMonitor() {
    if (this.timer) {
      clearTimeout(this.timer);
      this.timer = null;
    }
    if (this.activitySubscription) {
      this.activitySubscription.unsubscribe();
      this.activitySubscription = undefined;
    }
  }

  private resetTimer() {
    localStorage.setItem(this.LAST_ACTIVITY_KEY, Date.now().toString());

    if (this.timer) {
      clearTimeout(this.timer);
    }

    this.timer = setTimeout(() => {
      this.logoutDueToInactivity();
    }, this.timeoutSeconds * 1000);
  }

  private logoutDueToInactivity() {
    this.stopActivityMonitor();
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    localStorage.removeItem(this.LAST_ACTIVITY_KEY);
    this._isLoggedIn.set(false);
    this.currentUser.set(null);
    this.router.navigate(['/']);
  }
}
