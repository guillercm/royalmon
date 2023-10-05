package com.joyfe.daw.des.response;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity(name = "grupos_de_acciones")
public class GrupoDeAcciones {

	public GrupoDeAcciones(TIPOS tipo, boolean infinito, GrupoDeAcciones grupoDeAcciones, Npc npc,
			ObjetoArea objetoArea, List<Accion> acciones, int xIni, int yIni, int xFin, int yFin, int direccion) {
		super();
		this.tipo = tipo;
		this.infinito = infinito;
		this.npc = npc;
		this.objetoArea = objetoArea;
		this.acciones = acciones;
		this.xIni = xIni;
		this.yIni = yIni;
		this.xFin = xFin;
		this.yFin = yFin;
		this.fk_grupoDeAccionesCompletada = -1l;
		this.direccion = direccion;
	}
	
	public GrupoDeAcciones(TIPOS tipo, boolean infinito, GrupoDeAcciones grupoDeAcciones, Npc npc,
			ObjetoArea objetoArea, List<Accion> acciones, int xIni, int yIni, int xFin, int yFin, int direccion, Long fk_grupoDeAccionesCompletada) {
		super();
		this.tipo = tipo;
		this.infinito = infinito;
		this.npc = npc;
		this.objetoArea = objetoArea;
		this.acciones = acciones;
		this.xIni = xIni;
		this.yIni = yIni;
		this.xFin = xFin;
		this.yFin = yFin;
		this.fk_grupoDeAccionesCompletada = fk_grupoDeAccionesCompletada;
		this.direccion = direccion;
	}
	
	public GrupoDeAcciones() {}

	@Id
	@GeneratedValue
	private Long id_grupo_de_acciones;
	
	public enum TIPOS {
		alrededor_mirando_npc,   // Cuando el jugador está abajo, arriba, derecha o izquierda del npc y mirando al npc
		npc_mira_jugador, 		 // Cuando un npc puede ver al jugador a ciertas casillas de diferencia
		alrededor_mirando_objeto,// Cuando el jugador está abajo, arriba, derecha o izquierda del objeto y mirandolo
		coordenadas,			 // Cuando el jugador esté en las coordenadas del área
		coordenadas_espacio 	 // Cuando el jugador esté en las coordenadas del área y pulsa la tecla espacio
	}
	
	public static GrupoDeAcciones alrededorMirandoNpc(Npc npc, List<Accion> acciones) {
		return new GrupoDeAcciones(
				GrupoDeAcciones.TIPOS.alrededor_mirando_npc,
				false, null, npc, null, acciones ,1,1,1,1,1
		);
	}
	
	public static GrupoDeAcciones alrededorMirandoObjeto(ObjetoArea objetoArea, List<Accion> acciones) {
		return new GrupoDeAcciones(
				GrupoDeAcciones.TIPOS.alrededor_mirando_objeto,
				false, null, null, objetoArea, acciones ,1,1,1,1,1
		);
	}
	
	public static GrupoDeAcciones npcMiraJugador(Npc npc, List<Accion> acciones) {
		return new GrupoDeAcciones(
				GrupoDeAcciones.TIPOS.npc_mira_jugador,
				false, null, npc, null, acciones ,1,1,1,1,1
		);
	}
	
	public static GrupoDeAcciones coordenadas(List<Accion> acciones, int xIni, int yIni, int xFin, int yFin, int direccion) {
		return new GrupoDeAcciones(
				GrupoDeAcciones.TIPOS.coordenadas,
				false, null, null, null, acciones, xIni, yIni, xFin, yFin, direccion
		);
	}
	
	public static GrupoDeAcciones coordenadas(List<Accion> acciones, int xIni, int yIni, int xFin, int yFin) {
		return new GrupoDeAcciones(
				GrupoDeAcciones.TIPOS.coordenadas,
				false, null, null, null, acciones, xIni, yIni, xFin, yFin, -1
		);
	}
	
	public static GrupoDeAcciones coordenadasEspacio(List<Accion> acciones, int xIni, int yIni, int xFin, int yFin, int direccion) {
		return new GrupoDeAcciones(
				GrupoDeAcciones.TIPOS.coordenadas_espacio,
				false, null, null, null, acciones, xIni, yIni, xFin, yFin, direccion
		);
	}
	
	public static GrupoDeAcciones coordenadasEspacio(List<Accion> acciones, int xIni, int yIni, int xFin, int yFin) {
		return new GrupoDeAcciones(
				GrupoDeAcciones.TIPOS.coordenadas_espacio,
				false, null, null, null, acciones, xIni, yIni, xFin, yFin, -1
		);
	}
	
	public GrupoDeAcciones infinito() {
		this.infinito = true;
		return this;
	}
	
	private TIPOS tipo;
	
	private boolean infinito;    // Si es true, significa que siempre se ejecutará esto
	
	private Long fk_grupoDeAccionesCompletada;
		// Si esto no es nulo, significará que las acciones actuales 
		// sólo se ejecutarán cuando estén las acciones completadas de este grupo
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "fk_npc")
	private Npc npc;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "fk_objeto_area")
	private ObjetoArea objetoArea;
	

	
	/*
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_area")
	private Area area;
	*/
		
	@ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "ubicaciones_acciones_acciones",
            joinColumns = @JoinColumn(name = "fk_ubicacion_accion"),
            inverseJoinColumns = @JoinColumn(name = "fk_accion")
    )
	private List<Accion> acciones;
	
	private int xIni;
	
	private int yIni;
	
	private int xFin;
	
	private int yFin;
	
	private int direccion;

	public Long getId_grupo_de_acciones() {
		return id_grupo_de_acciones;
	}

	public void setId_grupo_de_acciones(Long id_grupo_de_acciones) {
		this.id_grupo_de_acciones = id_grupo_de_acciones;
	}

	public TIPOS getTipo() {
		return tipo;
	}

	public void setTipo(TIPOS tipo) {
		this.tipo = tipo;
	}

	public boolean isInfinito() {
		return infinito;
	}

	public void setInfinito(boolean infinito) {
		this.infinito = infinito;
	}

	public Long getFkGrupoDeAccionesCompletadas() {
		return fk_grupoDeAccionesCompletada;
	}

	public GrupoDeAcciones setGrupoDeAccionesCompletadas(GrupoDeAcciones grupoDeAcciones) {
		this.fk_grupoDeAccionesCompletada = grupoDeAcciones.getId_grupo_de_acciones();
		return this;
	}

	public Npc getNpc() {
		return npc;
	}

	public void setNpc(Npc npc) {
		this.npc = npc;
	}

	public ObjetoArea getObjetoArea() {
		return objetoArea;
	}

	public void setObjetoArea(ObjetoArea objetoArea) {
		this.objetoArea = objetoArea;
	}

	public List<Accion> getAcciones() {
		return acciones;
	}

	public void setAcciones(List<Accion> acciones) {
		this.acciones = acciones;
	}

	public int getxIni() {
		return xIni;
	}

	public int getyIni() {
		return yIni;
	}

	public int getxFin() {
		return xFin;
	}

	public int getyFin() {
		return yFin;
	}

	public int getDireccion() {
		return direccion;
	}

	public void setxIni(int xIni) {
		this.xIni = xIni;
	}

	public void setyIni(int yIni) {
		this.yIni = yIni;
	}

	public void setxFin(int xFin) {
		this.xFin = xFin;
	}

	public void setyFin(int yFin) {
		this.yFin = yFin;
	}

	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}
	
}
