import { Component, inject, signal, ViewChild } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet, Router } from '@angular/router';
import { ReportModalComponent } from './features/public/modals/report-modal/report-modal.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, ReportModalComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  private router     = inject(Router);

  isHome = signal(false);

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
