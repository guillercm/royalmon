import { Injectable } from '@angular/core';
import { Howl, Howler } from 'howler';
import { Utilidades } from '../constants';
import { Royalmon } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root',
})
export class AudioService {

  private backgroundMusic: Howl | null = null;
  private musics_prevs: string[] = [];

  // private sound: Howl | null = null;
  private base_path: string = "assets/audio/";

  private audios_a_cargar = ["royalmons.mp3"];

  private volumen_background_music: number = 0.3;
  private volumen_sounds: number = 1;

  constructor() {
    this.cargarAudios();
  }

  private cargarAudios() {
    this.audios_a_cargar.forEach((archivo) => {
      const sound = new Howl({
        src: [`${this.base_path}sonidos/${archivo}`],
        autoplay: false, // No reproducir automáticamente
        preload: true,   // Cargar el sonido inmediatamente
      });
  
      // Iniciar la carga del sonido
      sound.load();
    });
  }

  limpiarMusica() {
    this.musics_prevs.length = 0;
  }

  actualizarVolumen() {
    if (this.backgroundMusic) {
      this.backgroundMusic.volume(Utilidades.conVolumen ? this.volumen_background_music : 0);
    }
  }

  quitarMusica() {
    if (this.backgroundMusic) {
      this.backgroundMusic.stop();
      this.backgroundMusic.unload();
      this.backgroundMusic = null;
    }
  }

  reproducirMusica(archivo: string | null, segundos:number = 0) {
    
    this.quitarMusica();
  
    if (!archivo) return;

    this.backgroundMusic = new Howl({
      src: [`${this.base_path}musica/${archivo}`],
      loop: true, // Repetir en bucle
      autoplay: true, // Reproducir automáticamente
      volume: Utilidades.conVolumen ? this.volumen_background_music : 0
    });

    if (segundos) {
      this.backgroundMusic.on('end', () => {
        this.backgroundMusic?.seek(segundos);
      });
    }
    
  }

  reproducirSonido(archivo: string, inicio: string | null = null, final: string | null = null) {
    
    if (!Utilidades.conVolumen) return;
    
    const sound = new Howl({
      src: [`${this.base_path}sonidos/${archivo}`],
      autoplay: false
    });

    
  
    if (inicio && final) {
      const [inicioMinutos, inicioSegundos, inicioDecimas] = inicio.split(':').map(Number);
      const [finalMinutos, finalSegundos, finalDecimas] = final.split(':').map(Number);

      const inicioTotal = inicioMinutos * 60 + inicioSegundos + inicioDecimas / 10;
      const finalTotal = finalMinutos * 60 + finalSegundos + finalDecimas / 10;

      if (sound.state() == 'loaded') {
        sound.volume(this.volumen_sounds);
        sound.seek(inicioTotal);
        sound.play();
        
        setTimeout(() => {
          sound.stop();
        }, (finalTotal - inicioTotal) * 1000);
      }
      
    } else {
      sound.play();
      
    }
    
  }

  reproducirSonidoRoyalmon(royalmon:number|Royalmon, hagstag: string = 'saludo'): string | null {
    if (!royalmon) return null;
    let r:Royalmon = typeof royalmon === "number" ? Utilidades.getRoyalmonById(royalmon) : royalmon;
    if (r) {
      const audio = Utilidades.getAudiosRoyalmon(r, hagstag, false)[0];
      this.reproducirSonido(audio.audio, audio.inicio, audio.final);
      return audio.texto;
    }
    return null;
  }
  
}
