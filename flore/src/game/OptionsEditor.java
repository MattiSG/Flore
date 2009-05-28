package game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SpinnerNumberModel;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import xml.GlobalProperties;

public class OptionsEditor extends JFrame {
    private JPanel centerPanel = new JPanel();
    private Font font = new Font(null, Font.BOLD, GlobalProperties.getInteger("font_size"));

    public OptionsEditor() {
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        setFontSizeEditor();
        setCreatureSpeedEditor();
        setCreatureZoomEditor();
        setPlantZoomEditor();
        setLanguage();
        setAssetType();
        //setMissionZoomEditor();

        getContentPane().add(centerPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setFullScreen();
        setVisible(true);
    }

    private void addEditor(String label, Component comp) {
        JPanel editor = new JPanel();
        editor.setLayout(new BoxLayout(editor, BoxLayout.X_AXIS));

        JLabel jlabel = new JLabel(label);
        jlabel.setFont(font);
        comp.setFont(font);

        editor.add(jlabel);
        editor.add(comp);

        centerPanel.add(editor);
    }

    private void setCreatureSpeedEditor() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(GlobalProperties.getDouble("Creature_Speed") * GlobalProperties.STORAGE_NUMBERS_COEF, 1, 50, 1));

        spinner.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                GlobalProperties.set("Creature_Speed", ""+((SpinnerNumberModel)e.getSource()).getNumber().doubleValue() * GlobalProperties.STORAGE_NUMBERS_COEF);
            }
        });

        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e){
                if (KeyEvent.VK_ESCAPE == e.getKeyCode())
                    dispose();
            }
        });

        addEditor("Vitesse de la créature", spinner);
    }

    private void setCreatureZoomEditor() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(GlobalProperties.getDouble("Creature" + GlobalProperties.ZOOM_SUFFIX) * GlobalProperties.STORAGE_NUMBERS_COEF, 5, 30, 1));

        spinner.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                GlobalProperties.set("Creature"+GlobalProperties.ZOOM_SUFFIX, ""+((SpinnerNumberModel)e.getSource()).getNumber().doubleValue() * GlobalProperties.STORAGE_NUMBERS_COEF);
            }
        });

        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e){
                if (KeyEvent.VK_ESCAPE == e.getKeyCode()) 
                    dispose();
            }
        });

        addEditor("Zoom de la créature", spinner);
    }

    private void setPlantZoomEditor() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(GlobalProperties.getDouble("Plant" + GlobalProperties.ZOOM_SUFFIX) * GlobalProperties.STORAGE_NUMBERS_COEF, 5, 30, 1));

        spinner.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                GlobalProperties.set("Plant"+GlobalProperties.ZOOM_SUFFIX, ""+((SpinnerNumberModel)e.getSource()).getNumber().doubleValue() * GlobalProperties.STORAGE_NUMBERS_COEF);
            }
        });

        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e){
                if (KeyEvent.VK_ESCAPE == e.getKeyCode())
                    dispose();
            }
        });

        addEditor("Zoom de la plante", spinner);
    }

    private void setMissionZoomEditor() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(GlobalProperties.getDouble("Mission" + GlobalProperties.ZOOM_SUFFIX) * GlobalProperties.STORAGE_NUMBERS_COEF, 5, 30, 1));

        spinner.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                GlobalProperties.set("Mission"+GlobalProperties.ZOOM_SUFFIX, ""+((SpinnerNumberModel)e.getSource()).getNumber().doubleValue() * GlobalProperties.STORAGE_NUMBERS_COEF);
            }
        });

        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e){
                if (KeyEvent.VK_ESCAPE == e.getKeyCode()) 
                    dispose();
            }
        });

        addEditor("Zoom de la mission", spinner);
    }

    private void setFontSizeEditor() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel((int)GlobalProperties.getInteger("font_size"), 20, 100, 1));

        spinner.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                GlobalProperties.set("font_size", ""+((SpinnerNumberModel)e.getSource()).getNumber().intValue());
            }
        });

        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e){
                if (KeyEvent.VK_ESCAPE == e.getKeyCode()) 
                    dispose();
            }
        });

        addEditor("Taille de la police", spinner);
    }

    private void setFullScreen() {
        setUndecorated(true);
        setSize(getToolkit().getScreenSize());
        setLocationRelativeTo(null);
        validate();
    }

    private void setLanguage() {
        JComboBox list = new JComboBox(GlobalProperties.AVAILABLE_LANGUAGES);
        list.setSelectedItem(GlobalProperties.get("language"));

        list.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                GlobalProperties.set("language", (String)((JComboBox)e.getSource()).getSelectedItem());
            }
        });

        list.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e){
                if (KeyEvent.VK_ESCAPE == e.getKeyCode()) 
                    dispose();
            }
        });

        addEditor("Langue", list);
    }
    
    private void setAssetType() {
        JComboBox list = new JComboBox(GlobalProperties.AVAILABLE_ASSETS_TYPES);
        list.setSelectedItem(GlobalProperties.get("assets_type"));

        list.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                GlobalProperties.set("assets_type", (String)((JComboBox)e.getSource()).getSelectedItem());
            }
        });

        list.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e){
                if (KeyEvent.VK_ESCAPE == e.getKeyCode()) 
                    dispose();
            }
        });

        addEditor("Thème", list);
    }
}
