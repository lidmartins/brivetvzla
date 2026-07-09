import { Component, signal, computed, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportModalComponent } from '../modals/report-modal/report-modal.component';
import { PetCardModalComponent } from '../modals/pet-card-modal/pet-card-modal.component';
import { SolicitudService } from '../../../core/services/solicitud.service';

@Component({
  selector: 'app-lost',
  imports: [FormsModule, ReportModalComponent, PetCardModalComponent],
  templateUrl: './lost.component.html',
  styleUrl: './lost.component.scss'
})
export class LostComponent implements OnInit {
  private solicitudService = inject(SolicitudService);

  filter = signal<'all' | 'dog' | 'cat'>('all');
  stateId = signal<number | null>(null);
  animals = signal<any[]>([]);

  filtered = computed(() => {
    return this.animals();
  });

  ngOnInit() {
    this.loadAnimals();
  }

  setFilter(v: 'all' | 'dog' | 'cat') {
    this.filter.set(v);
    this.loadAnimals();
  }

  onStateChange(e: Event) {
    const val = (e.target as HTMLSelectElement).value;
    this.stateId.set(val ? parseInt(val, 10) : null);
    this.loadAnimals();
  }

  loadAnimals() {
    const especieParam = this.filter() === 'dog' ? 'PERRO' : (this.filter() === 'cat' ? 'GATO' : '');
    const estadoIdParam = this.stateId() || undefined;

    this.solicitudService.search('PERDIDA', especieParam, estadoIdParam).subscribe({
      next: (data) => {
        const mapped = data.map(sol => this.mapSolicitudToAnimal(sol));
        this.animals.set(mapped);
      },
      error: (err) => {
        console.error('Error loading lost pets:', err);
      }
    });
  }

  mapSolicitudToAnimal(sol: any) {
    const isDog = sol.animal?.especie === 'P';
    const isCat = sol.animal?.especie === 'G';
    const sizeStr = sol.animal?.tamanio === 'P' ? 'pequeño' : (sol.animal?.tamanio === 'M' ? 'mediano' : (sol.animal?.tamanio === 'G' ? 'grande' : ''));
    
    return {
      id: sol.id,
      name: sol.tipo === 'E' && !sol.animal?.nombre ? 'Desconocido' : sol.animal?.nombre,
      species: isDog ? 'dog' : (isCat ? 'cat' : 'other'),
      especie: isDog ? 'perro' : (isCat ? 'gato' : 'otro'),
      breed: sol.animal?.raza + (sizeStr ? ` ${sizeStr}` : ''),
      state: sol.ubicacion?.estado?.nombre || '',
      zone: sol.ubicacion?.ciudad || '',
      time: this.getRelativeTime(sol.fechaEvento || sol.createdAt),
      color: sol.mainPhotoUrl ? `url('${sol.mainPhotoUrl}')` : (sol.tipo === 'P' ? 'linear-gradient(140deg,#FEF2F2,#fdecec)' : 'linear-gradient(140deg,#F0FDF4,#e7f5ef)'),
      hasPhoto: !!sol.mainPhotoUrl,
      mainPhotoUrl: sol.mainPhotoUrl || '',
      icon: isDog ? '🐕' : (isCat ? '🐈' : '🐾'),
      status: sol.tipo === 'P' ? 'Buscando' : (sol.estado === 'C' ? 'Reunido' : (sol.estado === 'T' ? 'Adoptado' : 'Encontrado')),
      colorDesc: sol.animal?.color || '',
      description: sol.animal?.descripcion || '',
    };
  }

  formatRegistryId(id: number): string {
    if (!id) return 'SOL-00000';
    return 'SOL-' + id.toString().padStart(5, '0');
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
}
