package game;

import element.plant.Plant;

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

public class GameView extends JPanel {
    private Plant plant = new Plant("rosa");
    private BufferedImage im;
    private ArrayList<Dimension> trous = new ArrayList<Dimension>();

    public GameView() {
        plant.set(100.0f);
        try {
            im = ImageIO.read(new File("../ressources/images/grass.jpg"));
        } catch (java.io.IOException e) {
            System.err.println("[erreur] " + e);
        }
    }

    public void grow() {
        plant.grow();
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Rectangle rect = g2d.getClipBounds();

        // ciel
        g2d.setColor(new Color(118, 142, 176));
        g2d.fillRect(0, 0, rect.width, rect.height * 2 / 3);

        // soleil
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(20, 20, 75, 75);

        // herbe
        int w = im.getWidth();
        int h = im.getHeight();
        int nbW = (rect.width / w) + 1;
        int nbH = (rect.height / h) + 1;
        for (int x = 0; x < nbW; ++x)
            for (int y = 0; y < nbH; ++y)
                g2d.drawImage(im, null, x * w, (rect.height * 2 / 3) + y * h);

        // plante
        plant.paint(g);
    }
}
