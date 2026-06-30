import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  features = [
    { icon: '🔴', bg: '#FEF2F2', color: '#DC4A4A', title: 'Mascotas Perdidas', desc: 'Reporta y busca mascotas perdidas durante emergencias. Red activa en todo el país.', link: '/perdidas', linkLabel: 'Ver reportes', go: '#DC4A4A' },
    { icon: '🟢', bg: '#F0FDF4', color: '#2F9E63', title: 'Mascotas Encontradas', desc: 'Encontraste un animal y quieres ayudarlo a regresar a casa.', link: '/encontradas', linkLabel: 'Ver reportes', go: '#2F9E63' },
    { icon: '🏠', bg: '#F0F7F5', color: '#0F766E', title: 'Refugios Temporales', desc: 'Red de refugios coordinados por veterinarios con capacidad en tiempo real.', link: '/refugios', linkLabel: 'Ver refugios', go: '#0F766E' },
    //{ icon: '💜', bg: '#F5F3FF', color: '#7E5BD0', title: 'Adopciones', desc: 'Da un hogar permanente a animales rescatados durante emergencias.', link: '/adopciones', linkLabel: 'Ver animales', go: '#7E5BD0' },
  ];

  steps = [
    { num: '01', ic: '📝', title: 'Reportas', desc: 'Cargas la información y fotos del animal en pocos minutos, desde tu teléfono.', bg: '#fdecec' },
    { num: '02', ic: '🩺', title: 'Un veterinario revisa', desc: 'Nuestro equipo verifica y valida cada caso antes de publicarlo.', bg: '#E8F2FA' },
    { num: '03', ic: '💚', title: 'Reencuentro o adopción', desc: 'Coordinamos el reencuentro con la familia o una adopción responsable.', bg: '#e7f5ef' }
  ];
}
