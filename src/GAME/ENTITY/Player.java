package GAME.ENTITY;

import GAME.FX.KeyManager;
import GAME.GAME.TeisPanel;
import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Esta clase define la interacción del jugador con el entorno asi como su movimiento y uso de gráficos en 2D.
 * */
public class Player extends Entity {

    // Propiedades del jugador
    Propierties propierties;

    public KeyManager keyManager;

    public int screenX;
    public int screenY;

    // Checkea si tienes una VigoPass
    public boolean tenPass = false;

    // Constructor
    /**
     * Constructor de la clase Player, que representa al jugador en el juego.
     *
     * @param t instancia de TeisPanel, que es el panel donde se dibujará el jugador
     * @param k instancia de KeyManager, que maneja las entradas del teclado
     */
    public Player(TeisPanel t, KeyManager k, Propierties propierties) {
        // Llama al constructor de la clase padre (Entity) y pasa la instancia de TeisPanel
        super(t,propierties);

        // Asigna la instancia de KeyManager para manejar las entradas del teclado
        this.keyManager = k;

        this.propierties = propierties;

        // Inicializa la posición del jugador en la pantalla
        screenX = t.screenWidth / 2 - (t.sizeFinal / 2);
        screenY = t.screenHeight / 2 - (t.sizeFinal / 2);

        // Inicializa los valores por defecto del jugador
        setValoresPorDefecto();

        // Carga las imágenes del jugador
        getPlayerImage();

        // Carga las imágenes de ataque del jugador
        getPlayerAttackImage();
    }

    /**
     * Metodo que define el estado inicial del jugador
     */
    public void setValoresPorDefecto() {
        setPropierties("player");

        worldX = teisPanel.sizeFinal * 18;
        worldY = teisPanel.sizeFinal * 10;

        // Guarda los valores por defecto del área sólida
        defaultSolidAreaX = solidArea.x;
        defaultSolidAreaY = solidArea.y;

        // Iniciliaza el área de ataque del jugador
        attackArea.width = 36;
        attackArea.height = 36;
    }

    /**
     * Carga las imágenes del jugador y las establece en las variables correspondientes.
     */
    public void getPlayerImage() {
        // Carga las imágenes del jugador caminando hacia arriba y las establece en las variables correspondientes
        up1 = setEntitySprite("player/upWalkingBehind1.png", 48, 48);
        up2 = setEntitySprite("player/upWalkingBehind2.png", 48, 48);

        // Carga las imágenes del jugador caminando hacia abajo y las establece en las variables correspondientes
        down1 = setEntitySprite("player/downWalking1.png", 48, 48);
        down2 = setEntitySprite("player/downWalking2.png", 48, 48);

        // Carga las imágenes del jugador caminando hacia la izquierda y las establece en las variables correspondientes
        left1 = setEntitySprite("player/leftWalking1.png", 48, 48);
        left2 = setEntitySprite("player/leftWalking2.png", 48, 48);

        // Carga las imágenes del jugador caminando hacia la derecha y las establece en las variables correspondientes
        right1 = setEntitySprite("player/rightWalking1.png", 48, 48);
        right2 = setEntitySprite("player/rightWalking2.png", 48, 48);

        // Carga las imágenes del jugador detenido y las establece en las variables correspondientes
        stop = setEntitySprite("player/frontStanding.png", 48, 48);
        stop2 = setEntitySprite("player/stop2.png", 48, 48);
    }

    public void getPlayerAttackImage() {
        attackUp = setEntitySprite("player/player_attack_up.png", 48, 96);
        attackRight = setEntitySprite("player/player_attack_right.png", 96, 48);
        attackLeft = setEntitySprite("player/player_attack_left.png", 96, 48);
        attackDown = setEntitySprite("player/player_attack_down.png", 48, 96);
    }

    /**
     * Metodo MOVE
     * El juego al ser en 2D solo tiene dos dimensiones espaciales: X, Y
     * Moverse hacia arriba o hacia la derecha es equivalente a SUMAR en la posición
     * mientras que moverse hacia abajo o hacia la izquierda RESTA a la posición actual.
     * Además, controla los sprites por movimiento usados.
     */
    public void move(KeyManager e, TeisPanel teisPanel) throws LineUnavailableException {
        if (attack) {
            attack();
        } else if (e.up || e.down || e.left || e.right || e.isPressed) {
            // Inicializa el contador de parada
            stopCounter = 0;

            // Determina la dirección del movimiento
            sentido = getDirection(e);

            // Comprueba la colisión de la Pieza
            collisionOn = false;
            teisPanel.controller.collisionCheck.checkPieza(this);

            // Colisión de objetos
            int obj = teisPanel.controller.collisionCheck.checkObject(this, true);

            // Llama al método pickUpItem para recoger el objeto si es posible
            pickUpItem(obj);

            // Colision entre entidades
            int npc = teisPanel.controller.collisionCheck.checkEntity(this, teisPanel.controller.npc);
            interactuarNPC(npc);

            // Colision con enemigos
            int enemy = teisPanel.controller.collisionCheck.checkEntity(this, teisPanel.controller.enemy);
            interactuarEnemy(enemy);

            // Trigger de eventos
            teisPanel.controller.eventManager.checkEvent();

            movement();

            teisPanel.model.keyManager.isPressed = false;
        } else {
            // Si no se ha presionado ninguna tecla, incrementa el contador de parada
            sentido = '0';
            stopCounter++;
        }
        // Cambia el spriteNum si el contador de parada es mayor a 30
        if (stopCounter > 15) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            stopCounter = 0;
        }

        if (invencible) {
            timeInvencible++;
            if (timeInvencible > 30) {
                invencible = false;
                timeInvencible = 0;
            }
        }
    }

    /**
     * Devuelve la dirección del movimiento según la tecla presionada.
     *
     * @param e el objeto KeyManager que contiene el estado de las teclas
     * @return la dirección del movimiento como un carácter ('w', 's', 'a', 'd')
     */
    private char getDirection(KeyManager e) {
        // Si la tecla 'up' está presionada, devuelve 'w'
        if (e.up) {
            return 'w';
        }
        // Si la tecla 'down' está presionada, devuelve 's'
        else if (e.down) {
            return 's';
        }
        // Si la tecla 'left' está presionada, devuelve 'a'
        else if (e.left) {
            return 'a';
        }
        // Si la tecla 'right' está presionada, devuelve 'd'
        else if (e.right) {
            return 'd';
        } // Devuelve 'd'
        else return '0';
    }

    /**
     * Metodo que actualiza la posición del jugador mediante una llamada a otro metodo heredado de Entity
     */
    public void actualiza() throws LineUnavailableException {
        move(keyManager, teisPanel);
    }

    /**
     * Método que permite al jugador recoger un objeto del mapa.
     *
     * @param id el índice del objeto que se va a recoger
     */
    public void pickUpItem(int id) throws LineUnavailableException {
        if (id != 999) { // Verifica si el objeto existe

        }
    }

    public void interactuarNPC(int i) {
        if (teisPanel.model.keyManager.isPressed) {
            if (i != 999) {
                teisPanel.controller.estado = teisPanel.controller.dialogoState;
                teisPanel.controller.npc[i].fala();
            } else {
                attack = true;
            }
        }
    }

    public void interactuarEnemy(int i) {
        if (i != 999) {
            if (!invencible) {
                life -= 1;
                invencible = true;
            }
        }
    }

    /**
     * Método de ataque, cuando termina la animación, el boolean vuelve a FALSE
     * */
    public void attack() {
        spriteCounter++;
        if (spriteCounter < 10) {
            spriteNum = 1;

            // Guarda las propiedades de posición del jugador
            int currentX = worldX;
            int currentY = worldY;
            int solidAreaW = solidArea.width;
            int solidAreaH = solidArea.width;

            // Ajusta estas variables al área de ataque
            switch (sentido) {
                case 'w': worldY -= attackArea.height; break;
                case 's': worldY += attackArea.height; break;
                case 'a': worldX -= attackArea.width; break;
                case 'd': worldX += attackArea.width; break;
            }
            // Transforma el área sólida del ataque
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            // Checkea la colisión con enemigos
            int enemy = teisPanel.controller.collisionCheck.checkEntity(this,teisPanel.controller.enemy);
            dealDamage(enemy);

            // Restaura los valores por defecto.
            worldX = currentX;
            worldY = currentY;
            solidArea.width = solidAreaW;
            solidArea.height = solidAreaH;
        } else {
            spriteCounter = 0;
            attack = false;
        }
    }

    public void dealDamage(int i) {
        if (i != 999) {
            if (!teisPanel.controller.enemy[i].invencible) {
                teisPanel.controller.enemy[i].life -= 1;
                teisPanel.controller.enemy[i].invencible = true;
                if (teisPanel.controller.enemy[i].life <= 0) {
                    teisPanel.controller.enemy[i] = null; // El enemigo muere
                }
            }
        }
    }

    /**
     * Método que se encarga de establecer eventos específicos para cada entidad.
     * Debe ser sobreescrito en las clases hijas para implementar la lógica de eventos específica.
     */
    public void setEvent() {
        // Este método debe ser implementado en las clases hijas
    }

    /**
     * Método que se encarga de actualizar el estado de la entidad.
     */
    public void update() {
        // Llama al método setEvent() para establecer eventos específicos
        setEvent();

        // Checkea las colisiones de todas las entidades y piezas
        colisiones();

        // Actualiza el movimiento de la entidad
        movement();
    }

    public void draw (Graphics2D g2) {
        // Obtiene la imagen de sprite correspondiente al número de sprite activo y la dirección actual
        // Dibuja la imagen en la posición relativa al jugador en el panel
        drawMovement(g2);
    }

    /**
     * Dibuja una imagen en una posición relativa al jugador en un panel.
     */
    public void drawMovement(Graphics2D g2) {
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        // Crea un mapa que asocia cada dirección con un array de imágenes de sprite
        Map<Character, BufferedImage[]> sprites = new HashMap<>();
        if (!attack) {
            sprites = fillSprites(sprites,stop,stop2,up1,up2,down1,down2,left1,left2,right1,right2);
        } else {
            sprites = fillSprites(sprites,attackDown,attackDown,attackUp,attackUp,attackDown,attackDown,attackLeft,attackLeft,attackRight,attackRight);
            if (sentido == 'a') tempScreenX -= 48;
            if (sentido == 'w') tempScreenY -= 48;
        }

        // Obtiene el array de imágenes de sprite correspondiente a la dirección actual
        BufferedImage[] images = sprites.get(sentido);

        // Obtiene la imagen de sprite correspondiente al número de sprite activo
        BufferedImage image = images[spriteNum - 1];

        // Pasa tempScreenX y tempScreenY al método drawPlayer
        drawPlayer(g2, image, tempScreenX, tempScreenY);
    }

    public void drawPlayer(Graphics2D g2, BufferedImage image, int tempX, int tempY) {
        // Dibuja la imagen en la posición correspondiente
        if (invencible) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.4f));
        g2.drawImage(image, tempX, tempY, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
    }
}