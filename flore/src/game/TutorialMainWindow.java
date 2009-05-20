package game;

import java.io.File;
import java.io.FilenameFilter;

import java.util.Arrays;

import element.mission.Mission;

/*
 * Classe gérant la fenêtre principale des tutoriaux.
 *
 */
public class TutorialMainWindow extends MainWindow {
 
    // charge toutes les missions en listant le répertoire des ressources
    protected void loadMissions() {
        File rep = new File("../ressources/elements/tutorials");
        File[] listMissions = rep.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith("tutorial_");
            }
        });

        Arrays.sort(listMissions);

        for (File f : listMissions)
            missions.add(new Mission("tutorials/" + f.getName()));
    }
}
