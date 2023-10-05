import { Component } from '@angular/core';
import { Utilidades } from 'src/app/constants';
import { Royalmon } from 'src/app/interfaces/interfaces';
import { AudioService } from '../../servicios/audio.service';

@Component({
  selector: 'app-royaldex',
  templateUrl: './royaldex.component.html',
  styleUrls: ['./royaldex.component.scss']
})
export class RoyaldexComponent {

  royalmons:Royalmon[] = []

  ids_royalmons_capturados:number[] = []

  royalmonSeleccionado:Royalmon|null = null;

  animacion_royalmon_seleccionado: string = "batalla_rival";

  constructor(private audioService:AudioService) {
    this.royalmons = Utilidades.getRoyalmons();
    for (let r of Utilidades.user.royalmons) {
      const id_royalmon = r.royalmonCapturado.royalmon.id_royalmon;
      if (r.ids_anteriores_evoluciones) {
        for (let i of r.ids_anteriores_evoluciones) {
          if (!this.ids_royalmons_capturados.includes(+i)) {
            this.ids_royalmons_capturados.push(+i);
          }
        }
      }
      if (!id_royalmon) continue;
      if (!this.ids_royalmons_capturados.includes(id_royalmon)) {
        this.ids_royalmons_capturados.push(id_royalmon);
      }
    }
  }

  desbloqueado = (royalmon:Royalmon) => {
    return this.ids_royalmons_capturados.includes(royalmon.id_royalmon||0);
  }

  toggleIco = () => {
    this.animacion_royalmon_seleccionado = this.animacion_royalmon_seleccionado === "batalla" ? "batalla_rival" : "batalla";
    if (this.royalmonSeleccionado) {
      this.audioService.reproducirSonidoRoyalmon(this.royalmonSeleccionado);
    }
  }

  toggleInfo(royalmon:Royalmon) {
    this.animacion_royalmon_seleccionado = "batalla_rival";
    if (!this.royalmonSeleccionado) {
      this.royalmonSeleccionado = royalmon;
      return;
    }
    if (this.royalmonSeleccionado.id_royalmon === royalmon.id_royalmon) {
      this.royalmonSeleccionado = null;
      return;
    }
    this.royalmonSeleccionado = royalmon;
  }

}
