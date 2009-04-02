/*  classe de menu de lancement du jeu :
 *  cette classe hérite de la classe abstraite MenuAbstrait en définissant les méthodes :
 *     - nomOptions qui renvoie la liste des options possibles pour le menu
 *     - lancerOption qui associe une action à chaque option du menu
 *     - wavAccueil() qui renvoie le nom du fichier wav lu lors de l'accueil dans le menu
 *     - wavAide() qui renvoie le nom du fichier wav lu lors de l'activation de la touche F1
 */

package game; 

import devintAPI.MenuAbstrait;

import java.awt.GraphicsDevice;
import javax.swing.JWindow;

public class Flore extends MenuAbstrait {
    public Flore(String title) {
        super(title);
        setSize(500,500);
        setVisible(true);
    }

    protected String[] nomOptions() {
        return new String[] { "Jouer", "Quitter" };
    }

    protected void lancerOption(int i) {
        switch (i) {  
            case 0 : new MainWindow();    break;
                     //case 1 : new OptionInterface(nomJeu); break;
                     //case 2 : new ScoreInterface(nomJeu);  break;
            case 3 : System.exit(0);
            default: System.err.println("action non définie");
        }
    } 

    protected  String wavAccueil() {
        return "";
        //return "../ressources/accueil.wav";
    }

    protected  String wavAide(){
        return "";
        //return "../ressources/aideF1.wav";
    }

    static public void main(String[] args) {
        Flore f = new Flore("Flore");
    }
}
