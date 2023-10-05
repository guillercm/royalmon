package com.joyfe.daw.des.rest.dao;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.joyfe.daw.des.util.Utilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyfe.daw.des.response.Accion;
import com.joyfe.daw.des.response.Accion.TIPOS_DE_ACCIONES;
import com.joyfe.daw.des.response.AnimacionesRoyalmon;
import com.joyfe.daw.des.response.Royalmon;
import com.joyfe.daw.des.response.RoyalmonAtaque;
import com.joyfe.daw.des.response.RoyalmonExistente;
import com.joyfe.daw.des.response.RoyalmonInfo;
import com.joyfe.daw.des.response.RoyalmonSalvaje;
import com.joyfe.daw.des.response.Tipo;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;
import com.joyfe.daw.des.response.AnimacionesRoyalmon.TIPOS_DE_ANIMACIONES;
import com.joyfe.daw.des.response.Area;
import com.joyfe.daw.des.response.AreaInfo;
import com.joyfe.daw.des.response.AreaInfo.TIPOS_DE_AREAS;
import com.joyfe.daw.des.response.Ataque.TIPOS_ATAQUE;
import com.joyfe.daw.des.response.AssetMapa;
import com.joyfe.daw.des.response.AssetNpc;
import com.joyfe.daw.des.response.Ataque;
import com.joyfe.daw.des.response.Evolucion;
import com.joyfe.daw.des.response.Evolucion.GENERO;
import com.joyfe.daw.des.response.GrupoDeAcciones;
import com.joyfe.daw.des.response.Npc;
import com.joyfe.daw.des.response.NpcCombate;
import com.joyfe.daw.des.response.Objeto;
import com.joyfe.daw.des.response.Objeto.TIPOS;
import com.joyfe.daw.des.response.ObjetoArea;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @PersistenceContext
    private EntityManager em;
	private List<AnimacionesRoyalmon> listaAnimaciones;
	private List<AssetMapa> assetsMapa;
	
	@Autowired
	private Environment env; 
	
	private Boolean createData = false;  
 
	int d_null = -1;
	int arriba = 0;
	int	abajo = 1;
	int izquierda = 2; 
	int	derecha = 3;
	
	// NPCs
	
	Npc npc_miguel = new Npc("Miguel", 12, 5, izquierda, 
			new AssetNpc(30, 0, 3), false);
	
	Npc npc_nicolas = new Npc("Nicolás", 7, 3, abajo, 
		    new AssetNpc(5, 9, 2), true);

	Npc npc_bartolo = new Npc("Bartolo", 9, 7, derecha, 
			new AssetNpc(21, 25, 2,		
					/*fila_sprite_vs*/1,
					/*columna_sprite_vs*/0,
					/*fila_sprite_batalla*/1,
					/*columna_sprite_batalla*/3
					), true);

	Npc npc_bebe_dragon = new Npc(8, 3, abajo, 
	    null, true);

	Npc npc_arquera = new Npc(12, 3, izquierda, 
	    null, true);

	Npc npc_marta = new Npc("Marta", 5, 2, abajo, 
	    new AssetNpc(21, 22, 3), false);
	
	List<Npc> npcs = Arrays.asList(
		    npc_miguel,
		    npc_nicolas,
		    npc_bartolo,
		    npc_bebe_dragon,
		    npc_arquera,
		    npc_marta
		);
	
	Npc npc_enfermera = new Npc("Enfermera", 2, 0, abajo, new AssetNpc(21, 6, 2), true);
	Npc npc_currante = new Npc("Currante", 4, 3, izquierda, new AssetNpc(13, 7, 3), false);
	Npc npc_vendedor = new Npc("Vendedor", 0, 0, abajo, new AssetNpc(21, 2, 2), true);

	List<Npc> npcsCentroRoyalmon = Arrays.asList(
	    npc_enfermera,
	    npc_currante,
	    npc_vendedor
	);
	
	
	// OBJETOS BOLSA
	
	Objeto obj_canna_pescar = new Objeto(50, "Caña de pescar", "Sirve para poder conseguir objetos o poder capturar royalmons salvajes en el agua, eso sí, cuidado con ella porque se puede romper...", "caña");

	Objeto obj_deportivas = new Objeto(0, "Deportivas", "Sirve para poder desplazarse más rápido que andando", "deportivas");

	Objeto obj_bicicleta = new Objeto(0, "Bicicleta", "Sirve para poder desplazarse más rápido que con las deportivas", "bicicleta");

	Objeto obj_royal_ball = new Objeto(TIPOS.royalball, 15, "Royal Ball", "Sirve para poder capturar royalmons", "3", 30, 0, 0);

	Objeto obj_super_ball = new Objeto(TIPOS.royalball, 30, "Super Ball", "Sirve para poder capturar royalmons, pero tiene un mayor índice de captura que la Royal Ball", "2", 50, 0, 0);

	Objeto obj_ultra_ball = new Objeto(TIPOS.royalball, 50, "Ultra Ball", "Sirve para poder capturar royalmons, pero tiene un mayor índice de captura que la Super Ball", "1", 85, 0, 0);

	Objeto obj_master_ball = new Objeto(TIPOS.royalball, 200, "Master Ball", "Permite capturar nuevos Royalmons, tiene un índice de captura del 100%, nunca falla", "0", 100, 0, 0);

	Objeto obj_curacion_nvl1 = new Objeto(TIPOS.curacion, 5, "Curación Nvl1", "Cura 20 puntos de vida a un Royalmon", "curacion", 1, 18, 0);

	Objeto obj_curacion_nvl2 = new Objeto(TIPOS.curacion, 10, "Curación Nvl2", "Cura 50 puntos de vida a un Royalmon", "curacion", 1, 40, 0);

	Objeto obj_curacion_nvl3 = new Objeto(TIPOS.curacion, 20, "Curación Nvl3", "Cura 100 puntos de vida a un Royalmon", "curacion", 1, 120, 0);

	Objeto obj_pez = new Objeto(TIPOS.curacion, 20, "Pez", "Cura 30 puntos de vida a un Royalmon", "pez", 1, 30, 0);

	Objeto obj_flechas = new Objeto(TIPOS.danno, 5, "Flechas", "Lanza unas flechas", "arrows", 1, 0, 25);

	Objeto obj_bola_fuego = new Objeto(TIPOS.danno, 10, "Bola de Fuego", "Lanza una bola de fuego que causa 10 puntos de daño", "fireball", 1, 0, 60);

	Objeto obj_cohete = new Objeto(TIPOS.danno, 50, "Cohete", "Lanza un cohete que causa 50 puntos de daño en área", "rocket", 1, 0, 100);

	Objeto obj_flechazo = new Objeto(TIPOS.mt, 10, "Flechazo", "Enseña el ataque Flechazo a un Royalmon", "normal", 1, 0, 0);

	Objeto obj_roca_afilada = new Objeto(TIPOS.mt, 10, "Roca Afilada", "Enseña el ataque Roca Afilada a un Royalmon", "roca", 1, 0, 0);

	Objeto obj_vuelo = new Objeto(TIPOS.mt, 15, "Vuelo", "Enseña el ataque Vuelo a un Royalmon", "volador", 1, 0, 0);

	Objeto obj_corte = new Objeto(TIPOS.mt, 20, "Corte", "Enseña el ataque Corte a un Royalmon", "normal", 1, 0, 0);

	Objeto obj_alas_magicas = new Objeto(TIPOS.evolutivo, 20, "Alas mágicas", "Para evolucionar una arquera a un bebé dragón", "alas", 1, 0, 0);

	List<Objeto> objetos = Arrays.asList(
	    obj_canna_pescar,
	    obj_deportivas,
	    obj_bicicleta,
	    obj_royal_ball,
	    obj_super_ball,
	    obj_ultra_ball,
	    obj_master_ball,
	    obj_curacion_nvl1,
	    obj_curacion_nvl2,
	    obj_curacion_nvl3,
	    obj_pez,
	    obj_flechas,
	    obj_bola_fuego,
	    obj_cohete,
	    obj_flechazo,
	    obj_roca_afilada,
	    obj_vuelo,
	    obj_corte,
	    obj_alas_magicas
	);
	
	
	// TIPOS
	
	Tipo tipo_normal = new Tipo("normal", "#A8A77A");
	Tipo tipo_lucha = new Tipo("lucha", "#C22E28");
	Tipo tipo_volador = new Tipo("volador", "#A98FF3");
	Tipo tipo_veneno = new Tipo("veneno", "#A33EA1");
	Tipo tipo_tierra = new Tipo("tierra", "#E2BF65");
	Tipo tipo_roca = new Tipo("roca", "#B6A136");
	Tipo tipo_bicho = new Tipo("bicho", "#A6B91A");
	Tipo tipo_fantasma = new Tipo("fantasma", "#735797");
	Tipo tipo_acero = new Tipo("acero", "#B7B7CE");
	Tipo tipo_fuego = new Tipo("fuego", "#EE8130");
	Tipo tipo_agua = new Tipo("agua", "#6390F0");
	Tipo tipo_planta = new Tipo("planta", "#7AC74C");
	Tipo tipo_electrico = new Tipo("eléctrico", "#F7D02C");
	Tipo tipo_psiquico = new Tipo("psíquico", "#F95587");
	Tipo tipo_hielo = new Tipo("hielo", "#96D9D6");
	Tipo tipo_dragon = new Tipo("dragón", "#6F35FC");
	Tipo tipo_hada = new Tipo("hada", "#D685AD");
	Tipo tipo_siniestro = new Tipo("siniestro", "#705746");

	Tipo[] tipos = new Tipo[] {
	    tipo_normal,
	    tipo_lucha,
	    tipo_volador,
	    tipo_veneno,
	    tipo_tierra,
	    tipo_roca,
	    tipo_bicho,
	    tipo_fantasma,
	    tipo_acero,
	    tipo_fuego,
	    tipo_agua,
	    tipo_planta,
	    tipo_electrico,
	    tipo_psiquico,
	    tipo_hielo,
	    tipo_dragon,
	    tipo_hada,
	    tipo_siniestro
	};

	
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    	if (!createData) {
    		System.err.println("LOS DATOS NO SE CARGARÁN");
        	return;
    	}
    	System.out.println();
        System.out.println("CARGA DE DATOS: ");
        //System.out.println(em == null ? "NULL" : "NO NULL");
        initBBDD();
        // En las acciones de mostrar mensajes, 
        // se puede mencionar el nombre del jugador, usando la palabra clave nombreJug
    }
    
	public void initBBDD() {
		Utilities.setEntityManager(em);
		initTipos();
		initObjetos();
		initAtaques();
		initRoyalmons();
		npc_bebe_dragon.setRoyalmon(getRoyalmon("Bebé dragón"));
		npc_arquera.setRoyalmon(getRoyalmon("Arquera"));
		initAreas();
	}
	
	public static Royalmon getRoyalmon(String nombre) {
		RoyalmonInfo r = Utilities.find(RoyalmonInfo.class, true, "nombre", nombre);
		if (r == null) return null;
		return Utilities.find(Royalmon.class, true, "id_royalmon", r.getIdRoyalmonInfo().toString());
	}
	
	public Tipo getTipo(String nombre) {
		return Utilities.find(Tipo.class, true, "nombre", nombre);
	}
	
	public Ataque getAtaque(String nombre) {
		return Utilities.find(Ataque.class, true, "nombre", nombre);
	}
	
	public Objeto getObjeto(String nombre) {
		return Utilities.find(Objeto.class, true, "nombre", nombre);
	}
	
	public Npc getNpc(String nombre) {
		return Utilities.find(Npc.class, true, "nombre", nombre);
	}
	
	
	public void initObjetos() {
		System.out.println("Insertando objetos...");
		
		for (Objeto obj : objetos) {
			em.persist(obj);
		}
	}
	
	public void initRoyalmons() {
		RoyalmonInfo[] royalmons = getJson("royalmons", RoyalmonInfo[].class);
		if (royalmons == null) {
			System.err.println("No se encuentra el json del los royalmons");
			return;
		}
		System.out.println("Insertando royalmons...");
		int num_royadex = 1;
		for (RoyalmonInfo r : royalmons) {
			r.setIdRoyalmonInfo(null);
			r.setTipo_1(getTipo(r.getTipo_1().getNombre()));
			r.setTipo_2(getTipo(r.getTipo_2().getNombre()));
			r.setNum_royaldex(num_royadex);
			num_royadex++;
			for (AnimacionesRoyalmon a : r.getAnimaciones()) {
				a.setId_royalmon_animacion(null);
			}
			for (RoyalmonAtaque a : r.getAtaquesQueAprende()) {
				a.setId_royalmon_ataque(null);
				a.setAtaque(getAtaque(a.getAtaque().getNombre()));
				if (a.getObjeto() != null) {
					a.setObjeto(getObjeto(a.getObjeto().getNombre()));
				}
			}
			for (Evolucion ev : r.getEvoluciones()) {
				ev.setId_evolucion(null);
				if (ev.getObjeto() != null) {
					ev.setObjeto(getObjeto(ev.getObjeto().getNombre()));
				}
				ev.getRoyalmon_evolucion().setId_royalmon(null);
			}
		}
		boolean todosInsertados = false;
		while (!todosInsertados) {
			todosInsertados = true;
			
		    for (RoyalmonInfo r : royalmons) {
		    	if (r.getIdRoyalmonInfo() != null) continue;
		    	boolean insertar = true;
		        for (Evolucion ev : r.getEvoluciones()) {
		        	if (ev.getRoyalmon_evolucion().getId_royalmon() == null) {
		        		Royalmon rEv = getRoyalmon(ev.getRoyalmon_evolucion().getNombre());
		        		if (rEv == null) {
		        			todosInsertados = false;
		        			insertar = false;
		        		} else {
		        			ev.setRoyalmon_evolucion(rEv);
		        		}
		        	}
		        }
		        if (insertar) {
	        		em.persist(new Royalmon());
		        	em.persist(r);
		        }
		    }
		}
		System.out.println("*************");
	}
	
	Ataque atq_corte = new Ataque("Corte", "Este ataque es muy dañino xd", 20, 3, tipo_normal);
	public void initAtaques() {
		System.out.println("Insertando ataques...");
		// https://www.pkmnstats.com/dex/quinta/movimientos/
		List<Ataque> ataques = Arrays.asList(
			new Ataque("Placaje", "Este ataque es muy dañino xd", 30,3, tipo_normal),
			atq_corte, 
			new Ataque("Flechazo", "Una flecha k te mata guau xd", 5, 4, tipo_bicho),
			new Ataque("Psíquico", "Te controla la mente para bajarte vida", 5, 4, tipo_psiquico),
			new Ataque("Ascuas", "Esta muy quemado este ataque", 20, 4, tipo_fuego),
			new Ataque("Vuelo", "Con este ataque podrás ir volando a otras áreas", 10, 4, tipo_volador),
			new Ataque("Malicioso", "Maldice al enemigo para bajarle la defensa un 20% durante la partida", 10, tipo_siniestro, TIPOS_ATAQUE.bajar_defensa , 20),
			new Ataque("Rayo oportuno", 
				"Te sube el ataque un 30% durante la partida", 
				10, tipo_electrico, TIPOS_ATAQUE.subir_ataque , 30),
			new Ataque("Ala pesimista", "Le baja el ataque al rival un 10% durante la partida", 10, tipo_volador, TIPOS_ATAQUE.bajar_ataque , 10)
		);
		for (Ataque ataque : ataques) {
			em.persist(ataque);
		}
	}
	
	public List<AnimacionesRoyalmon> getAnimaciones(int index, int total) {
		return listaAnimaciones.subList(index, index + total);
	}
	
	
	public <T> T getJson(String nombre, Class<T> valueType) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String directorioActual = System.getProperty("user.dir");
			String file = directorioActual.replace("\\","/") + "/src/main/java/com/joyfe/daw/des/rest/dao/" + nombre + ".json";
			Path filePath = Paths.get(file);
			InputStream inputStream = Files.newInputStream(filePath);
			return objectMapper.readValue(inputStream, valueType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	
	
	public void initTipos() {
		System.out.println("Insertando tipos...");
		
		for (Tipo tipo : tipos) {
			em.persist(tipo);
		}
	}
		
	
	
	public AssetMapa GetAssetMapa(String name) {
		for (AssetMapa a : assetsMapa) {
			if (a.getNombre().equalsIgnoreCase(name)) {
				return a;
			}
		}
		return null;
	}
	
	public static String obtenerNombreSinExtension(File archivo) {
	    String nombreArchivo = archivo.getName();
	    int lastIndexOfDot = nombreArchivo.indexOf('.');
	    if (lastIndexOfDot != -1) {
	        return nombreArchivo.substring(0, lastIndexOfDot);
	    }
	    return nombreArchivo; // No se encontró una extensión.
	}

	
	public void initAssetsMapa() {
		String directorioActual = System.getProperty("user.dir").replace("\\", "/");
    	File directorioActualFile = new File(directorioActual);
        File directorioPadre = directorioActualFile.getParentFile();
		System.out.println(directorioPadre);
		String directorioObjetosMapa = directorioPadre + "/angular/src/assets/images/mapa";
		File directorio = new File(directorioObjetosMapa);
		assetsMapa = new ArrayList<>();
		if (directorio.exists() && directorio.isDirectory()) {
            // Filtrar archivos por extensión (por ejemplo, .jpg o .png)
            File[] archivos = directorio.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return (name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png")) && !name.contains(" - copia") && !name.startsWith("_");
                }
            });
            if (archivos != null && archivos.length > 0) {
                for (File archivo : archivos) {
                    try {
                        BufferedImage imagen = ImageIO.read(archivo);
                        int ancho = imagen.getWidth() / 100;
                        int alto = imagen.getHeight() / 100;
                        assetsMapa.add(new AssetMapa(obtenerNombreSinExtension(archivo), alto, ancho, archivo.getName()));
                    } catch (Exception e) {
                        System.err.println("No se pudo leer la imagen: " + archivo.getName());
                    }
                }
            } else {
                System.err.println("No se encontraron imágenes de objetos del mapa en el directorio.");
            }
        } else {
            System.out.println("El directorio de los objetos del mapa no existe o no es un directorio válido.");
        }
		for (AssetMapa a : assetsMapa) {
			em.persist(a);
		}
	}
	
	public void initAreas() {
		System.out.println("Insertando areas...");
		for (int i = 0; i < 3; i++) {
			em.persist(new Area());
		}
		initAssetsMapa();
		
		for (Npc npc : npcs) {
			em.persist(npc);
		}
	
		List<RoyalmonSalvaje> royalmonsSalvajes = Arrays.asList(
				new RoyalmonSalvaje(getRoyalmon("Arquera"), 1, 5, 50),
				new RoyalmonSalvaje(getRoyalmon("Bebé dragón"), 2, 6, 50)
		);
		System.out.println("\\n***********************\n");
		for (RoyalmonSalvaje r : royalmonsSalvajes) {
			em.persist(r);
		}
		System.out.println("\\n***********************\n");
		List<RoyalmonExistente> royalmons_batalla = null;
		royalmons_batalla = Arrays.asList(
				new RoyalmonExistente(getRoyalmon("Arquera"), null, 1, 
						true, null, null, null, null, obj_royal_ball),
				new RoyalmonExistente(getRoyalmon("Bebé dragón"), "dragoncin", 
						1, true, null, null, null, null, obj_royal_ball)
		);
		em.persist(npc_bartolo);
		NpcCombate npcCombate = new NpcCombate(
				4, npc_bartolo, royalmons_batalla, 
				"0::No me ganarás tan fácilmente esta batalla!||1::No creas que ya me ganaste, ¡ahora queda lo mejor!||2::No me lo puedo creer!", 
				100, 30);
		em.persist(npcCombate);
		
		GrupoDeAcciones g_marta = GrupoDeAcciones.alrededorMirandoNpc(
				npc_marta, 
				Arrays.asList(
					Accion.IF().siNoEligioInicial(),
						Accion.hablar("Bienvenido nombreJug, elige un inicial antes de empezar a jugar esta demo del juego"),
						Accion.marcarGrupoAccionesComoNoRealizado(),
					Accion.ELSE(),
						Accion.hablar("Hola nombreJug, te daré estas deportivas"),
						Accion.conseguirObjeto(obj_deportivas),
						Accion.hablar("Y también esta bicicleta"),
						Accion.conseguirObjeto(obj_bicicleta),
						Accion.hablar("Y estas cañas de pescar también, por si quieres pescar"),
						Accion.conseguirObjeto(obj_canna_pescar, 3),
						Accion.mover_npc(izquierda, 1),
					Accion.ENDIF()
				)
		);
		em.persist(g_marta);
		
		GrupoDeAcciones g_marta2 = GrupoDeAcciones.alrededorMirandoNpc(npc_marta, Arrays.asList(
				Accion.empezarALlover(),
				Accion.cambiarMusica("lluvia.mp3"),
				Accion.hablar("Uy, está lloviendo, menos mal que voy protegida con mi paraguas"),
				Accion.IF().siEligioPrimerInicial(),
					Accion.hablar("Veo que elegiste la arquera como tu inicial, ¡Buena elección!"),
				Accion.ENDIF(),
				Accion.IF().siEligioSegundoInicial(),
					Accion.hablar("Veo que elegiste el bebé dragón como tu inicial, ¡Buena elección!"),
					Accion.hablar("Una elección muy quemada también te digo"),
				Accion.ENDIF(),
				Accion.dejarDeLlover(),
				Accion.cambiarMusicaArea(),
				Accion.hablar("Bien, ya dejó de llover")
			)).infinito().setGrupoDeAccionesCompletadas(g_marta);
		
		GrupoDeAcciones g_entrarCuartel = GrupoDeAcciones.coordenadasEspacio(
				Arrays.asList(
						Accion.cambiar_area(em.find(Area.class, 2), 2, 5, arriba)
					), 9,5,-1,-1,arriba).infinito();
		
		GrupoDeAcciones g_entrarArea2 = GrupoDeAcciones.coordenadasEspacio(Arrays.asList(
				Accion.cambiar_area(em.find(Area.class, 3), 2, 5, arriba)
			),14,0,-1,-1,arriba).infinito();
		
		GrupoDeAcciones g_salirCuartel = GrupoDeAcciones.coordenadasEspacio(Arrays.asList(
				Accion.cambiar_area(em.find(Area.class, 1), 9, 5, abajo)
			),2,6,-1,-1,abajo ).infinito();
		
		
		GrupoDeAcciones g_entrenador = GrupoDeAcciones.npcMiraJugador(npc_bartolo, Arrays.asList(
				Accion.exclamacionNpc(),
				Accion.npcAcercarseJugador(),
				Accion.hablar("¿Que haces tu por aquí?"),
				Accion.cambiarMusica("batalla_entrenador.mp3"),
				Accion.batalla(npcCombate),
				Accion.hablar("Que barbaridad!, buen combate"),
				Accion.hablar("Has demostrado tener el poder y sabiduría para la medalla de bronce"),
				Accion.conseguirMedalla(),
				Accion.cambiarMusicaArea()
			));
		em.persist(g_entrenador);
		
		
		GrupoDeAcciones g_enfermera = GrupoDeAcciones.alrededorMirandoNpc(npc_enfermera, 
				Arrays.asList(
						Accion.no_andar(),
						Accion.hablar("Te veo triste, voy a curar todos tus royalmons"),
						Accion.ocultarRoyalmonAcomp(),
						Accion.reproducirSonido("curando.mp3"),
						Accion.curarEquipo(),
						Accion.esperar(0.5f),
						Accion.animacionNpc(getKeyframes(
								getKeyframe(24, 7),
								getKeyframe(24, 7),
								getKeyframe(25, 6),
								getKeyframe(25, 6),
								getKeyframe(24, 7),
								getKeyframe(24, 7)
						), 0.6f),
						Accion.esperar(1f),
						Accion.hablar("Ha sido un placer curarte tus royalmons"),
						Accion.mostrarRoyalmonAcomp(),
						Accion.andar()
					)).infinito();
		List<Objeto> objetosTienda = Arrays.asList(
				obj_flechas, obj_bola_fuego, obj_cohete, obj_royal_ball, obj_super_ball, obj_ultra_ball, obj_master_ball
		);
		GrupoDeAcciones g_vendedor = GrupoDeAcciones.alrededorMirandoNpc(npc_vendedor, Arrays.asList(
				Accion.hablar("Mira los productos que tengo pensados para tí"),
				Accion.mostrarTienda(objetosTienda)
			)).infinito();
		
		GrupoDeAcciones g_currante = GrupoDeAcciones.alrededorMirandoNpc(npc_currante, Arrays.asList(
				Accion.hablar("Ostras, que se me hace tarde"),
				Accion.mover_npc(izquierda, 2, 1),
				Accion.mover_npc(abajo, 3, 2),
				Accion.esperar(0.5f),
				Accion.cambiar_area_npc(em.find(Area.class, 1), 1, 2, izquierda)
			));
		
		em.persist(g_currante);
		
		List<ObjetoArea> objetosAreasCentroRoyalmon = Arrays.asList(
			new ObjetoArea(
					/* nombre */ null,
					/* transpasable */ false,
					/* oculto */ false,
					/* x_Ini */ 0, 
					/* y_Ini */ 3, 
					/* rotacion */ 0,
					/* z-index */ 1,
					/* asset */ GetAssetMapa("roca")
				)
		);
		for (ObjetoArea o : objetosAreasCentroRoyalmon) {
			em.persist(o);
		}
		GrupoDeAcciones g_abrir_gestionar_royalmons = GrupoDeAcciones.alrededorMirandoObjeto(objetosAreasCentroRoyalmon.get(0), Arrays.asList(
				Accion.mensaje("Vas a acceder a tu almacenamiento de royalmons"),
				Accion.gestionarRoyalmons()
			)).infinito();
		
		ObjetoArea recolectorElixir = ObjetoArea.objeto(GetAssetMapa("elixir_recolector"), 10, 1);
		ObjetoArea elixir = ObjetoArea.objetoTranspasable(GetAssetMapa("elixir"), 10, 1);
		ObjetoArea agua = ObjetoArea.objeto(GetAssetMapa("agua_1"), 18, 2, 24, 10);
		List<ObjetoArea> objetosAreas = Arrays.asList(
				ObjetoArea.objeto(GetAssetMapa("cuartel_azul"), 9, 3),
				ObjetoArea.objeto(GetAssetMapa("roca"), 2, 1),
				ObjetoArea.objetoOculto(GetAssetMapa("roca"), 12, 7),
				ObjetoArea.objetoTranspasable(GetAssetMapa("arbusto"), 3, 4, 5, 7).setZIndex(0),
				ObjetoArea.objeto(GetAssetMapa("cofre"), 5, 0),
				ObjetoArea.objeto(GetAssetMapa("cofre"), 6, 0),
				ObjetoArea.objetoOculto(GetAssetMapa("cofre_abriendose"), 5, 0),
				ObjetoArea.objetoOculto(GetAssetMapa("cofre_abriendose"), 6, 0),
				ObjetoArea.objeto(GetAssetMapa("tronco"), 14, 0),
				ObjetoArea.objeto(GetAssetMapa("valla"), 12, 0),
				ObjetoArea.objeto(GetAssetMapa("valla"), 15, 0),
				agua,
				ObjetoArea.objetoTranspasable(GetAssetMapa("camino_tierra"), 5, 5, 10, 6).setZIndex(0),
				ObjetoArea.objetoTranspasable(GetAssetMapa("camino_tierra"), 5, 3, 6, 5).setZIndex(0),
				ObjetoArea.objeto(GetAssetMapa("arbol"), 0, 9, 6, 14),
				ObjetoArea.objeto(GetAssetMapa("arbol"), 4, 0, 5, 2),
				ObjetoArea.objeto(GetAssetMapa("arbol"), 6, 2, 9, 3),
				ObjetoArea.objeto(GetAssetMapa("arbol"), 9, 0, 10, 2),
				recolectorElixir.setZIndex(1),
				elixir.setZIndex(0)
				);
		//cofre_abriendose
		for (ObjetoArea o : objetosAreas) {
			em.persist(o);
		}
		ObjetoArea cofre1 = objetosAreas.get(4);
		ObjetoArea cofre2 = objetosAreas.get(5);
		ObjetoArea cofre1_abr = objetosAreas.get(6);
		ObjetoArea cofre2_abr = objetosAreas.get(7);
		ObjetoArea tronco = objetosAreas.get(8);
		GrupoDeAcciones g_recolector = 
				GrupoDeAcciones.alrededorMirandoObjeto(recolectorElixir, Arrays.asList(
				Accion.no_andar(),
				Accion.IF().siVariableGlobal("elixir_lleno", 1),
					Accion.setVariableGlobal("elixir_lleno", 0),
					Accion.moverObjetoMapa(elixir, 0, -0.30f, 2),
					Accion.mensaje("Se llenó el recolector"),
				Accion.ELSE(),
					Accion.setVariableGlobal("elixir_lleno", 1),
					Accion.moverObjetoMapa(elixir, 0, 0.30f, 2),
					Accion.mensaje("Se vació el recolector"),
				Accion.ENDIF(),
				Accion.esperar(2),
				Accion.andar()
				)).infinito();
		
		RoyalmonExistente arquera_inicial = new RoyalmonExistente(getRoyalmon("Arquera"), null, 5, false, null, null, null, null, obj_royal_ball);
		RoyalmonExistente bebe_dragon_inicial = new RoyalmonExistente(getRoyalmon("Bebé dragón"), null, 5, true, null, null, null, null, obj_royal_ball);
		
		GrupoDeAcciones g_tronco = GrupoDeAcciones.alrededorMirandoObjeto(tronco, Arrays.asList(
				Accion.mensaje("Este tronco estorba por el camino..."),
				Accion.pregunta("¿Quieres usar corte?", "usarCorte").siTieneRoyalmonConAtaque(atq_corte),
				Accion.ocultarObjetoArea(tronco).setCondicion("usarCorte").siTieneRoyalmonConAtaque(atq_corte)
		)).infinito();
		
		GrupoDeAcciones g_cofre1 = GrupoDeAcciones.alrededorMirandoObjeto(cofre1, Arrays.asList(
				Accion.IF().siNoEligioInicial(),
				Accion.mostrarRoyalmonPantalla(getRoyalmon("Arquera")),
				Accion.ocultarObjetoArea(cofre1),
				Accion.mostrarObjetoArea(cofre1_abr),
				Accion.pregunta("¿Quieres que tu inicial sea Arquera?", "elegirArquera"),
				Accion.IF().setCondicion("elegirArquera"),
					Accion.mensaje("Muy bien, disfruta con tu Arquera"),
					Accion.obtenerRoyalmonExistente(arquera_inicial),
					Accion.eligioPrimerInicial(),
				Accion.ELSE(),
					Accion.mensaje("¿No te gustan las arqueras?"),
					Accion.ocultarObjetoArea(cofre1_abr),
					Accion.mostrarObjetoArea(cofre1),
				Accion.ENDIF(),
				Accion.ocultarRoyalmonPantalla(),
			Accion.ENDIF()
		)).infinito();
		
		GrupoDeAcciones g_cofre2 = GrupoDeAcciones.alrededorMirandoObjeto(cofre2, Arrays.asList(
				Accion.setVariableGlobal("roca_desplazada", 1),
				Accion.IF().siNoEligioInicial(),
		        	Accion.mostrarRoyalmonPantalla(getRoyalmon("Bebé Dragón")),
			        Accion.ocultarObjetoArea(cofre2),
			        Accion.mostrarObjetoArea(cofre2_abr),
			        Accion.pregunta("¿Quieres que tu inicial sea el bebé dragón?", "elegirDragon"),
			        Accion.IF().setCondicion("elegirDragon"),
			        	Accion.mensaje("Muy bien, disfruta con tu bebé dragón"),
			        	Accion.obtenerRoyalmonExistente(bebe_dragon_inicial),
			        	Accion.eligioSegundoInicial(),
			        Accion.ELSE(), 
			        	Accion.mensaje("¿No te gustan los dragones?"),
			        	Accion.ocultarObjetoArea(cofre2_abr),
			        	Accion.mostrarObjetoArea(cofre2),
			        Accion.ENDIF(),
			        Accion.ocultarRoyalmonPantalla(),
		        Accion.ENDIF()
			)).infinito();
		
		GrupoDeAcciones g_roca = GrupoDeAcciones.alrededorMirandoObjeto(objetosAreas.get(1), Arrays.asList(
				Accion.pregunta("¿Y si rompo la piedra de una patada?", "romperPiedra"),
				Accion.IF().setCondicion("romperPiedra"),
					Accion.ocultarObjetoArea(objetosAreas.get(1)),
					Accion.setVariableGlobal("roca_rota", 1),
					Accion.mensaje("¡Qué fuerte estoy!"),
					/*
					Accion.BEGIN_RANDOM_SELECTION(),
					Accion.SET_PROBABILITY(50),
						Accion.mensaje("Has pescado una trucha"),
						Accion.mensaje("Molan las truchas"),
						Accion.IF().siEligioPrimerInicial(),
							Accion.mensaje("Eligio el primer inicial"),
						Accion.ELSE(),
							Accion.mensaje("No eligio ningún inicial").siNoEligioInicial(),
							Accion.mensaje("Eligió el primer inicial").siEligioPrimerInicial(),
						Accion.ENDIF(),
					Accion.SET_PROBABILITY(50),
						Accion.mensaje("Has pescado una lubina"),
						Accion.mensaje("Molan las lubinas"),
					Accion.END_RANDOM_SELECTION(),*/
					Accion.sumarDinero(100),
					Accion.restarDinero(20),
					//Accion.reproducirSonidoRoyalmon(getRoyalmon("Arquera"), "ataque"),
				Accion.ELSE(),
					Accion.marcarGrupoAccionesComoNoRealizado(),
					Accion.pregunta("¿Y si la muevo entonces?", "moverPiedra"),
					Accion.IF().setCondicion("moverPiedra"),
						Accion.IF().siVariableGlobal("roca_desplazada", 1),
							Accion.moverObjetoMapa(objetosAreas.get(1), 1, -1, 2),
							Accion.setVariableGlobal("roca_desplazada", 0),
						Accion.ELSE(),
							Accion.moverObjetoMapa(objetosAreas.get(1), -1, 1, 2),
							Accion.setVariableGlobal("roca_desplazada", 1),
						Accion.ENDIF(),
						Accion.esperar(2),
					Accion.ELSE(),
						Accion.mensaje("Entonces la dejo ahí"),
					Accion.ENDIF(),
				Accion.ENDIF()
			));
		
		GrupoDeAcciones g_currante_2 = GrupoDeAcciones.alrededorMirandoNpc(npc_currante, Arrays.asList(
				Accion.hablar("Ay no, que hoy era domingo, ¡Qué despiste!"),
				/*
				Accion.IF().siNoEligioInicial(),
					Accion.hablar("Ya es hora de que tengas tu primer royalmon, ve a los cofres y elige uno"),
				Accion.ELSE(),
					Accion.hablar("Ya elegiste tu inicial"),
					Accion.cambiar_area_npc(em.find(Area.class, 1), npc_vendedor, 0, 1, derecha),
					Accion.mover_npc(npc_vendedor, abajo, 2, 1),
				Accion.ENDIF(),
				*/
				Accion.mostrarObjetoArea(objetosAreas.get(2))
			)).infinito();
		
		GrupoDeAcciones g_entrenador_2 = GrupoDeAcciones.alrededorMirandoNpc(npc_bartolo, Arrays.asList(
				Accion.hablar("Madre mía, ¡Qué paliza me has dado!")
			)).infinito().setGrupoDeAccionesCompletadas(g_entrenador);
		
		em.persist(g_entrenador_2); 
		
		GrupoDeAcciones g1_g = GrupoDeAcciones.alrededorMirandoNpc(npc_nicolas, Arrays.asList(
				Accion.hablar("Hola, ¿qué tal?"),
				Accion.hablar("La arquera evoluciona a nivel 6 o también puede evolucionar por objeto"),
				Accion.hablar("Una cosa más...")
				));
		
		em.persist(g1_g);
		
		GrupoDeAcciones g_rugido = GrupoDeAcciones.alrededorMirandoNpc(npc_bebe_dragon, Arrays.asList(
				//Accion.animacionNpc("atq_rival_defecto", 2).siVariableGlobal("roca_rota", 1)
			)).infinito();
		
		GrupoDeAcciones g_rugido2 = GrupoDeAcciones.alrededorMirandoNpc(npc_arquera, Arrays.asList(
				
			)).infinito();
		
		em.persist(g_rugido);
		em.persist(g_rugido2);
		
		GrupoDeAcciones g2_g = GrupoDeAcciones.alrededorMirandoNpc(npc_nicolas, Arrays.asList(
				Accion.IF().siTieneEnSuBolsa(obj_pez),
					Accion.hablar("Estoy oliendo que tienes pescado en tu bolsa"),
					Accion.pregunta("Si me das un pez, te daré otros objetos, ¿aceptas?", "darPez"),
					Accion.IF().setCondicion("darPez"),
						Accion.quitarObjeto(obj_pez, 1),
						Accion.conseguirObjeto(obj_flechas, 1),
						Accion.conseguirObjeto(obj_bola_fuego, 1),
						Accion.conseguirObjeto(obj_cohete, 1),
						Accion.conseguirObjeto(obj_vuelo, 1),
						Accion.conseguirObjeto(obj_corte, 2),
						Accion.conseguirObjeto(obj_curacion_nvl2, 20),
						Accion.hablar("Ala, ahí tienes, ahora no me molestes que voy a comer"),
					Accion.ELSE(),
						Accion.hablar("Tengo mucha hambre, si cambias de opinión me avisas"),
						Accion.marcarGrupoAccionesComoNoRealizado(),
					Accion.ENDIF(),
					/*Accion.mover_npc(npc_bebe_dragon, arriba, 2, 1),
					Accion.mover_npc(npc_bebe_dragon, derecha, 3, 2),
					Accion.mover_npc(derecha, 2, 3),
					Accion.mover_jugador(derecha, 1, 3),
					Accion.mover_npc(arriba, 1, 4),
					Accion.mover_jugador(derecha, 1, 4),
					Accion.mover_npc(arriba, 2, 5),
					Accion.mover_jugador(arriba, 2, 5),*/
				Accion.ELSE(),
					Accion.hablar("Veo que no has pescado todavía, ve al lago que está más a la derecha"),
					Accion.hablar("Y cuando tengas un pez te doy unos objetos"),
					Accion.marcarGrupoAccionesComoNoRealizado(),
				Accion.ENDIF()
			)).setGrupoDeAccionesCompletadas(g1_g);
		em.persist(g2_g);
		
		GrupoDeAcciones g2_g3 = GrupoDeAcciones.alrededorMirandoNpc(npc_nicolas, Arrays.asList(
				Accion.hablar("Dejame tranquilo, que me estoy comiendo mi pez")
		)).infinito().setGrupoDeAccionesCompletadas(g2_g);
		
		GrupoDeAcciones g1 = GrupoDeAcciones.alrededorMirandoNpc(npc_miguel, 
				Arrays.asList(Accion.hablar("Hola, ¿qué tal?")
		));
		
		em.persist(g1);
		
		GrupoDeAcciones g2 = GrupoDeAcciones.alrededorMirandoNpc(npc_miguel, Arrays.asList(
				Accion.hablar("Me tengo que ir ya"),
				Accion.mover_npc(derecha, 4, 1),
				Accion.mover_npc(abajo, 2, 2),
				Accion.mover_npc(izquierda, 3, 3)
			)).setGrupoDeAccionesCompletadas(g1);
		em.persist(g2);
		
		GrupoDeAcciones g3 = GrupoDeAcciones.alrededorMirandoNpc(npc_miguel, Arrays.asList(
				Accion.hablar("Uy, ¿Cómo me encontraste?"),
				Accion.hablar("Toma, te regalo estas royalballs, ve a los arbustos a capturar arqueras y baby dragons"),
				Accion.conseguirObjeto(obj_royal_ball, 3),
				Accion.conseguirObjeto(obj_alas_magicas, 1)
			)).infinito().setGrupoDeAccionesCompletadas(g2);
		
		
		GrupoDeAcciones g_pesca = GrupoDeAcciones.alrededorMirandoObjeto(agua, Arrays.asList(
				Accion.mensaje("Estas son aguas idóneas para pescar..."),
				Accion.IF().siTieneEnSuBolsa(obj_canna_pescar),
					Accion.pregunta("¿Quieres pescar?", "condicionCaña"),
					Accion.IF().setCondicion("condicionCaña"),
						Accion.no_andar(),
						Accion.pescando(),
						Accion.esperar(1),
						Accion.BEGIN_RANDOM_SELECTION(),
							Accion.SET_PROBABILITY(50),
								Accion.conseguirObjeto(obj_pez),
								Accion.mensaje("Has pescado un pez!!"),
							Accion.SET_PROBABILITY(30),
								Accion.mensaje("Vaya, no has pescado nada"),
							Accion.SET_PROBABILITY(20),
								Accion.mensaje("Se ha roto la caña de pescar"),
								Accion.quitarObjeto(obj_canna_pescar),
						Accion.END_RANDOM_SELECTION(),
						Accion.andar(),
					Accion.ENDIF(),
				Accion.ENDIF())).infinito();
		
		GrupoDeAcciones g_royalmonsSalvajes = GrupoDeAcciones.coordenadas(Arrays.asList(
				Accion.aparicionRoyalmonsSalvajes(royalmonsSalvajes, 20, "salvaje.mp3", 2)
			), 3,4,5,7).infinito();
		
		
		List<GrupoDeAcciones> gruposDeAcciones = Arrays.asList(
			g1_g, g2_g,g2_g3, g1,g2,g3, g_entrenador, g_entrenador_2,g_rugido,g_rugido2
			,g_entrarCuartel,g_entrarArea2,g_currante_2,g_roca,g_cofre1,g_cofre2
			,g_tronco,g_vendedor,g_recolector,g_marta,g_marta2,g_pesca,
			g_royalmonsSalvajes
		);
		List<GrupoDeAcciones> gruposDeAccionesCentroRoyalmon = Arrays.asList(
			g_enfermera, g_salirCuartel, g_currante, g_abrir_gestionar_royalmons, g_vendedor
		);
		
		List<AreaInfo> areasInfo = Arrays.asList(
			new AreaInfo(
							"ruta 1", 
							"Una ruta muy bonita",
							null, /* ancho */30l, 
							/* largo */ 30l, "hierva_clara.png", 
							/* x_aparicion */10, 
							/* y_aparicion */5,
							/* grupo */ 1, 
							TIPOS_DE_AREAS.principal, 
							objetosAreas
							, gruposDeAcciones, npcs,
							/* puedoVolar*/ true,
							/* aparecerEnMapa */ true,
							/* musica */ "Route 1.mp3" ),
			new AreaInfo(
					"Centro Royalmon", 
					null,
					null, /* ancho */5l, 
					/* largo */ 7l, "piedra.png", 
					/* x_aparicion */2, 
					/* y_aparicion */1,
					/* grupo */ 1, 
					TIPOS_DE_AREAS.centro_recuperacion, 
					objetosAreasCentroRoyalmon
					, gruposDeAccionesCentroRoyalmon, npcsCentroRoyalmon,
					/* puedoVolar*/ false,
					/* aparecerEnMapa */ true,
					/* musica */ "PokeCenter.mp3" ),
			new AreaInfo(
					"ruta 2", 
					"La ruta donde más flores hay",
					null, /* ancho */30l, 
					/* largo */ 30l, "hierva_clara.png", 
					/* x_aparicion */5, 
					/* y_aparicion */10,
					/* grupo */ 2, 
					TIPOS_DE_AREAS.principal, 
					Arrays.asList(),
					Arrays.asList(),
					Arrays.asList(),
					/* puedoVolar*/ true,
					/* aparecerEnMapa */ true,
					/* musica */ "Route 1.mp3"
			)
		);
		
		for (AreaInfo a : areasInfo) {
			em.persist(a);
		}
	}
	
	public String getAreaBase(String tipo) {
		String data = getJson(tipo, String.class);
		return tipo + "##" + data;
	}
	
	public String getKeyframes(String ...keyframes) {
		return String.join("|", keyframes);
	}
	
	public String getKeyframe(int fila, int col) {
		return getKeyframe(fila, col, false);
	}
	
	public String getKeyframe(int fila, int col, boolean flip) {
		return fila + "," + col + "," + (flip ? 1 : 0);
	}
}
