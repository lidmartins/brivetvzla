import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { SolicitudService } from '../../../../core/services/solicitud.service';

@Component({
  selector: 'app-report-modal',
  imports: [ReactiveFormsModule],
  templateUrl: './report-modal.component.html',
  styleUrl: './report-modal.component.scss'
})
export class ReportModalComponent {
  private fb           = inject(FormBuilder);
  private solicitudSvc = inject(SolicitudService);

  showModal     = false;
  submitting    = signal(false);
  submitSuccess = signal(false);
  submitError   = signal('');
  currentStep   = signal(1);
  selectedType  = signal<'P' | 'E' | ''>('');

  selectedFile: File | null = null;
  previewUrl: string | null = null;

  reportForm = this.fb.group({
    tipoSolicitud: ['PERDIDA', Validators.required],
    
    // Step 2: Animal info
    nombre: [''],
    especie: ['PERRO', Validators.required],
    raza: [''],
    color: ['', Validators.required],
    tamanio: ['MEDIANO', Validators.required],
    sexo: ['MACHO', Validators.required],
    edadAproximada: [null as number | null],
    descripcion: ['', Validators.required],
    requiereAtencionMedica: [false],
    
    // Step 3: Location
    estadoId: ['', Validators.required],
    ciudad: [''],
    direccion: [''],
    referencia: [''],
    
    // Step 4: Contact
    contactoNombre: ['', Validators.required],
    contactoApellido: ['', Validators.required],
    contactoTelefono: ['', Validators.required],
    contactoEmail: [''],
    contactoWhatsapp: ['']
  });

  openReportModal(type: 'P' | 'E' = 'P') {
    this.showModal = true;
    this.currentStep.set(1);
    this.selectedType.set(type);
    this.submitSuccess.set(false);
    this.submitError.set('');
    this.reportForm.reset({
      tipoSolicitud: type === 'P' ? 'PERDIDA' : 'ENCONTRADA',
      especie: 'PERRO',
      tamanio: 'MEDIANO',
      sexo: 'MACHO',
      requiereAtencionMedica: false,
      estadoId: '',
      nombre: '',
      raza: '',
      color: '',
      edadAproximada: null,
      descripcion: '',
      ciudad: '',
      direccion: '',
      referencia: '',
      contactoNombre: '',
      contactoApellido: '',
      contactoTelefono: '',
      contactoEmail: '',
      contactoWhatsapp: ''
    });
    this.selectedFile = null;
    this.previewUrl = null;
  }

  closeModal() {
    this.showModal = false;
  }

  selectType(type: 'P' | 'E') {
    this.selectedType.set(type);
    this.reportForm.patchValue({ tipoSolicitud: type === 'P' ? 'PERDIDA' : 'ENCONTRADA' });
  }

  onFileSelected(e: Event) {
    const input = e.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result as string;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onDrop(e: DragEvent) {
    e.preventDefault();
    if (e.dataTransfer?.files && e.dataTransfer.files.length > 0) {
      this.selectedFile = e.dataTransfer.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result as string;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  removeFile() {
    this.selectedFile = null;
    this.previewUrl = null;
  }

  isStepValid(step: number): boolean {
    if (step === 1) {
      return this.reportForm.get('tipoSolicitud')?.valid ?? false;
    }
    if (step === 2) {
      const controls = ['especie', 'color', 'tamanio', 'descripcion'];
      return controls.every(name => this.reportForm.get(name)?.valid);
    }
    if (step === 3) {
      return this.reportForm.get('estadoId')?.valid ?? false;
    }
    if (step === 4) {
      const controls = ['contactoNombre', 'contactoApellido', 'contactoTelefono'];
      return controls.every(name => this.reportForm.get(name)?.valid);
    }
    return true;
  }

  markStepControlsAsTouched(step: number) {
    let controls: string[] = [];
    if (step === 1) controls = ['tipoSolicitud'];
    if (step === 2) controls = ['especie', 'color', 'tamanio', 'descripcion'];
    if (step === 3) controls = ['estadoId'];
    if (step === 4) controls = ['contactoNombre', 'contactoApellido', 'contactoTelefono'];
    
    controls.forEach(name => {
      this.reportForm.get(name)?.markAsTouched();
    });
  }

  nextStep() {
    if (!this.isStepValid(this.currentStep())) {
      this.markStepControlsAsTouched(this.currentStep());
      return;
    }
    
    if (this.currentStep() < 5) {
      this.currentStep.set(this.currentStep() + 1);
    } else {
      this.submit();
    }
  }

  prevStep() {
    if (this.currentStep() > 1) {
      this.currentStep.set(this.currentStep() - 1);
    }
  }

  submit() {
    if (this.reportForm.invalid) {
      this.reportForm.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    this.submitError.set('');

    const v = this.reportForm.getRawValue();
    const payload = {
      tipoSolicitud: v.tipoSolicitud,
      animal: {
        nombre: v.nombre || undefined,
        especie: v.especie === 'PERRO' ? 'PERRO' : 'GATO',
        raza: v.raza || undefined,
        color: v.color,
        tamanio: v.tamanio === 'P' ? 'PEQUENIO' : (v.tamanio === 'G' ? 'GRANDE' : 'MEDIANO'),
        sexo: v.sexo === 'H' ? 'HEMBRA' : (v.sexo === 'N' ? 'NO_SE' : 'MACHO'),
        edadAproximada: v.edadAproximada ? parseInt(v.edadAproximada as any, 10) : undefined,
        descripcion: v.descripcion,
        requiereAtencionMedica: v.requiereAtencionMedica
      },
      ubicacion: {
        estadoId: parseInt(v.estadoId!, 10),
        ciudad: v.ciudad || undefined,
        direccion: v.direccion || undefined,
        referencia: v.referencia || undefined
      },
      contacto: {
        nombre: v.contactoNombre,
        apellido: v.contactoApellido,
        email: v.contactoEmail || undefined,
        telefono: v.contactoTelefono,
        whatsapp: v.contactoWhatsapp || v.contactoTelefono,
        metodoContacto: 'WHATSAPP',
        permitirDatosPublicos: true
      }
    };

    const filesArray = this.selectedFile ? [this.selectedFile] : [];
    this.solicitudSvc.create(payload, filesArray).subscribe({
      next: () => {
        this.submitting.set(false);
        this.submitSuccess.set(true);
      },
      error: (err: Error) => {
        this.submitting.set(false);
        this.submitError.set(err.message || 'Error al enviar el reporte. Por favor, intente de nuevo.');
      }
    });
  }

  hasRequiredError(controlName: string): boolean {
    const control = this.reportForm.get(controlName);
    return !!control?.hasError('required') && (control.touched || control.dirty);
  }

  getRequiredMessage(controlName: string): string {
    const messages: Record<string, string> = {
      tipoSolicitud: 'Debe seleccionar el tipo de reporte.',
      especie: 'Debe seleccionar la especie.',
      color: 'El color es obligatorio.',
      tamanio: 'Debe seleccionar el tamaño.',
      sexo: 'Debe seleccionar el sexo.',
      descripcion: 'La descripción es obligatoria.',
      estadoId: 'El estado es obligatorio.',
      contactoNombre: 'El nombre es obligatorio.',
      contactoApellido: 'El apellido es obligatorio.',
      contactoTelefono: 'El teléfono de contacto es obligatorio.'
    };

    return messages[controlName] ?? 'Este campo es obligatorio.';
  }
}
