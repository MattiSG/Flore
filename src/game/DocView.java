package game;

import element.XMLLoadableElement;

import javax.swing.JTextArea;
import java.awt.BorderLayout;

public class DocView extends InfoView {
    private JTextArea doc = new JTextArea();

    public DocView() {
        doc.setLineWrap(true);
        description = "Un peu de documentation ne serait pas de refus...";
        doc.setText(description);
        doc.setSize(400, 400);
        add(doc, BorderLayout.CENTER);
    }

    public void display(XMLLoadableElement e) {
        description = "Voici la description de : " + e.name() + ". \n" + e.description();
        doc.setText(description);
        play();
    }
}
