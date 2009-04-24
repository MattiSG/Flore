package game;

import element.XMLLoadableElement;
import element.plant.Plant;
import element.creature.Creature;
import element.mission.Mission;

import t2s.SIVOXDevint;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainWindow extends JFrame {
    private JLabel           statusBar     = new JLabel("Initialisation");
    private JProgressBar     levelBar      = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
    private DefaultListModel seedList      = new DefaultListModel();
    private JList            seedListView  = new JList(seedList);
    private GameView         gameView      = new GameView();
    private SIVOXDevint      player        = new SIVOXDevint();

    // plantes de la mission courante
    private List<Plant>    plants   = new LinkedList<Plant>();
    // insectes de la mission courante
    private List<Creature> insects  = new LinkedList<Creature>();
    // listes de toutes les missions
    private List<Mission>  missions = new LinkedList<Mission>();

    // plantes en terre
    private ArrayList<Plant> plantedPlants = new ArrayList<Plant>();
    // mission courante
    private Mission          currentMission;
    // timer pour la pousse des plantes
    private Timer            timer;

    public MainWindow() {
        // chargement de toutes les missions
        loadMissions();

        // chargement de la première mission
        loadMission(0);

        // changement du type de rendu de la liste pour l'affichage des images
        seedListView.setCellRenderer(new CustomCellRenderer());
        seedListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // autoriser l'affichage d'un message dans la progress bar
        levelBar.setStringPainted(true);

        // gestion des évènements gauche, droite, haut, bas et entrée
        seedListView.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyCode()) {
                    int i = gameView.getSelectedHoleIndex();
                    if (plantedPlants.get(i) == null) {
                        try {
                            plantedPlants.set(i, (Plant) ((Plant) seedListView.getSelectedValue()).clone());
                        } catch(CloneNotSupportedException ex) {
                            System.err.println("[erreur] Impossible de cloner l'objet plante : " + ex);
                        }
                    }
                    else
                        play("Il ya déjà une plante dans ce trou.", "Il y a déjà une plante dans ce trou.");
                    gameView.repaint();
                } else if (KeyEvent.VK_RIGHT == e.getKeyCode()) {
                    gameView.selectNextHole();
                } else if (KeyEvent.VK_LEFT == e.getKeyCode()) {
                    gameView.selectPreviousHole();
                } else if (KeyEvent.VK_ESCAPE == e.getKeyCode()) {
                    statusBar.setText("Au revoir !");
                    dispose();
                }
            }
        });

        // pronociation du nom de la plante dont la graine est sélectionnée
        seedListView.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                Plant p = (Plant) seedListView.getSelectedValue();
                play(p.name());
            }
        });

        // scroll pour l'affichage de la liste
        JScrollPane list = new JScrollPane(seedListView);
        list.setPreferredSize(new Dimension(200, 400));

        getContentPane().add(gameView,  BorderLayout.CENTER);
        getContentPane().add(list,      BorderLayout.EAST);
        getContentPane().add(levelBar,  BorderLayout.WEST);
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setFullScreen();
        setSize(700,700);
        setVisible(true);

        seedListView.requestFocus();
        seedListView.setSelectedIndex(0);

        // lancement du jeu
        run();
    }

    private void run() {
        play(currentMission.description());
        statusBar.setText(currentMission.description());

        int delay = 100;
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean finish = true;
                for (Plant p : plantedPlants)
                    if (p != null) {
                        p.grow();
                        if (!p.isAdult())
                            finish = false;
                    }
                    else
                        finish = false;

                gameView.repaint();

                if (finish) {
                    gameView.repaint();
                    stopTimer();
                    play("Tu as gagné cette mission !", true);
                    int newMission = missions.indexOf(currentMission) + 1;
                    if (newMission >= missions.size()) {
                        play("Tu as fini de jouer. Il n'y a plus de niveau disponible.");
                        setVisible(false);
                        dispose();
                    }
                    else
                    {
                        play("Niveau suivant");
                        missions.get(newMission);
                    }
                }
            }
        };
        timer = new Timer(delay, taskPerformer);
        timer.start();
    }

    private void stopTimer() {
        timer.stop();
    }

    // prononce le texte et l'affiche dans la barre de status
    private void play(String readText, String statusText, boolean widthDialog) {
        player.stop();
        player.playText(readText);
        statusBar.setText(statusText);
        if (widthDialog)
            JOptionPane.showMessageDialog(null, statusText, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void play(String readText) {
        play(readText, readText, false);
    }

    private void play(String readText, boolean widthDialog) {
        play(readText, readText, widthDialog);
    }

    private void play(String readText, String statusText) {
        play(readText, statusText, false);
    }

    private void loadMissions() {
        missions.add(new Mission("mission_1"));
        missions.add(new Mission("mission_2"));
    }

    private void loadMission(int index) {
        currentMission = missions.get(index);

        for (int i = 0; i < currentMission.holes(); ++i)
            plantedPlants.add(null);

        loadCurrentPlants();
        loadCurrentInsects();

        gameView.setHolesNumber(currentMission.holes());
    }

    private void loadCurrentPlants() {
        plants.clear();

        for (Map.Entry<String, Integer> i : currentMission.plants().entrySet())
            plants.add(new Plant(i.getKey()));

        // list on the left
        for (Plant p : plants)
            seedList.addElement(p);
    }

    private void loadCurrentInsects() {
        insects.clear();

        String msg = "";
        int value  = 0;
        for (Map.Entry<String, Integer> i : currentMission.goal().entrySet()) {
            value += i.getValue();
            msg += i.getValue() + " " + i.getKey() + "(s)";
            insects.add(new Creature(i.getKey()));
        }

        // adapte la barre de progression en fonction de la mission
        levelBar.setValue(0);
        levelBar.setMaximum(value);
        levelBar.setString(msg);
    }

    public ArrayList<Plant> getPlantedPlants() {
        return plantedPlants;
    }

    private void setFullScreen() {
        setUndecorated(true);
        setSize(getToolkit().getScreenSize());
        setLocationRelativeTo(null);
        validate();
    }

    class CustomCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(
            JList list,
            Object value,   // value to display
            int index,      // cell index
            boolean iss,    // is the cell selected
            boolean chf)    // the list and the cell have the focus
        {
            super.getListCellRendererComponent(list, value, index, iss, chf);

            setIcon(new ImageIcon(((Plant) value).seedImages().get(0)));
            setText("");

            return this;
        }
    }
}
