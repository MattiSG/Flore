package game;

import element.XMLLoadableElement;

import javax.swing.JLabel;

public class DocView extends InfoView {
    private JLabel doc = new JLabel();

    public DocView() {
        description = "Un peu de documentation ne serait pas de refus...";
        doc.setText(description);
        add(doc);
    }

    public void display(XMLLoadableElement e) {
        description = "Voici la description de : " + e.name() + ". \n" + e.description();
        doc.setText(description);
        play();
    }
}
