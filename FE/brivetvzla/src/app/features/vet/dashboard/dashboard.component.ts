import { Component, inject, signal, computed, OnInit, ViewChild } from '@angular/core';
import { SolicitudService } from '../../../core/services/solicitud.service';
import { ReportModalComponent } from '../../public/modals/report-modal/report-modal.component';

@Component({
  selector: 'app-dashboard',
  imports: [ReportModalComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  private solicitudSvc = inject(SolicitudService);

  @ViewChild('reportModal') reportModal!: ReportModalComponent;

  currentDate = '';
  allRequests = signal<any[]>([]);
  pendingRequests = signal<any[]>([]);
  recentRequests = signal<any[]>([]);

  recentAnimals = computed(() => {
    return this.allRequests()
      .slice(0, 4)
      .map(r => {
        const isCat = r.animal?.especie === 'G';
        const isDog = r.animal?.especie === 'P';
        const speciesEmoji = isCat ? '🐈' : '🐕';
        const speciesLabel = isCat ? 'Gato' : 'Perro';
        
        let stateLabel = 'Pendiente';
        let stBg = '#fdeee9';
        let stColor = '#c4562f';

        if (r.estado === 'A') {
          stateLabel = 'Activo';
          stBg = '#eaf0fb';
          stColor = '#3a5fb0';
        } else if (r.estado === 'U' || r.estado === 'C') {
          stateLabel = 'Encontrado';
          stBg = '#e7f5ef';
          stColor = '#1b7a55';
        } else if (r.estado === 'T') {
          stateLabel = 'Adoptado';
          stBg = '#e7f5ef';
          stColor = '#1b7a55';
        } else if (r.estado === 'R') {
          stateLabel = 'Rechazado';
          stBg = '#f3f4f6';
          stColor = '#6b7280';
        }

        return {
          name: r.animal?.nombre || 'Sin nombre',
          ic: speciesEmoji,
          breed: `${speciesLabel} · ${r.animal?.raza || 'Mestizo'}`,
          st: stateLabel,
          stBg,
          stColor
        };
      });
  });

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    const options: Intl.DateTimeFormatOptions = { weekday: 'long', day: 'numeric', month: 'long' };
    const rawDate = new Date().toLocaleDateString('es-ES', options);
    this.currentDate = rawDate.charAt(0).toUpperCase() + rawDate.slice(1) + ' · Emergencia activa';

    this.solicitudSvc.getVetSolicitudes().subscribe({
      next: (list) => {
        this.allRequests.set(list);
        const sorted = [...list].sort((a, b) => b.id - a.id);
        this.recentRequests.set(sorted.slice(0, 5));
      }
    });

    this.solicitudSvc.getVetSolicitudes('PENDIENTE').subscribe({
      next: (list) => {
        this.pendingRequests.set(list);
      }
    });
  }

  approve(id: number) {
    this.solicitudSvc.updateVetSolicitudStatus(id, 'ACTIVA').subscribe({
      next: () => {
        this.loadData();
        this.solicitudSvc.refreshPendingCount();
      }
    });
  }

  markReunida(id: number) {
    this.solicitudSvc.updateVetSolicitudStatus(id, 'REUNIDA').subscribe({
      next: () => {
        this.loadData();
        this.solicitudSvc.refreshPendingCount();
      }
    });
  }

  getInitials(name?: string, apellido?: string): string {
    const f = name?.charAt(0) || '';
    const l = apellido?.charAt(0) || '';
    return (f + l).toUpperCase() || '??';
  }

  getAvatarStyle(initials: string) {
    const colors = [
      { bg: '#E8F2FA', color: '#0F4C76' },
      { bg: '#FBE7E0', color: '#c4562f' },
      { bg: '#E7EAF4', color: '#3a5fb0' },
      { bg: '#F3E8F1', color: '#7E5BD0' },
      { bg: '#E6F1E9', color: '#1b7a55' }
    ];
    let sum = 0;
    for (let i = 0; i < initials.length; i++) { sum += initials.charCodeAt(i); }
    return colors[sum % colors.length];
  }

  formatDate(dateStr?: string): string {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const months = ['ene', 'feb', 'mar', 'abr', 'may', 'jun', 'jul', 'ago', 'sep', 'oct', 'nov', 'dic'];
    return `${date.getDate()} ${months[date.getMonth()]}`;
  }

  openReportModal() {
    this.reportModal.openReportModal('P');
  }
}
