import { Component } from '@angular/core';
import { Utilidades } from 'src/app/constants';
import { UsuarioRoyalmon } from 'src/app/interfaces/interfaces';
import { AudioService } from 'src/app/servicios/audio.service';

@Component({
  selector: 'app-royalmons',
  templateUrl: './royalmons.component.html',
  styleUrls: ['./royalmons.component.scss']
})
export class RoyalmonsComponent {

  constructor(private audioService:AudioService) {

  }

  royalmonSeleccionado:UsuarioRoyalmon|null = null;
  mostrarDatos:boolean = false;
  nombreRoyalmon:string = ""
  listaUsuarioRoyalmon:any = null;

  seleccionarRoyalmon = (royalmon:UsuarioRoyalmon) => {
    this.royalmonSeleccionado = royalmon;
    this.nombreRoyalmon = Utilidades.getNombreMoteRoyalmon(this.royalmonSeleccionado.royalmonCapturado);
  }

  moverRoyalmonPrimeraPosicion = () => {
    if (!this.royalmonSeleccionado) return;
    const equipo: (UsuarioRoyalmon | null)[] = Utilidades.getRoyalmonsEquipo();
    for (let r of equipo) {
      if (!r) continue;
      if (r.index_guardado === 0) {
        r.index_guardado = this.royalmonSeleccionado.index_guardado;
        this.royalmonSeleccionado.index_guardado = 0;
        this.royalmonSeleccionado = null;
        Utilidades.ordenarRoyalmonsByIndex();
        this.listaUsuarioRoyalmon = Utilidades.getRoyalmonsEquipo();
        break;
      }
    }
  }

  establecerAcompanante = (acomp:boolean) => {
    if (!this.royalmonSeleccionado) return;
    if (!acomp) {
      this.royalmonSeleccionado.es_acompanante = false;
      Utilidades.areaComponent.royalmonAcomp = null;
      return;
    }
    for (let r of Utilidades.getRoyalmonsEquipo()) {
      if (!r) continue;
      r.es_acompanante = false;
    }
    Utilidades.areaComponent.royalmonAcomp = this.royalmonSeleccionado;
    this.royalmonSeleccionado.es_acompanante = true;
    this.audioService.reproducirSonidoRoyalmon(this.royalmonSeleccionado.royalmonCapturado.royalmon);
  }

}
