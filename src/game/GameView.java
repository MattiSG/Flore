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
    private BufferedImage    grass;
    private ArrayList<Point> holes = new ArrayList<Point>();
    private Point            selectedHole;

    public static final int HOLES_NUMBER = 4;

    public GameView() {
        try {
            grass = ImageIO.read(new File("../ressources/images/grass.jpg"));
        } catch (java.io.IOException e) {
            System.err.println("[erreur] " + e);
        }

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                computeHoles();
            }
        });
    }

    public int getSelectedHoleIndex() {
        return holes.indexOf(selectedHole);
    }

    public void setSelectedHoleNext() {
        int i = getSelectedHoleIndex();
        selectedHole = holes.get(holes.size() == i+1 ? 0 : i+1);
        repaint();
    }

    public void setSelectedHolePrevious() {
        int i = getSelectedHoleIndex();
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
        int w = grass.getWidth();
        int h = grass.getHeight();
        int nbW = (rect.width / w) + 1;
        int nbH = (rect.height / h) + 1;
        for (int x = 0; x < nbW; ++x)
            for (int y = 0; y < nbH; ++y)
                g2d.drawImage(grass, null, x * w, (rect.height * 2 / 3) + y * h);

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

        // plantes
        MainWindow m = (MainWindow) getParent().getParent().getParent().getParent(); // immonde => ajouter un param au constructeur
        //for (Plant p : m.getPlantedPlants())
        for (int i = 0; i < m.getPlantedPlants().size(); ++i)
        {
            Plant p = m.getPlantedPlants().get(i);
            if (p != null)
            {
                Point pos = holes.get(i);
                p.setX(pos.x);
                p.setY(pos.y);
                System.out.println(" => " + i + " : " + p.get());
                p.paint(g);
            }
        }
    }
}
