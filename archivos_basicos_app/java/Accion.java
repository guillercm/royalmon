package com.joyfe.daw.des.response;

import java.util.List;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joyfe.daw.des.response.Accion.TIPOS_DE_ACCIONES;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;


@Entity(name = "acciones")
public class Accion {

	
	public enum TIPOS_DE_ACCIONES {
		ESPERAR, // se espera unos determiados segundos antes de las demás acciones
	    NO_ANDAR, // prohibe al jugador moverse
	    ANDAR, // permite al jugador moverse
	    MOVER_NPC, // para mover un npc en una direccion tantas casillas como sea necesario
		MOVER_JUGADOR, // para mover al jugador en una direccion tantas casillas como sea necesario
	    CAMBIAR_AREA, // el jugador se teletransporta a otro area
		CAMBIAR_AREA_NPC, // un npc se teletransporta a otro area
		APARECER_OBJETO_MAPA, // aparece un objeto en el mapa
		DESAPARECER_OBJETO_MAPA, // Desaparecer un objeto  (abrir cofre, etc)
		MOVER_OBJETO_MAPA, // Mueve un objeto del mapa con una duracion de transicion específica
	    TEXTO, // aparece un texto normalmente dicho por un npc
	    TEXTO_CONFIRMAR, // aparece un texto junto con botones de sí o no
	    CONSEGUIR_OBJETO, // da al jugador un objeto x cantidad
	    QUITAR_OBJETO, // quita al jugador un objeto x cantidad
	    CONSEGUIR_MEDALLA, // da al jugador una medalla de gimnasio
	    ENTRAR_BATALLA, // el jugador entra en batalla con un rival
	    APARICION_ROYALMON_SALVAJE, // el jugador se encuntra con un royalmon salvaje
	    EXCLAMACION_NPC, //se le aparece al npc en la cabeza el simbolo de la exclamacion para llamar la atención
	    NPC_ACERCARSE_A_JUGADOR, // el npc se acerca al jugador si está en línea recta 
	    MOSTRAR_TIENDA, // al jugador se le muestra una tienda con unos objetos en particular
	    GESTIONAR_ROYALMONS, // el jugador podrá sacar, mover o meter royalmons en su pc
	    RESTAURAR_VIDA_ROYALMONS, // al jugador se le recuperará toda la vida de sus royalmons que tenga en el equipo
	    REALIZAR_ANIMACION_NPC, // un npc, realiza una animación determinada por el nombre
		OCULTAR_ROYALMON_ACOMP, // para ocultar el royalmon acompañante
		MOSTRAR_ROYALMON_ACOMP, // para mostrar el royalmon acompañante
	    MOSTRAR_ROYALMON_PANTALLA, // muestra al jugador la imagen de un royalmon en medio de la pantalla
		OCULTAR_ROYALMON_PANTALLA,// oculta al jugador la imagen de un royalmon en medio de la pantalla
	    OBTENER_ROYALMON_EXISTENTE, // Se le añade a su equipo un nuevo royalmon sin tener que capturarlo
	    SET_VAR_GLOBAL, // setea una variable global para el jugador
	    REPRODUCIR_SONIDO, //Para reproducir un sonido en el juego
	    REPRODUCIR_SONIDO_ROYALMON, //Para reproducir un sonido de un royalmon en el juego
	    CAMBIAR_MUSICA, // Para cambiar la musica de fondo del juego, si el valor de musica es null, se asignará el del área actual
	    CONSEGUIR_DINERO, // al jugador le dan dinero
	    RESTAR_DINERO, // al jugador le quitan dinero por una compra o lo k sea
	    PESCANDO, // reproducir la animacion del jugador pescando
	    BALANCEANDOSE,// poner el estado del jugador para balancearse
	    ANDANDO,// poner el estado del jugador para andar
	    ABRIR_GRUPO_ACCIONES_ALEATORIAS, // para establecer una lista de acciones, de las cuales, sólo se ejecutarán unas en concreto, dependiendo de la probabilidad
	    SET_PROBABILIDAD,
	    CERRAR_GRUPO_ACCIONES_ALEATORIAS,
	    IF,
	    ELSE,
	    ENDIF,
	    EMPEZAR_A_LLOVER,
	    DEJAR_DE_LLOVER,
	    MARCAR_GRUPO_DE_ACCIONES_NO_COMPLETADO, //para los grupos de acciones no infinitos, que por alguna razón, se quiera volver a repetir
	    GUARDAR_PARTIDA //le sale al jugador la interfaz para guardar la partida
	}

	

	public Accion(TIPOS_DE_ACCIONES tipo, boolean se_realiza_siempre, Area area, int x_aparicion, int y_aparicion,
			String texto, Objeto objeto, int cantidad, NpcCombate combate,
			List<RoyalmonSalvaje> royalmonsSalvajes, @Range(min = 1, max = 100) int probabilidadRoyalmonSalvaje,
			@Range(min = 1, max = 4) int direccion, int casillas, ObjetoArea objetoArea, List<Objeto> objetosTienda) {
		super();
		this.tipo = tipo;
		this.se_realiza_siempre = se_realiza_siempre;
		this.area = area;
		this.x_aparicion = x_aparicion;
		this.y_aparicion = y_aparicion;
		this.texto = texto;
		this.objeto = objeto;
		this.cantidad = cantidad;
		this.combate = combate;
		this.royalmonsSalvajes = royalmonsSalvajes;
		this.probabilidadRoyalmonSalvaje = probabilidadRoyalmonSalvaje;
		this.direccion = direccion;
		this.casillas = casillas;
		this.objetoArea = objetoArea;
		this.objetosTienda = objetosTienda;
		this.left_value = 0;
		this.top_value = 0;
	}
	
	public Accion() {
		
	}

	public Accion(float segundosEspera) {
		this.tipo = TIPOS_DE_ACCIONES.ESPERAR;
		this.segundosEspera = segundosEspera;
	}

	// NO_ANDAR ANDAR GESTIONAR_ROYALMONS RESTAURAR_VIDA_ROYALMONS
	public Accion(TIPOS_DE_ACCIONES tipo) {
		this.tipo = tipo;
	}

	public Accion(Npc npc, int direccion, int casillas, int orden) {
		this.tipo = TIPOS_DE_ACCIONES.MOVER_NPC;
		this.npc = npc;
		this.direccion = direccion;
		this.casillas = casillas;
		this.orden = orden;
	}
	
	public Accion(Npc npc, int direccion, int casillas, int orden, boolean kk) {
		this.tipo = TIPOS_DE_ACCIONES.MOVER_NPC;
		this.npc = npc;
		this.direccion = direccion;
		this.casillas = casillas;
		this.orden = orden;
	}
	
	public Accion(int direccion, int casillas, int orden) {
		this.tipo = TIPOS_DE_ACCIONES.MOVER_JUGADOR;
		this.direccion = direccion;
		this.casillas = casillas;
		this.orden = orden;
	}

	public Accion(Area area, int x_aparicion, int y_aparicion, int direccion) {
		this.tipo = TIPOS_DE_ACCIONES.CAMBIAR_AREA;
		this.area = area;
		this.x_aparicion = x_aparicion;
		this.y_aparicion = y_aparicion;
		this.direccion = direccion;
	}

	public Accion(Area area, Npc npc, int x_aparicion, int y_aparicion, int direccion) {
		this.tipo = TIPOS_DE_ACCIONES.CAMBIAR_AREA_NPC;
		this.area = area;
		this.npc = npc;
		this.x_aparicion = x_aparicion;
		this.y_aparicion = y_aparicion;
		this.direccion = direccion;
	}

	public Accion(ObjetoArea objetoArea, boolean visible) {
		this.tipo = !visible ? TIPOS_DE_ACCIONES.DESAPARECER_OBJETO_MAPA : TIPOS_DE_ACCIONES.APARECER_OBJETO_MAPA;
		this.objetoArea = objetoArea;
	}
	
	public Accion(ObjetoArea objetoArea, float left, float top, float duracion) {
		this.tipo = TIPOS_DE_ACCIONES.MOVER_OBJETO_MAPA;
		this.objetoArea = objetoArea;
		this.left_value = left;
		this.top_value = top;
		this.segundosEspera = duracion;
	}

	public Accion(String texto, Npc npc) {
		this.tipo = TIPOS_DE_ACCIONES.TEXTO;
		this.texto = texto;
		this.npc = npc;
	}
	
	public Accion(String texto, String keyConfirm, Npc npc) {
		this.tipo = TIPOS_DE_ACCIONES.TEXTO_CONFIRMAR;
		this.texto = texto;
		this.keyConfirm = keyConfirm;
		this.npc = npc;
	}
	
	private Accion(TIPOS_DE_ACCIONES tipo, Objeto objeto, int cantidad) {
        this.tipo = tipo;
        this.objeto = objeto;
        this.cantidad = cantidad;
    }

	public Accion(NpcCombate npcCombate) {
		this.tipo = TIPOS_DE_ACCIONES.ENTRAR_BATALLA;
		this.combate = npcCombate;
	}
	
	public Accion(List<RoyalmonSalvaje> royalmonsSalvajes, @Range(min = 1, max = 100) int probabilidadRoyalmonSalvaje, String musica, int imagen_fondo) {
		this.tipo = TIPOS_DE_ACCIONES.APARICION_ROYALMON_SALVAJE;
		this.probabilidadRoyalmonSalvaje = probabilidadRoyalmonSalvaje;
		this.royalmonsSalvajes = royalmonsSalvajes;
		this.se_realiza_siempre = true;
		this.setMusica(musica);
		this.imagen_fondo = imagen_fondo;
	}

	public Accion(TIPOS_DE_ACCIONES exclamacionNpc, Npc npc) {
		this.tipo = TIPOS_DE_ACCIONES.EXCLAMACION_NPC;
		this.npc = npc;
	}

	public Accion(Npc npc) {
		this.tipo = TIPOS_DE_ACCIONES.NPC_ACERCARSE_A_JUGADOR;
		this.npc = npc;
	}

	public Accion(List<Objeto> objetosTienda) {
		this.tipo = TIPOS_DE_ACCIONES.MOSTRAR_TIENDA;
		this.objetosTienda = objetosTienda;
	}
	
	public Accion(Royalmon royalmon) {
		this.tipo = TIPOS_DE_ACCIONES.MOSTRAR_ROYALMON_PANTALLA;
		this.royalmon = royalmon;
	}
	
	public Accion(TIPOS_DE_ACCIONES tipo, Royalmon royalmon, String texto) {
		this.tipo = tipo;
		this.royalmon = royalmon;
		this.texto = texto;
	}
	
	/*
	 * Dos formatos tendrá el string:
	 * Si el npc es royalmon, contendrá el nombre clave de la propiedad 'animaciones'
	 * Si el npc es un personaje, tendrá los keyframes apropiados en el formato [fila,col,flip]|[fila,col,flip]
	*/
	public Accion(Npc npc, String animacion, float segundos) {
		this.tipo = TIPOS_DE_ACCIONES.REALIZAR_ANIMACION_NPC;
		this.npc = npc;
		this.animacion = animacion;
		this.segundosEspera = segundos;
	}
	
	// npcs de tipo royalmon
	public Accion(Npc npc, String animacion) {
		this.tipo = TIPOS_DE_ACCIONES.REALIZAR_ANIMACION_NPC;
		this.npc = npc;
		this.animacion = animacion;
	}
	
	public Accion(RoyalmonExistente royalmonExistente) {
		this.tipo = TIPOS_DE_ACCIONES.OBTENER_ROYALMON_EXISTENTE;
		this.royalmonExistente = royalmonExistente;
	}
	
	public Accion(String nombre_var_global, Object valor_var_global, boolean kk) {
		this.tipo = TIPOS_DE_ACCIONES.SET_VAR_GLOBAL;
		this.setNombre_var_global(nombre_var_global);
		this.setValor_var_global(valor_var_global.toString());
	}

	public Accion(TIPOS_DE_ACCIONES tipo_sonido, String audio, boolean kk) {
		this.tipo = tipo_sonido;
		this.musica = audio;
	}
	
	


	public Accion(TIPOS_DE_ACCIONES tipo, int cantidad) {
		this.tipo = tipo;
		this.cantidad = cantidad;
	}




	@Id
	@GeneratedValue
	private int id_accion;
	
	
	private TIPOS_DE_ACCIONES tipo;
	
	private float segundosEspera;
	
	private boolean se_realiza_siempre;
	
	private String musica;
	
	
	/* CAMBIAR_AREA */
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_area")
	//@JsonProperty("_area")
	private Area area;
	
	private int x_aparicion;
	
	private int y_aparicion;
	
	/****************/
	
	
	/* TEXTO */
	
	private String texto;
	
	private String keyConfirm;
	
	private String condicionConfirm;
	/****************/
	
	
	/* CONSEGUIR_OBJETO */
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_objeto")
	private Objeto objeto;
	
	private int cantidad;
	/****************/
	
	

	
	
	/* ENTRAR_BATALLA */
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_combate")
	private NpcCombate combate;
	/****************/
	
	
	/* APARICION_ROYALMON_SALVAJE */
	
	@ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "acciones_royalmones_salvajes",
            joinColumns = @JoinColumn(name = "fk_accion"),
            inverseJoinColumns = @JoinColumn(name = "fk_royalmon_salvaje")
    )
	private List<RoyalmonSalvaje> royalmonsSalvajes;
	
	@Range(min = 1, max = 100)
	private int probabilidadRoyalmonSalvaje = 1;
	
	private int imagen_fondo;
	/****************/

	
	/* MOVER_JUGADOR, MOVER_NPC, EXCLAMACION_NPC, NPC_ACERCARSE_A_JUGADOR */
	
	@Range(min = -1, max = 4)
	private int direccion = 1;
	
	private int casillas;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_npc")
	private Npc npc;
	
	private int orden;

	/*****************/
	
	
	/* DESAPARECER_OBJETO_MAPA */
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_objetoArea")
	private ObjetoArea objetoArea;
	
	
	
	/* MOVER_OBJETO_MAPA */
	private float top_value;
	
	private float left_value;
	
	/*****************/
	
	
	
	/* MOSTAR_TIENDA */
	
	@ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "acciones_objetos_tienda",
            joinColumns = @JoinColumn(name = "fk_accion"),
            inverseJoinColumns = @JoinColumn(name = "fk_objeto")
    )
	private List<Objeto> objetosTienda;
	/****************/
	
	private String animacion;
	
	private int inicialElegido;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_royalmonExistente")
	private RoyalmonExistente royalmonExistente;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_royalmon")
	private Royalmon royalmon;
	
	
	private String nombre_var_global;

	private String valor_var_global;
	
	public TIPOS_DE_ACCIONES getTipo() {
		return tipo;
	}
	public Npc getNpc() {
		return npc;
	}

	public void setNpc(Npc npc) {
		this.npc = npc;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public void setTipo(TIPOS_DE_ACCIONES tipo) {
		this.tipo = tipo;
	}
	public float getSegundosEspera() {
		return segundosEspera;
	}

	public void setSegundosEspera(float segundosEspera) {
		this.segundosEspera = segundosEspera;
	}

	public boolean isSe_realiza_siempre() {
		return se_realiza_siempre;
	}
	public void setSe_realiza_siempre(boolean se_realiza_siempre) {
		this.se_realiza_siempre = se_realiza_siempre;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public int getX_aparicion() {
		return x_aparicion;
	}
	public void setX_aparicion(int x_aparicion) {
		this.x_aparicion = x_aparicion;
	}
	public int getY_aparicion() {
		return y_aparicion;
	}
	public void setY_aparicion(int y_aparicion) {
		this.y_aparicion = y_aparicion;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Objeto getObjeto() {
		return objeto;
	}
	public void setObjeto(Objeto objeto) {
		this.objeto = objeto;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public NpcCombate getCombate() {
		return combate;
	}
	public void setCombate(NpcCombate combate) {
		this.combate = combate;
	}
	public List<RoyalmonSalvaje> getRoyalmonsSalvajes() {
		return royalmonsSalvajes;
	}
	public void setRoyalmonsSalvajes(List<RoyalmonSalvaje> royalmonsSalvajes) {
		this.royalmonsSalvajes = royalmonsSalvajes;
	}
	public int getProbabilidadRoyalmonSalvaje() {
		return probabilidadRoyalmonSalvaje;
	}
	public void setProbabilidadRoyalmonSalvaje(int probabilidadRoyalmonSalvaje) {
		this.probabilidadRoyalmonSalvaje = probabilidadRoyalmonSalvaje;
	}
	public int getImagen_fondo() {
		return imagen_fondo;
	}

	public void setImagen_fondo(int imagen_fondo) {
		this.imagen_fondo = imagen_fondo;
	}

	public int getDireccion() {
		return direccion;
	}
	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}
	public int getCasillas() {
		return casillas;
	}
	public void setCasillas(int casillas) {
		this.casillas = casillas;
	}
	public ObjetoArea getObjetoArea() {
		return objetoArea;
	}
	public void setObjetoArea(ObjetoArea objetoArea) {
		this.objetoArea = objetoArea;
	}
	public float getTop_value() {
		return top_value;
	}

	public void setTop_value(float top) {
		this.top_value = top;
	}

	public float getLeft_value() {
		return left_value;
	}

	public void setLeft_value(float left) {
		this.left_value = left;
	}

	public List<Objeto> getObjetosTienda() {
		return objetosTienda;
	}
	public void setObjetosTienda(List<Objeto> objetosTienda) {
		this.objetosTienda = objetosTienda;
	}

	public String getAnimacion() {
		return animacion;
	}

	public void setAnimacion(String animacion) {
		this.animacion = animacion;
	}

	public Royalmon getRoyalmon() {
		return royalmon;
	}

	public void setRoyalmon(Royalmon royalmon) {
		this.royalmon = royalmon;
	}

	public String getKeyConfirm() {
		return keyConfirm;
	}

	public void setKeyConfirm(String keyConfirm) {
		this.keyConfirm = keyConfirm;
	}

	public String getCondicionConfirm() {
		return condicionConfirm;
	}

	public void setCondicionConfirm(String condicionConfirm) {
		this.condicionConfirm = condicionConfirm;
	}
	
	public int getInicialElegido() {
		return inicialElegido;
	}

	public void setInicialElegido(int inicialElegido) {
		this.inicialElegido = inicialElegido;
	}

	public RoyalmonExistente getRoyalmonExistente() {
		return royalmonExistente;
	}

	public void setRoyalmonExistente(RoyalmonExistente royalmonExistente) {
		this.royalmonExistente = royalmonExistente;
	}

	public String getNombre_var_global() {
		return nombre_var_global;
	}

	public void setNombre_var_global(String nombre_var_global) {
		this.nombre_var_global = nombre_var_global;
	}

	public String getValor_var_global() {
		return valor_var_global;
	}

	public void setValor_var_global(String valor_var_global) {
		this.valor_var_global = valor_var_global;
	}

	public String getMusica() {
		return musica;
	}

	public void setMusica(String musica) {
		this.musica = musica;
	}
	
	public Accion setCondicion(String condicionConfirm) {
	    if (this.condicionConfirm == null) {
	        this.condicionConfirm = "";
	    }
	    this.condicionConfirm += "|" + condicionConfirm;
	    if (this.condicionConfirm.startsWith("|")) {
	    	this.condicionConfirm = this.condicionConfirm.substring(1);
	    }
	    
	    return this;
	}
	
	public Accion siVariableGlobal(String key, Object value, boolean igual) {
		if (value == null) {
			value = "null";
		}
		String variable = "global:" + key + (igual ? "=" : "!=") + value.toString();
		return this.setCondicion(variable);
	}
	
	public Accion siVariableGlobal(String key, Object value) {
		return siVariableGlobal(key, value, true);
	}
	
	public Accion siNoEligioInicial() {
		return siVariableGlobal("inicial", null);
	}
	
	public Accion siEligioPrimerInicial() {
		return siVariableGlobal("inicial", 1);
	}
	
	public Accion siEligioSegundoInicial() {
		return siVariableGlobal("inicial", 2);
	}
	
	public Accion siEligioTercerInicial() {
		return siVariableGlobal("inicial", 3);
	}
	
	public Accion siTieneEnSuBolsa(Objeto obj, int cantidad) {
		return setCondicion("objBolsa=" + obj.getNombre() + "=" + cantidad);
	}
	
	public Accion siTieneEnSuBolsa(Objeto obj) {
		return siTieneEnSuBolsa(obj, 1);
	}
	
	public Accion siTieneRoyalmonConAtaque(Ataque ataque) {
		return setCondicion("atq=" + ataque.getNombre());
	}
	
	public static Accion eligioPrimerInicial() {
        return Accion.setVariableGlobal("inicial", 1);
    }
	
	public static Accion eligioSegundoInicial() {
        return Accion.setVariableGlobal("inicial", 2);
    }
	
	public static Accion eligioTercerInicial() {
        return Accion.setVariableGlobal("inicial", 3);
    }
	
	// MÉTODOS A USAR PARA CREAR LAS ACCIONES
	
	public static Accion esperar(float segundosEspera) {
        return new Accion(segundosEspera);
    }
	
	public static Accion esperar(int segundosEspera) {
        return new Accion(segundosEspera);
    }
	
	public static Accion no_andar() {
        return new Accion(TIPOS_DE_ACCIONES.NO_ANDAR);
    }
	
	public static Accion andar() {
        return new Accion(TIPOS_DE_ACCIONES.ANDAR);
    }
	
	public static Accion mover_npc(Npc npc, int direccion, int casillas, int orden) {
        return new Accion(npc, direccion, casillas, orden);
    }
	
	public static Accion mover_npc(Npc npc, int direccion, int casillas) {
        return new Accion(npc, direccion, casillas, 1);
    }
	
	public static Accion mover_npc(int direccion, int casillas, int orden) {
        return new Accion(null, direccion, casillas, orden, false);
    }
	
	public static Accion mover_npc(int direccion, int casillas) {
        return new Accion(null, direccion, casillas, 1, false);
    }
	
	public static Accion mover_jugador(int direccion, int casillas, int orden) {
        return new Accion(direccion, casillas, orden);
    }
	
	public static Accion mover_jugador(int direccion, int casillas) {
        return new Accion(direccion, casillas, 1);
    }
	
	public static Accion cambiar_area(Area area, int x_aparicion, int y_aparicion, int direccion) {
        return new Accion(area, x_aparicion, y_aparicion, direccion);
    }
	
	public static Accion cambiar_area_npc(Area area, Npc npc, int x_aparicion, int y_aparicion, int direccion) {
        return new Accion(area, npc, x_aparicion, y_aparicion, direccion);
    }
	
	public static Accion cambiar_area_npc(Area area, int x_aparicion, int y_aparicion, int direccion) {
        return new Accion(area, null, x_aparicion, y_aparicion, direccion);
    }

	public static Accion mostrarObjetoArea(ObjetoArea objetoArea) {
		return new Accion(objetoArea, true);
	}
	
	public static Accion ocultarObjetoArea(ObjetoArea objetoArea) {
		return new Accion(objetoArea, false);
	}
	
	public static Accion moverObjetoMapa(ObjetoArea objetoArea, float left, float top, float duracion) {
		return new Accion(objetoArea, left, top, duracion);
	}
	
	public static Accion hablar(Npc npc, String texto) {
		return new Accion(texto, npc);
	}
	
	public static Accion hablar(String texto) {
		return new Accion(texto, null);
	}
	
	public static Accion mensaje(String texto) {
		return new Accion(texto, null);
	}
	
	public static Accion pregunta(Npc npc, String texto, String keyConfirm) {
		return new Accion(texto, keyConfirm, npc);
	}
	
	public static Accion pregunta(String texto, String keyConfirm) {
		return new Accion(texto, keyConfirm, null);
	}
	
	public static Accion conseguirObjeto(Objeto objeto, int cantidad) {
        return new Accion(TIPOS_DE_ACCIONES.CONSEGUIR_OBJETO, objeto, cantidad);
    }
	
	public static Accion conseguirObjeto(Objeto objeto) {
        return new Accion(TIPOS_DE_ACCIONES.CONSEGUIR_OBJETO, objeto, 1);
    }

    public static Accion quitarObjeto(Objeto objeto, int cantidad) {
        return new Accion(TIPOS_DE_ACCIONES.QUITAR_OBJETO, objeto, cantidad);
    }
    
    public static Accion quitarObjeto(Objeto objeto) {
        return new Accion(TIPOS_DE_ACCIONES.QUITAR_OBJETO, objeto, 1);
    }
	
    public static Accion conseguirMedalla() {
        return new Accion(TIPOS_DE_ACCIONES.CONSEGUIR_MEDALLA);
    }
    
    public static Accion batalla(NpcCombate npcCombate) {
    	return new Accion(npcCombate);
    }
    
    public static Accion aparicionRoyalmonsSalvajes(List<RoyalmonSalvaje> royalmonsSalvajes, @Range(min = 1, max = 100) int probabilidadRoyalmonSalvaje, String musica, int imagen_fondo) {
    	return new Accion(royalmonsSalvajes, probabilidadRoyalmonSalvaje, musica, imagen_fondo);
    }
    
    public static Accion exclamacionNpc(Npc npc) {
    	return new Accion(TIPOS_DE_ACCIONES.EXCLAMACION_NPC, npc);
    }
    
    public static Accion exclamacionNpc() {
    	return new Accion(TIPOS_DE_ACCIONES.EXCLAMACION_NPC, null);
    }
    
    public static Accion npcAcercarseJugador(Npc npc) {
    	return new Accion(npc);
    }
    
    public static Accion npcAcercarseJugador() {
    	return new Accion(TIPOS_DE_ACCIONES.NPC_ACERCARSE_A_JUGADOR);
    }
    
    public static Accion mostrarTienda(List<Objeto> objetosTienda) {
    	return new Accion(objetosTienda);
    }
    
    public static Accion gestionarRoyalmons() {
    	return new Accion(TIPOS_DE_ACCIONES.GESTIONAR_ROYALMONS);
    }
    
    public static Accion curarEquipo() {
    	return new Accion(TIPOS_DE_ACCIONES.RESTAURAR_VIDA_ROYALMONS);
    }
    
    public static Accion animacionNpc(Npc npc, String animacion, float segundos) {
    	return new Accion(npc, animacion, segundos);
    }
    
    public static Accion animacionNpc(String animacion, float segundos) {
    	return new Accion(null, animacion, segundos);
    }
    
    public static Accion mostrarRoyalmonAcomp() {
    	return new Accion(TIPOS_DE_ACCIONES.MOSTRAR_ROYALMON_ACOMP);
    }
    
    public static Accion ocultarRoyalmonAcomp() {
    	return new Accion(TIPOS_DE_ACCIONES.OCULTAR_ROYALMON_ACOMP);
    }
    
    public static Accion mostrarRoyalmonPantalla(Royalmon royalmon) {
    	return new Accion(royalmon);
    }
    
    public static Accion ocultarRoyalmonPantalla() {
        return new Accion(TIPOS_DE_ACCIONES.OCULTAR_ROYALMON_PANTALLA);
    }
	
    public static Accion obtenerRoyalmonExistente(RoyalmonExistente royalmonExistente) {
        return new Accion(royalmonExistente);
    }
    
    public static Accion setVariableGlobal(String nombre_var_global, Object valor_var_global) {
        return new Accion(nombre_var_global, valor_var_global, false);
    }
    
    public static Accion reproducirSonido(String musica) {
    	return new Accion(TIPOS_DE_ACCIONES.REPRODUCIR_SONIDO, musica, false);
    }
    
    public static Accion reproducirSonidoRoyalmon(Royalmon r, String tag) {
        return new Accion(TIPOS_DE_ACCIONES.REPRODUCIR_SONIDO_ROYALMON, r, tag);
    }
	
	public static Accion reproducirSonidoRoyalmon(Royalmon r) {
        return Accion.reproducirSonidoRoyalmon(r, "saludo");
    }
    
    public static Accion cambiarMusica(String musica) {
    	return new Accion(TIPOS_DE_ACCIONES.CAMBIAR_MUSICA, musica, false);
    }
    
    public static Accion cambiarMusicaArea() {
    	return new Accion(TIPOS_DE_ACCIONES.CAMBIAR_MUSICA, null, false);
    }
    
    public static Accion sumarDinero(int cantidad) {
        return new Accion(TIPOS_DE_ACCIONES.CONSEGUIR_DINERO, cantidad);
    }
    
    public static Accion restarDinero(int cantidad) {
        return new Accion(TIPOS_DE_ACCIONES.RESTAR_DINERO, cantidad);
    }
    
    public static Accion pescando() {
    	return new Accion(TIPOS_DE_ACCIONES.PESCANDO);
    }
    
    public static Accion balanceandose() {
    	return new Accion(TIPOS_DE_ACCIONES.BALANCEANDOSE);
    }
    
    public static Accion andando() {
    	return new Accion(TIPOS_DE_ACCIONES.ANDANDO);
    }
    
    public static Accion BEGIN_RANDOM_SELECTION() {
    	return new Accion(TIPOS_DE_ACCIONES.ABRIR_GRUPO_ACCIONES_ALEATORIAS);
    }
    
    public static Accion SET_PROBABILITY(int probabilidad) {
        return new Accion(TIPOS_DE_ACCIONES.SET_PROBABILIDAD, probabilidad);
    }
    
    public static Accion END_RANDOM_SELECTION() {
    	return new Accion(TIPOS_DE_ACCIONES.CERRAR_GRUPO_ACCIONES_ALEATORIAS);
    }
    
	public static Accion IF() {
        return new Accion(TIPOS_DE_ACCIONES.IF);
    }
	
	public static Accion ELSE() {
        return new Accion(TIPOS_DE_ACCIONES.ELSE);
    }
	
	public static Accion ENDIF() {
        return new Accion(TIPOS_DE_ACCIONES.ENDIF);
    }
	
	public static Accion empezarALlover() {
		return new Accion(TIPOS_DE_ACCIONES.EMPEZAR_A_LLOVER);
	}
	
	public static Accion dejarDeLlover() {
		return new Accion(TIPOS_DE_ACCIONES.DEJAR_DE_LLOVER);
	}
    
    public static Accion marcarGrupoAccionesComoNoRealizado() {
        return new Accion(TIPOS_DE_ACCIONES.MARCAR_GRUPO_DE_ACCIONES_NO_COMPLETADO);
    }
    
    public static Accion guardarPartida() {
        return new Accion(TIPOS_DE_ACCIONES.GUARDAR_PARTIDA);
    }
    
	


	
}
