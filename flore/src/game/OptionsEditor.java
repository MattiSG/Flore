package game;

import xml.GlobalProperties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.BoxLayout;

import java.awt.Dimension;
import java.awt.BorderLayout;

public class OptionsEditor extends JFrame {
    public OptionsEditor() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));


        JPanel fontSizeEditor = new JPanel();
        fontSizeEditor.setLayout(new BoxLayout(fontSizeEditor, BoxLayout.X_AXIS));

        JSpinner spinner = new JSpinner();
        JSpinner.NumberEditor spinnerEditor = new JSpinner.NumberEditor(spinner);
        spinner.setEditor(spinnerEditor);
        spinnerEditor.getModel().setMinimum(0);
        spinnerEditor.getModel().setMaximum(100);
        spinnerEditor.getModel().setStepSize(1);
        spinnerEditor.getModel().setValue(GlobalProperties.getInteger("font_size"));

        fontSizeEditor.add(new JLabel("Taille de la police")); 
        fontSizeEditor.add(spinner);

        fontSizeEditor.setPreferredSize(new Dimension(400, 100));

        centerPanel.add(fontSizeEditor);
        
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setFullScreen();
        setVisible(true);
    }

    private void setFullScreen() {
        setUndecorated(true);
        setSize(getToolkit().getScreenSize());
        setLocationRelativeTo(null);
        validate();
    }
}
