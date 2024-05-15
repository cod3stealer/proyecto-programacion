package EDITOR.FX;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;


/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Esta clase define la interacción del jugador con el entorno asi como su movimiento y uso de gráficos en 2D.
 * */
public class SpriteUtils {
    public void generateSpriteMap(ImageIcon[] sprites, ImageIcon[][] formato) throws IOException {
        String userInput;
        String mapName = null;

        do {
            userInput = JOptionPane.showInputDialog(null, "Introduzca el nombre del mapa:");
            if (userInput != null && !userInput.isEmpty()) {
                mapName = userInput.trim();
                // Quitar extensión .txt si existe
                if (mapName.toLowerCase().endsWith(".txt")) {
                    mapName = mapName.substring(0, mapName.length() - 4);
                }
            }
        } while (mapName == null || mapName.isEmpty());

        try (FileWriter fileWriter = new FileWriter("Assets/maps/"+mapName+".txt")) {

            for (ImageIcon[] imageIcons : formato) {
                for (ImageIcon imageIcon : imageIcons) {
                    boolean foundMatch = false;
                    int spriteIndex = 0;

                    if (imageIcon != null) {
                        for (ImageIcon sprite : sprites) {
                            if (imageIcon == sprite) {
                                fileWriter.write(spriteIndex + " ");
                                foundMatch = true;
                            }
                            spriteIndex++;
                        }
                    }

                    if (!foundMatch) {
                        fileWriter.write("0 ");
                    }
                }
                fileWriter.write(System.lineSeparator());
            }
        }
    }
}
