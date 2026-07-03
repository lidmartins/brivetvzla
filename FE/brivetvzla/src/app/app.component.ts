import { Component, inject, signal, ViewChild } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet, Router } from '@angular/router';
import { ReportModalComponent } from './features/public/modals/report-modal/report-modal.component';
import { AuthService } from './core/auth/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, ReportModalComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  private router     = inject(Router);
  private authService = inject(AuthService);

  isHome = signal(false);
  isLoggedIn = this.authService.isLoggedIn;

  constructor() {
    this.router.events.subscribe(() => {
      this.isHome.set(this.router.url === '/' || this.router.url === '/home');
    });
  }

  @ViewChild('reportModal') reportModal!: ReportModalComponent;

  openReportModal(type: 'P' | 'E' = 'P') {
    this.reportModal.openReportModal(type);
  }
}
