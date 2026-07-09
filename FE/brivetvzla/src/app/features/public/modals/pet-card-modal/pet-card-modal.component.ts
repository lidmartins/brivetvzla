import { Component } from '@angular/core';

@Component({
  selector: 'app-pet-card-modal',
  imports: [],
  templateUrl: './pet-card-modal.component.html',
  styleUrl: './pet-card-modal.component.scss'
})
export class PetCardModalComponent {
  showModal = false;
  showLightbox = false;
  pet: any = {};

  open(pet: any) {
    this.pet = this.normalizePet(pet);
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.showLightbox = false;
  }

  openLightbox() {
    if (this.pet.hasPhoto) {
      this.showLightbox = true;
    }
  }

  closeLightbox(event?: Event) {
    if (event) {
      event.stopPropagation();
    }
    this.showLightbox = false;
  }

  formatRegistryId(id: number): string {
    if (!id) return 'SOL-00000';
    return 'SOL-' + id.toString().padStart(5, '0');
  }

  normalizePet(pet: any) {
    if (!pet) return {};
    if ('hasPhoto' in pet) {
      if (!pet.mainPhotoUrl) {
        if (pet.color && pet.color.startsWith("url('")) {
          pet.mainPhotoUrl = pet.color.slice(5, -2);
        } else if (pet.color && pet.color.startsWith("url(\"")) {
          pet.mainPhotoUrl = pet.color.slice(5, -2);
        }
      }
      return pet;
    }

    const isDog = pet.animal?.especie === 'P';
    const isCat = pet.animal?.especie === 'G';
    const sizeStr = pet.animal?.tamanio === 'P' ? 'pequeño' : (pet.animal?.tamanio === 'M' ? 'mediano' : (pet.animal?.tamanio === 'G' ? 'grande' : ''));

    return {
      id: pet.id,
      name: pet.tipo === 'E' && !pet.animal?.nombre ? 'Desconocido' : pet.animal?.nombre,
      species: isDog ? 'dog' : (isCat ? 'cat' : 'other'),
      especie: isDog ? 'perro' : (isCat ? 'gato' : 'otro'),
      breed: (pet.animal?.raza || '') + (sizeStr ? ` ${sizeStr}` : ''),
      state: pet.ubicacion?.estado?.nombre || '',
      zone: pet.ubicacion?.ciudad || '',
      time: this.getRelativeTime(pet.fechaEvento || pet.createdAt),
      color: pet.mainPhotoUrl ? `url('${pet.mainPhotoUrl}')` : (pet.tipo === 'P' ? 'linear-gradient(140deg,#FEF2F2,#fdecec)' : 'linear-gradient(140deg,#F0FDF4,#e7f5ef)'),
      hasPhoto: !!pet.mainPhotoUrl,
      mainPhotoUrl: pet.mainPhotoUrl || '',
      icon: isDog ? '🐕' : (isCat ? '🐈' : '🐾'),
      status: pet.tipo === 'P' ? 'Buscando' : (pet.estado === 'C' ? 'Reunido' : (pet.estado === 'T' ? 'Adoptado' : 'Encontrado')),
      colorDesc: pet.animal?.color || '',
      description: pet.animal?.descripcion || '',
    };
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
