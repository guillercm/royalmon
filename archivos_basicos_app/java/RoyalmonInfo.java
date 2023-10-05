package com.joyfe.daw.des.response;

import java.util.List;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity(name = "royalmones_info")
public class RoyalmonInfo {
	

	
	public RoyalmonInfo(String nombre, String descripcion,@Range(min = 1, max = 8) float stat_ataque,
			@Range(min = 1, max = 8) float stat_defensa, @Range(min = 1, max = 8) float stat_vida, Tipo tipo_1, Tipo tipo_2, boolean es_legendario,
			int probabilidad_macho, String nombre_carpeta, int num_royaldex, List<AnimacionesRoyalmon> animaciones,
			List<RoyalmonAtaque> ataquesQueAprende, int experiencia_base) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.stat_ataque = stat_ataque;
		this.stat_defensa = stat_defensa;
		this.stat_vida = stat_vida;
		this.tipo_1 = tipo_1;
		this.tipo_2 = tipo_2;
		this.es_legendario = es_legendario;
		this.probabilidad_macho = probabilidad_macho;
		this.nombre_carpeta = nombre_carpeta;
		this.num_royaldex = num_royaldex;
		this.animaciones = animaciones;
		this.ataquesQueAprende = ataquesQueAprende;
		this.experiencia_base = experiencia_base;
		//this.evoluciones = new ArrayList<>(Arrays.asList(evolucion));
	}
	
	public RoyalmonInfo(String nombre, String descripcion, @Range(min = 1, max = 8) float stat_ataque,
			@Range(min = 1, max = 8) float stat_defensa, @Range(min = 1, max = 8) float stat_vida,Tipo tipo_1, Tipo tipo_2, boolean es_legendario,
			int probabilidad_macho, String nombre_carpeta, int num_royaldex, List<AnimacionesRoyalmon> animaciones,
			List<RoyalmonAtaque> ataquesQueAprende, int experiencia_base, List<Evolucion> evoluciones) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.stat_ataque = stat_ataque;
		this.stat_defensa = stat_defensa;
		this.stat_vida = stat_vida;
		this.tipo_1 = tipo_1;
		this.tipo_2 = tipo_2;
		this.es_legendario = es_legendario;
		this.probabilidad_macho = probabilidad_macho;
		this.nombre_carpeta = nombre_carpeta;
		this.num_royaldex = num_royaldex;
		this.animaciones = animaciones;
		this.ataquesQueAprende = ataquesQueAprende;
		this.experiencia_base = experiencia_base;
		this.evoluciones = evoluciones;
	}
	
	public RoyalmonInfo() {}

	@GeneratedValue
	@Id
	@JsonProperty("id_royalmon")
	private Long id_royalmon_info;
	
	private String nombre;
	
    private String descripcion;
    
    @Range(min = 1, max = 8)
    private float stat_ataque;
    
    @Range(min = 1, max = 8)
    private float stat_defensa;
    
    @Range(min = 1, max = 8)
    private float stat_vida;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "fk_tipo_1")
    private Tipo tipo_1;
    
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "fk_tipo_2")
    private Tipo tipo_2;
    
    private boolean es_legendario;
    
    private int probabilidad_macho;
    
    private String nombre_carpeta;
    
    private int num_royaldex;
    
	@ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "royalmones_animaciones",
            joinColumns = @JoinColumn(name = "fk_royalmon"),
            inverseJoinColumns = @JoinColumn(name = "fk_animacion")
    )
	private List<AnimacionesRoyalmon> animaciones;
	
	@ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "royalmones_evoluciones",
            joinColumns = @JoinColumn(name = "fk_royalmon"),
            inverseJoinColumns = @JoinColumn(name = "fk_evolucion")
    )
	private List<Evolucion> evoluciones;
	
	@ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "royalmones_royalmones_ataques",
            joinColumns = @JoinColumn(name = "fk_royalmon"),
            inverseJoinColumns = @JoinColumn(name = "fk_royalmon_ataque")
    )
	private List<RoyalmonAtaque> ataquesQueAprende;
	
	private int experiencia_base;
	
	private String sonidos;

	public Long getIdRoyalmonInfo() {
		return id_royalmon_info;
	}

	public void setIdRoyalmonInfo(Long id_royalmon) {
		this.id_royalmon_info = id_royalmon;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	public float getStat_ataque() {
		return stat_ataque;
	}

	public void setStat_ataque(float stat_ataque) {
		this.stat_ataque = stat_ataque;
	}

	public float getStat_defensa() {
		return stat_defensa;
	}

	public void setStat_defensa(float stat_defensa) {
		this.stat_defensa = stat_defensa;
	}

	public float getStat_vida() {
		return stat_vida;
	}

	public void setStat_vida(float stat_vida) {
		this.stat_vida = stat_vida;
	}

	public Tipo getTipo_1() {
		return tipo_1;
	}

	public void setTipo_1(Tipo tipo_1) {
		this.tipo_1 = tipo_1;
	}

	public Tipo getTipo_2() {
		return tipo_2;
	}

	public void setTipo_2(Tipo tipo_2) {
		this.tipo_2 = tipo_2;
	}

	public boolean isEs_legendario() {
		return es_legendario;
	}

	public void setEs_legendario(boolean es_legendario) {
		this.es_legendario = es_legendario;
	}

	public int getProbabilidad_macho() {
		return probabilidad_macho;
	}

	public void setProbabilidad_macho(int probabilidad_macho) {
		this.probabilidad_macho = probabilidad_macho;
	}

	public String getNombre_carpeta() {
		return nombre_carpeta;
	}

	public void setNombre_carpeta(String nombre_carpeta) {
		this.nombre_carpeta = nombre_carpeta;
	}

	public int getNum_royaldex() {
		return num_royaldex;
	}

	public void setNum_royaldex(int num_royaldex) {
		this.num_royaldex = num_royaldex;
	}

	public List<AnimacionesRoyalmon> getAnimaciones() {
		return animaciones;
	}

	public void setAnimaciones(List<AnimacionesRoyalmon> animaciones) {
		this.animaciones = animaciones;
	}

	public List<Evolucion> getEvoluciones() {
		return evoluciones;
	}

	public void setEvoluciones(List<Evolucion> evoluciones) {
		this.evoluciones = evoluciones;
	}

	public List<RoyalmonAtaque> getAtaquesQueAprende() {
		return ataquesQueAprende;
	}

	public void setAtaquesQueAprende(List<RoyalmonAtaque> ataquesQueAprende) {
		this.ataquesQueAprende = ataquesQueAprende;
	}

	public int getExperiencia_base() {
		return experiencia_base;
	}

	public void setExperiencia_base(int experiencia_base) {
		this.experiencia_base = experiencia_base;
	}

	public String getSonidos() {
		return sonidos;
	}

	public void setSonidos(String sonidos) {
		this.sonidos = sonidos;
	}
	
}
