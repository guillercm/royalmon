import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import * as FileSaver from 'file-saver';
import * as CryptoJS from 'crypto-js';

@Injectable({
  providedIn: 'root'
})
export class FicheroService {

  public fichero:string | null = null;
  private path_base: string = "";
  private secret_key: string = "??";

  constructor(private httpClient: HttpClient) { }

  guardar(data:any): any {
    if (typeof data === "object") {
      data = JSON.stringify(data)
    } 
    data = data.toString();
    const str = this.encriptar(data);
    if (!str) {
      alert("Ha habido algún problema al guardar el archivo");
      return;
    }
    const blob = new Blob([str], {type: 'text/plain;charset=utf-8'});
    FileSaver.saveAs(blob, this.fichero + ".txt");
  }

  guardarSiNoExiste(data:any): any {
    const data1= data;
    this.leer((data:string|null) => {
      if (!data) {
        this.guardar(data1);
      }
    });
  }
  
  private encriptar(data: string): string | null {
    try {
      return CryptoJS.AES.encrypt( data, this.secret_key).toString();
    } catch (e:any) {
      return null;
    }
  }

  private desencriptar(data: string): string | null {
    try {
      return CryptoJS.AES.decrypt( data, this.secret_key ).toString(CryptoJS.enc.Utf8);
    } catch (e:any) {
      return null;
    }
  }

  getRuta = () => `assets/${this.path_base}${this.fichero}.txt`;

  setRuta(fichero:string, carpeta:string = "data/") {
    this.fichero = fichero;
    this.path_base = carpeta;
  }

  leer(funcion: (data: string | null) => void) {
    const este = this;
    this.httpClient.get(this.getRuta(), { responseType: 'text' }).subscribe({
      next: (v) => { funcion(este.desencriptar(v as string))},
      error: (e) => funcion(null),
    });
  }

}

