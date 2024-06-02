package GAME.GAME;

import GAME.EFFECT.Sound;
import GAME.EFFECT.UserInterface;
import GAME.ENTITY.CollisionCheck;
import GAME.ENTITY.Entity;
import GAME.ENTITY.Propierties;
import GAME.FX.EventManager;
import GAME.GPHICS.PiezaManager;
import GAME.OBJECT.Placer;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Mantiene una referencia al jugador y al gestor de piezas del juego.
 * */
public class GameController {
    // Gestor de piezas
    private final PiezaManager piezaManager;

    // Efectos de sonido
    Sound sound = new Sound();
    Sound se = new Sound(); // SoundEffect

    // Interfaz de Usuario
    public UserInterface ui;

    // Controlador de colisiones
    public CollisionCheck collisionCheck;
    public Placer placer;

    // Propiedades de cada entidad
    public Propierties propierties = new Propierties("jdbc:postgresql://localhost:5432/proyecto", "postgres", "123");

    // Manejo de objetos
    public Entity[] obj = new Entity[10];

    // ESTADO DEL JUEGO
    public int estado = 3;
    public int pauseState = 0, dialogoState = 1, playState = 2, cargaState = 3;

    // Entidades
    public Entity[] npc = new Entity[10];

    // Entidades no amistosas
    public Entity[] enemy = new Entity[10];

    // Orden de renderizado
    ArrayList<Entity> entities = new ArrayList<>();

    // Manejo de eventos del juego
    public EventManager eventManager;

    /**
     * Constructor que inicializa el controlador del juego con el jugador y el gestor de piezas.
     *
     * @param piezaManager El gestor de piezas del juego.
     */
    public GameController(PiezaManager piezaManager,TeisPanel teisPanel) {
        this.piezaManager = piezaManager;
        propierties.crearTablaEntidad();

        ui = new UserInterface(teisPanel, propierties);
        placer = new Placer(teisPanel, propierties);

        // Inicializa el controlador de colisiones
        collisionCheck = new CollisionCheck(teisPanel);

        // Inicializa el manejo de eventos
        eventManager = new EventManager(teisPanel);
    }

    /**
     * Obtiene el gestor de piezas del juego.
     *
     * @return El gestor de piezas del juego.
     */
    public PiezaManager getPiezaManager() {
        return piezaManager;
    }

    public Sound getSound() {
        return sound;
    }

    public void playMusic(int i) throws LineUnavailableException {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic() {
        sound.stop();
    }

    public void playSelection(int i) throws LineUnavailableException {
        se.setFile(i);
        se.play();
    }

    public void stopSelection(int i) throws LineUnavailableException {
        se.setFile(i);
        se.stop();
    }
}