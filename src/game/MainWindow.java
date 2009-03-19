package game;

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
import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    private JMenuBar     menuBar   = new JMenuBar();
    private JLabel       statusBar = new JLabel("Initialisation");
    private JProgressBar levelBar  = new JProgressBar(JProgressBar.VERTICAL, 0, 20);
    private GameView     gameView  = new GameView();
    private DocView      docView   = new DocView();
    private JToggleButton    docButton    = new JToggleButton("Documentation", new ImageIcon("../ressources/images/doc.png"));
    private DefaultListModel seedList     = new DefaultListModel();
    private JList            seedListView = new JList(seedList);
    private JPanel           centerPanel  = new JPanel();
    private CardLayout       centerLayout = new CardLayout();

    public MainWindow() {
        setMenu();
        setSeedList();

        docButton.setVerticalTextPosition(JToggleButton.BOTTOM);
        docButton.setHorizontalTextPosition(JToggleButton.CENTER);
        docButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                centerLayout.next(centerPanel);
            }
        });

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(docButton);
        leftPanel.add(levelBar);

        centerPanel.setLayout(centerLayout);
        centerPanel.add(gameView, "game");
        centerPanel.add(docView,  "doc");

        getContentPane().add(statusBar, BorderLayout.SOUTH);
        getContentPane().add(leftPanel, BorderLayout.WEST);
        getContentPane().add(new JScrollPane(seedListView),  BorderLayout.EAST);
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setFullScreen();
        setVisible(true);

        run();
    }

    private void run() {
        int delay = 1000;
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // fait grandir ta plante ici !
            }
        };
        new Timer(delay, taskPerformer).start();
    }

    private void setSeedList() {
        seedListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        seedList.addElement("Graîne de rosier");
        seedList.addElement("Graîne de mimosa");
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
