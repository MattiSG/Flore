package game;

import element.XMLLoadableElement;

import javax.swing.JLabel;

public class MissionView extends InfoView {
    private JLabel mission = new JLabel();

    public MissionView() {
        description = "Votre mission, si vous l'acceptez est de capturer les insectes.";
        mission.setText(description);
        add(mission);
    }

    public void display(XMLLoadableElement e) {
        description = "Votre mission est " + e.description();
        mission.setText(description);
        play();
    }
}
