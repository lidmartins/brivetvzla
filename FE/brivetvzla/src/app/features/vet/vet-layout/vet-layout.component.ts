import {Component, inject, OnInit} from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {AuthService} from '../../../core/auth/auth.service';
import {SolicitudService} from '../../../core/services/solicitud.service';

@Component({
  selector: 'app-vet-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './vet-layout.component.html',
  styleUrl: './vet-layout.component.scss'
})
export class VetLayoutComponent implements OnInit {
  private auth = inject(AuthService);
  private solicitudSvc = inject(SolicitudService);
  
  user = this.auth.user;
  pendingCount = this.solicitudSvc.pendingCount;

  ngOnInit() {
    this.solicitudSvc.refreshPendingCount();
  }

  logout() { this.auth.logout(); }
}
