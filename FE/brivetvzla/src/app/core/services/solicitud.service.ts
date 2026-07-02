import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Solicitud } from '../../shared/models/solicitud.model';
import { Page } from '../../shared/models/pagination.model';
import { environment } from '../../../environments/environment';

export interface SolicitudFilter {
  tp?: 'P' | 'E' | '';
  st?: 'P' | 'R' | 'A' | '';
  page?: number;
  size?: number;
}

@Injectable({ providedIn: 'root' })
export class SolicitudService {
  private base = `${environment.apiUrl}/solicitudes`;

  constructor(private http: HttpClient) {}

  getAll(filter: SolicitudFilter = {}): Observable<Page<Solicitud>> {
    let params = new HttpParams()
      .set('page',  (filter.page ?? 0).toString())
      .set('size',  (filter.size ?? 5).toString());
    if (filter.tp) params = params.set('tp', filter.tp);
    if (filter.st) params = params.set('st', filter.st);
    return this.http.get<Page<Solicitud>>(this.base, { params });
  }

  getById(id: number): Observable<Solicitud> {
    return this.http.get<Solicitud>(`${this.base}/${id}`);
  }

  search(tipo?: 'PERDIDA' | 'ENCONTRADA' | '', especie?: 'PERRO' | 'GATO' | '', estadoId?: number, ciudad?: string): Observable<any[]> {
    let params = new HttpParams();
    if (tipo) params = params.set('tipo', tipo);
    if (especie) params = params.set('especie', especie);
    if (estadoId) params = params.set('estadoId', estadoId.toString());
    if (ciudad) params = params.set('ciudad', ciudad);

    const url = `${environment.apiUrl}${environment.endpoints.solicitudSearch}`;
    return this.http.get<any[]>(url, { params });
  }

  updateStatus(id: number, st: string, obs: string): Observable<Solicitud> {
    return this.http.patch<Solicitud>(`${this.base}/${id}/status`, { so_st_solicitud: st, so_de_observacion_vet: obs });
  }

  create(payload: any, files?: File[]): Observable<any> {
    const formData = new FormData();
    formData.append('data', new Blob([JSON.stringify(payload)], { type: 'application/json' }));
    if (files && files.length > 0) {
      files.forEach(f => {
        formData.append('fotos', f, f.name);
      });
    }
    const url = `${environment.apiUrl}/solicitud`;
    return this.http.post<any>(url, formData);
  }
}

