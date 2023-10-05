import { Component } from '@angular/core';
import { Utilidades, direcciones } from 'src/app/constants';
import { Area, UsuarioRoyalmon } from 'src/app/interfaces/interfaces';
import { JugadorComponent } from 'src/app/jugador/jugador.component';

@Component({
  selector: 'app-mapa',
  templateUrl: './mapa.component.html',
  styleUrls: ['./mapa.component.scss']
})
export class MapaComponent {

  areas: Area[] = []

  nombreAtaqueCambiarArea:string = "Vuelo"

  royalmonCambiarArea:UsuarioRoyalmon|null = null;

  areaSelecionada: Area | null = null;

  infoOculta: boolean = true;

  constructor() {
    for (const [key, value] of Object.entries(Utilidades.areas)) {
      let area:Area = value as Area;
      if ( area.aparecerEnMapa /*area.tipo === "principal" */ ) {
        this.areas.unshift(Utilidades.getAreaById(area.id_area));
      }
    }
    this.setRoyalmonCambiarArea();
    
  }

  heVisitadoArea = (area:Area):boolean => {
    for (let a of Utilidades.user.areasVisitadas) {
      if (a.id_area === area.id_area) return true;
    }
    return false;
  }

  setRoyalmonCambiarArea = () => {
    this.royalmonCambiarArea = Utilidades.getRoyalmonByAtaque(this.nombreAtaqueCambiarArea, false);
  }

  getMensajeCambiarArea = () => {
    return `¿Quieres que ${Utilidades.getNombreMoteRoyalmon(this.royalmonCambiarArea?.royalmonCapturado)} use ${this.nombreAtaqueCambiarArea} para ir a ${this.areaSelecionada?.nombre}?`;
  }

  setAreaSeleccionada = (area:Area) => {
    if (!Utilidades.user.area.puedoVolar) return;
    if (area.tipo !== 'principal') return;
    if (!this.royalmonCambiarArea) return;
    if (!this.heVisitadoArea(area)) return;
    if (this.areaSelecionada && this.areaSelecionada.id_area === area.id_area) {
      this.areaSelecionada = null;
      return;
    }
    this.areaSelecionada = area;
  }

  getURLImageRutaBaseArea = (area:Area) => {
    return 'url(' + Utilidades.getURLImageRutaBaseArea(area) + ')';
  }

  toggleInfoMapa() {
    this.infoOculta = !this.infoOculta;
  }

  cambiarArea = () => {
    if (!this.areaSelecionada || !this.royalmonCambiarArea) return;
    Utilidades.inventarioAbierto = false;
    Utilidades.jugadorComponent.volarArea(this.areaSelecionada, this.royalmonCambiarArea);
  }

}
