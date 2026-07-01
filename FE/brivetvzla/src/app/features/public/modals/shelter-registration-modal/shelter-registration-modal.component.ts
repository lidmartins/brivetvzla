import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-shelter-registration-modal',
  imports: [],
  templateUrl: './shelter-registration-modal.component.html',
  styleUrl: './shelter-registration-modal.component.scss'
})
export class ShelterRegistrationModalComponent {
  showModal = false;
  currentStep = signal(1);
  submitSuccess = signal(false);

  openModal() {
    this.showModal = true;
    this.currentStep.set(1);
    this.submitSuccess.set(false);
  }

  closeModal() {
    this.showModal = false;
  }

  nextStep() {
    if (this.currentStep() < 4) {
      this.currentStep.update(s => s + 1);
    } else if (this.currentStep() === 4) {
      this.submitSuccess.set(true);
      this.currentStep.set(5);
    }
  }

  prevStep() {
    if (this.currentStep() > 1 && !this.submitSuccess()) {
      this.currentStep.update(s => s - 1);
    }
  }
}
