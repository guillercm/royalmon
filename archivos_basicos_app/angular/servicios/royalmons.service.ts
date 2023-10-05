import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_BASE_URL, Utilidades } from '../constants';
import { headers, headersImg } from './header';
import { Observable } from 'rxjs';
import { FicheroService } from './fichero.service';

@Injectable({
  providedIn: 'root'
})
export class RoyalmonsService {

  constructor(private http: HttpClient) { }

  getAllRoyalmons() {
    return this.http.get<any>(`${API_BASE_URL}/royalmons`, { headers });
  }

  getAllTipos() {
    return this.http.get<any>(`${API_BASE_URL}/tipos`, { headers });
  }

  getAllAtaques() {
    return this.http.get<any>(`${API_BASE_URL}/ataques`, { headers });
  }

  getAllObjetosBolsa() {
    return this.http.get<any>(`${API_BASE_URL}/objetosBolsa`, { headers });
  }


}
