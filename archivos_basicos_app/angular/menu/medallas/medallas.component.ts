import { Component } from '@angular/core';
import { Utilidades } from 'src/app/constants';

@Component({
  selector: 'app-medallas',
  templateUrl: './medallas.component.html',
  styleUrls: ['./medallas.component.scss']
})
export class MedallasComponent {
  num_total_medallas = 9;
  medallas_usuario = Utilidades.user.medallas;

  medallasArray = Array.from({ length: this.num_total_medallas - 1 }, (_, i) => i + 1);

  getURLImageMedalla = (n: number) => Utilidades.getURLImageMedalla(n);

  tengoMedalla = (n: number) => {
    return this.medallas_usuario >= n;
  };

  

}

