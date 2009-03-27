package game;

import element.XMLLoadableElement;

import t2s.SIVOXDevint;

import javax.swing.JPanel;

abstract public class InfoView extends JPanel {
    protected SIVOXDevint player      = new SIVOXDevint();
    protected String      description = "";

    public void play() {
        player.stop();
        player.playText(description);
    }

    abstract public void display(XMLLoadableElement e);
}
