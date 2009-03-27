package game;

import element.XMLLoadableElement;
import element.plant.Plant;
import element.creature.Creature;
import element.mission.Mission;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JMenuBar;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JOptionPane;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

public class MainWindow extends JFrame {
    private JMenuBar         menuBar       = new JMenuBar();
    private JLabel           statusBar     = new JLabel("Initialisation");
    private JProgressBar     levelBar      = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
    private GameView         gameView      = new GameView();
    private DocView          docView       = new DocView();
    private MissionView      missionView   = new MissionView();
    private JToggleButton    docButton     = new JToggleButton("Documentation", new ImageIcon("../ressources/images/doc.png"));
    private JToggleButton    missionButton = new JToggleButton("Mission", new ImageIcon("../ressources/images/doc.png"));
    private DefaultListModel seedList      = new DefaultListModel();
    private JList            seedListView  = new JList(seedList);
    private JPanel           centerPanel   = new JPanel();
    private CardLayout       centerLayout  = new CardLayout();

    private ArrayList<Plant>    plants   = new ArrayList<Plant>();
    private ArrayList<Creature> insects  = new ArrayList<Creature>();
    private ArrayList<Mission>  missions = new ArrayList<Mission>();

    public MainWindow() {
        loadPlants();
        loadInsects();
        loadMissions();

        setMenu();
        setSeedList();

        docButton.setVerticalTextPosition(JToggleButton.BOTTOM);
        docButton.setHorizontalTextPosition(JToggleButton.CENTER);
        docButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (docButton.isSelected())
                {
                    missionButton.setSelected(false);
                    centerLayout.show(centerPanel, "doc");
                    docView.display((XMLLoadableElement) seedListView.getSelectedValue());
                }
                else
                    centerLayout.show(centerPanel, "game");
            }
        });

        missionButton.setVerticalTextPosition(JToggleButton.BOTTOM);
        missionButton.setHorizontalTextPosition(JToggleButton.CENTER);
        missionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (missionButton.isSelected())
                {
                    docButton.setSelected(false);
                    centerLayout.show(centerPanel, "mission");
                    missionView.display((XMLLoadableElement) seedListView.getSelectedValue());
                }
                else
                    centerLayout.show(centerPanel, "game");
            }
        });

        levelBar.setValue(50);
        levelBar.setStringPainted(true);
        levelBar.setString("Moitié du niveau !");

        centerPanel.setLayout(centerLayout);
        centerPanel.add(gameView,     "game");
        centerPanel.add(docView,      "doc");
        centerPanel.add(missionView,  "mission");


        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);
        c.gridwidth  = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;

        setLayout(layout);

        layout.setConstraints(docButton, c);
        getContentPane().add(docButton);

        c.gridwidth  = 2;
        c.gridheight = 4;
        c.weightx = 1;
        c.weighty = 1;
        layout.setConstraints(centerPanel, c);
        getContentPane().add(centerPanel);

        c.gridwidth  = GridBagConstraints.REMAINDER;
        c.gridheight = 4;
        c.weightx = 0;
        c.weighty = 0;
        JScrollPane s = new JScrollPane(seedListView);
        layout.setConstraints(s, c);
        getContentPane().add(s);

        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth  = 1;
        c.gridheight = 1;
        layout.setConstraints(missionButton, c);
        getContentPane().add(missionButton);

        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth  = 1;
        c.gridheight = 2;
        layout.setConstraints(levelBar, c);
        getContentPane().add(levelBar);

        c.gridy = 4;
        c.gridwidth  = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        layout.setConstraints(statusBar, c);
        getContentPane().add(statusBar);

        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setFullScreen();
        setVisible(true);

        run();
    }

    private void run() {
        int delay = 10000;
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // fait grandir ta plante ici !
                //statusBar.setText("tick: " + e.getWhen());
                //gameView.grow();
                //gameView.repaint();
            }
        };
        new Timer(delay, taskPerformer).start();
    }

    private void loadPlants() {
        plants.add(new Plant("rosa"));
    }

    private void loadInsects() {
    }

    private void loadMissions() {
        missions.add(new Mission());
    }

    private void setSeedList() {
        seedListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (Plant p : plants)
            seedList.addElement(p);
    }

    private void setMenu() {
    }

    private void setFullScreen() {
        setUndecorated(true);
        setSize(getToolkit().getScreenSize());
        setLocationRelativeTo(null);
        validate();
    }
}
