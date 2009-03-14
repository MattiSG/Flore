package game;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.DefaultListModel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class MainWindow extends JFrame {
    private JMenuBar     menuBar   = new JMenuBar();
    private JLabel       statusBar = new JLabel("Initialisation");
    private JProgressBar levelBar  = new JProgressBar(JProgressBar.VERTICAL, 0, 20);
    private JButton      docButton = new JButton("Documentation", new ImageIcon("../ressources/images/doc.png"));
    private GameView     gameView  = new GameView();
    private DefaultListModel seedList      = new DefaultListModel();
    private JList            seedListView  = new JList(seedList);

    public MainWindow() {
        setMenu();
        setSeedList();

        docButton.setVerticalTextPosition(JButton.BOTTOM);
        docButton.setHorizontalTextPosition(JButton.CENTER);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(docButton);
        leftPanel.add(levelBar);

        getContentPane().add(statusBar, BorderLayout.SOUTH);
        getContentPane().add(leftPanel, BorderLayout.WEST);
        getContentPane().add(new JScrollPane(seedListView),  BorderLayout.EAST);
        getContentPane().add(gameView,  BorderLayout.CENTER);

        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setFullScreen();
        setVisible(true);
    }

    private void setSeedList() {
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
