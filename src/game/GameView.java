package game;

import element.plant.Plant;

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

import java.util.ArrayList;

public class GameView extends JPanel {
    private Plant plant = new Plant("rosa");
    private BufferedImage im;
    private ArrayList<Point> holes = new ArrayList<Point>();
    private Point selectedHole;

    private final int HOLES_NUMBER = 4;

    public GameView() {
        plant.set(100.0f);
        try {
            im = ImageIO.read(new File("../ressources/images/grass.jpg"));
        } catch (java.io.IOException e) {
            System.err.println("[erreur] " + e);
        }

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                computeHoles();
            }
        });
    }

    public void setSelectedHoleNext() {
        int i = holes.indexOf(selectedHole);
        selectedHole = holes.get(holes.size() == i+1 ? 0 : i+1);
        repaint();
    }

    public void setSelectedHolePrevious() {
        int i = holes.indexOf(selectedHole);
        selectedHole = holes.get(i == 0 ? holes.size()-1 : i-1);
        repaint();
    }

    private void computeHoles() {
        int w = getSize().width;
        int h = getSize().height;
        int n = w / (HOLES_NUMBER + 1);
        //System.out.println("width:  " + w);
        //System.out.println("height: " + h);
        //System.out.println("space:  " + n);
        for (int i = 0; i < HOLES_NUMBER; ++i)
        {
            holes.add(new Point(n*(i+1), h-(h/10)));
            //System.out.println("trous n°" + i + ": " + n*(i+1) + ", " + (h-(h/10)));
        }
        selectedHole = holes.get(0);
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

        // trous
        g2d.setColor(Color.BLACK);
        for (Point p : holes)
        {
            if (p == selectedHole) {
                g2d.setColor(Color.RED);
                g2d.fillOval(p.x-50, p.y-50, 100, 100);
                g2d.setColor(Color.BLACK);
            }
            g2d.fillOval(p.x-25, p.y-25, 50, 50);
        }
    }
}
