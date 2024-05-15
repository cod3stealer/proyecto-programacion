package EDITOR.SELECTPANEL;

import EDITOR.FX.SpriteLoader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;

public class GridPanel extends JPanel {
    private static List<Celda> celdas; // Lista de paneles cuadrados
    Celda celda;
    ImageIcon sprite;


    public GridPanel(int rows, int cols, ImageIcon[] sprites) throws IOException {
        celdas = new ArrayList<>();

        setBackground(Color.DARK_GRAY);
        setLayout(new GridLayout(rows,cols)); // Usar GridLayout para el diseño
        GridBagConstraints constraints = new GridBagConstraints();

        int contador = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (sprites.length > contador){
                    sprite = sprites[contador];
                    contador++;
                    celda = new Celda(sprite);
                } else {
                    celda = new Celda(new ImageIcon());
                }
                celdas.add(celda);
                constraints.gridx = col;
                constraints.gridy = row;
                add(celda, constraints);
            }
        }
    }

    public static List<Celda> getCeldas() {
        return celdas;
    }
}
