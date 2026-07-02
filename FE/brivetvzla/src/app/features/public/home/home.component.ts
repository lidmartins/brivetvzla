import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ReportModalComponent } from '../modals/report-modal/report-modal.component';
import { SolicitudService } from '../../../core/services/solicitud.service';

@Component({
  selector: 'app-home',
  imports: [RouterLink, ReportModalComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  private solicitudService = inject(SolicitudService);
  recentReports = signal<any[]>([]);

  features = [
    { icon: '🔴', bg: '#FEF2F2', color: '#DC4A4A', title: 'Mascotas Perdidas', desc: 'Reporta y busca mascotas perdidas durante emergencias. Red activa en todo el país.', link: '/perdidas', linkLabel: 'Ver reportes', go: '#DC4A4A' },
    { icon: '🟢', bg: '#F0FDF4', color: '#2F9E63', title: 'Mascotas Encontradas', desc: 'Encontraste un animal y quieres ayudarlo a regresar a casa.', link: '/encontradas', linkLabel: 'Ver reportes', go: '#2F9E63' },
    { icon: '🏠', bg: '#F0F7F5', color: '#0F766E', title: 'Refugios Temporales', desc: 'Red de refugios coordinados por veterinarios con capacidad en tiempo real.', link: '/refugios', linkLabel: 'Ver refugios', go: '#0F766E' },
  ];

  steps = [
    { num: '01', ic: '📝', title: 'Reportas', desc: 'Cargas la información y fotos del animal en pocos minutos, desde tu teléfono.', bg: '#fdecec' },
    { num: '02', ic: '🩺', title: 'Un veterinario revisa', desc: 'Nuestro equipo verifica y valida cada caso antes de publicarlo.', bg: '#E8F2FA' },
    { num: '03', ic: '💚', title: 'Reencuentro o adopción', desc: 'Coordinamos el reencuentro con la familia o una adopción responsable.', bg: '#e7f5ef' }
  ];

  @ViewChild('reportModal') reportModal!: ReportModalComponent;

  ngOnInit() {
    this.solicitudService.search().subscribe({
      next: (data) => {
        // take first 5 recent reports
        this.recentReports.set(data.slice(0, 5));
      },
      error: (err) => {
        console.error('Error loading recent reports:', err);
      }
    });
  }

  getRelativeTime(dateStr: string): string {
    if (!dateStr) return '';
    try {
      const now = new Date();
      const past = new Date(dateStr);
      const diffMs = now.getTime() - past.getTime();
      const diffMins = Math.floor(diffMs / 60000);
      if (diffMins < 1) return 'Hace un momento';
      if (diffMins < 60) return `Hace ${diffMins} min`;
      const diffHours = Math.floor(diffMins / 60);
      if (diffHours < 24) return `Hace ${diffHours} h`;
      const diffDays = Math.floor(diffHours / 24);
      if (diffDays === 1) return 'Ayer';
      return `Hace ${diffDays} días`;
    } catch {
      return '';
    }
  }

  formatRegistryId(id: number): string {
    if (!id) return 'SOL-00000';
    return 'SOL-' + id.toString().padStart(5, '0');
  }

  openReportModal(type: 'P' | 'E' = 'P') {
    this.reportModal.openReportModal(type);
  }
}

