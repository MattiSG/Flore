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

import java.awt.Dimension;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.ArrayList;

public class MainWindow extends JFrame {
    private JMenuBar         menuBar       = new JMenuBar();
    private JLabel           statusBar     = new JLabel("Initialisation");
    private JProgressBar     levelBar      = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
    private DefaultListModel seedList      = new DefaultListModel();
    private JList            seedListView  = new JList(seedList);
    private GameView         gameView      = new GameView();
    private SIVOXDevint      player        = new SIVOXDevint();

    private ArrayList<Plant>    plants   = new ArrayList<Plant>();
    private ArrayList<Creature> insects  = new ArrayList<Creature>();
    private ArrayList<Mission>  missions = new ArrayList<Mission>();

    private ArrayList<Plant> plantedPlants = new ArrayList<Plant>(gameView.HOLES_NUMBER);
    private Mission          currentMission;

    public MainWindow() {
        for (int i = 0; i < gameView.HOLES_NUMBER; ++i)
            plantedPlants.add(null);

        loadPlants();
        loadInsects();
        loadMissions();

        setMenu();
        setSeedList();

        levelBar.setValue(10);
        levelBar.setStringPainted(true);
        levelBar.setString("10 insectes");

        seedListView.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyCode()) {
                    int i = gameView.getSelectedHoleIndex();
                    plantedPlants.set(i, new Plant(((Plant) seedListView.getSelectedValue()).ID()));
                    gameView.repaint();
                } else if (KeyEvent.VK_RIGHT == e.getKeyCode()) {
                    gameView.setSelectedHoleNext();
                } else if (KeyEvent.VK_LEFT == e.getKeyCode()) {
                    gameView.setSelectedHolePrevious();
                } else if (KeyEvent.VK_ESCAPE == e.getKeyCode()) {
                    statusBar.setText("Au revoir !");
                    dispose();
                }
            }
        });
        seedListView.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                Plant p = (Plant) seedListView.getSelectedValue();
                statusBar.setText(p.name());
            }
        });
        JScrollPane s = new JScrollPane(seedListView);
        s.setPreferredSize(new Dimension(200, 400));

        getContentPane().add(gameView,  BorderLayout.CENTER);
        getContentPane().add(s,         BorderLayout.EAST);
        getContentPane().add(levelBar,  BorderLayout.WEST);
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setFullScreen();
        setVisible(true);

        run();
    }

    private void run() {
        currentMission = missions.get(0);
        play(currentMission.description());
        statusBar.setText(currentMission.description());

        seedListView.requestFocus();
        seedListView.setSelectedIndex(0);

        int delay = 100;
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // fait grandir ta plante ici !
                //statusBar.setText("tick: " + e.getWhen());
                for (Plant p : plantedPlants)
                    if (p != null)
                    {
                        p.grow();
                        System.out.println(p.name());
                    }
                gameView.repaint();
            }
        };
        new Timer(delay, taskPerformer).start();
    }

    private void play(String text) {
        player.stop();
        player.playText(text);
    }

    private void loadPlants() {
        plants.add(new Plant("rosa"));
        plants.add(new Plant("mimosa"));
    }

    private void loadInsects() {
    }

    private void loadMissions() {
        Mission temp = new Mission("specs_mission");
        missions.add(temp);
    }

    private void setMenu() {
    }

    private void setFullScreen() {
        setUndecorated(true);
        setSize(getToolkit().getScreenSize());
        setLocationRelativeTo(null);
        validate();
    }

    private void setSeedList() {
        seedListView.setCellRenderer(new CustomCellRenderer());
        seedListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (Plant p : plants)
            seedList.addElement(p);
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

    public ArrayList<Plant> getPlantedPlants() {
        return plantedPlants;
    }
}
