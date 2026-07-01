import { Component } from '@angular/core';

@Component({
  selector: 'app-pet-card-modal',
  imports: [],
  templateUrl: './pet-card-modal.component.html',
  styleUrl: './pet-card-modal.component.scss'
})
export class PetCardModalComponent {
  showModal = false;
  pet: any = {};

  open(pet: any) {
    this.pet = pet;
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }
}
