 import { Component } from '@angular/core';
import { Utilidades } from '../constants';
// import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent {
  selectedComponent: string = '';

  btnVolverHabilitado:boolean = true;

  habilitarBotonesAtras = (value:boolean) => {
    this.btnVolverHabilitado = value;
  }

  toggleComponent(component: string) {
    this.selectedComponent = component;
    console.log(this.selectedComponent)
    Utilidades.jugadorComponent.puedoCerrarMenu = this.selectedComponent === '';
  }

  guardarProgreso() {
    Utilidades.jugadorComponent.guardarProgreso();
  }

}
