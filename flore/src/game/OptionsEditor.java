package game;

import xml.GlobalProperties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SpinnerNumberModel;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class OptionsEditor extends JFrame {
    private JPanel centerPanel = new JPanel();

    public OptionsEditor() {
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        setFontSizeEditor();
        setCreatureSpeedEditor();
        setCreatureZoomEditor();
        setPlantZoomEditor();
        setMissionZoomEditor();

        getContentPane().add(centerPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setFullScreen();
        setVisible(true);
    }

    private void addEditor(String label, Component comp) {
        JPanel editor = new JPanel();
        editor.setLayout(new BoxLayout(editor, BoxLayout.X_AXIS));
        editor.add(new JLabel(label));
        editor.add(comp);
        centerPanel.add(editor);
    }

    private void setCreatureSpeedEditor() {
        JSpinner spinner = new JSpinner();
        JSpinner.NumberEditor spinnerEditor = new JSpinner.NumberEditor(spinner);
        spinner.setEditor(spinnerEditor);
        spinnerEditor.getModel().setMinimum(1);
        spinnerEditor.getModel().setMaximum(50);
        spinnerEditor.getModel().setStepSize(1);
        spinnerEditor.getModel().setValue(GlobalProperties.getDouble("Creature_Speed") * GlobalProperties.STORAGE_NUMBERS_COEF);

        spinnerEditor.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                GlobalProperties.set("Creature_Speed", ""+((SpinnerNumberModel)e.getSource()).getValue());
            }
        });

        addEditor("Vitesse de la créature", spinner);
    }

    private void setCreatureZoomEditor() {
        JSpinner spinner = new JSpinner();
        JSpinner.NumberEditor spinnerEditor = new JSpinner.NumberEditor(spinner);
        spinner.setEditor(spinnerEditor);
        spinnerEditor.getModel().setMinimum(5);
        spinnerEditor.getModel().setMaximum(30);
        spinnerEditor.getModel().setStepSize(0.1);
        spinnerEditor.getModel().setValue(GlobalProperties.getDouble("Creature" + GlobalProperties.ZOOM_SUFFIX) * GlobalProperties.STORAGE_NUMBERS_COEF);

        spinnerEditor.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                GlobalProperties.set("Creature"+GlobalProperties.ZOOM_SUFFIX, ""+((SpinnerNumberModel)e.getSource()).getValue());
            }
        });

        addEditor("Zoom de la créature", spinner);
    }

    private void setPlantZoomEditor() {
        JSpinner spinner = new JSpinner();
        JSpinner.NumberEditor spinnerEditor = new JSpinner.NumberEditor(spinner);
        spinner.setEditor(spinnerEditor);
        spinnerEditor.getModel().setMinimum(5);
        spinnerEditor.getModel().setMaximum(30);
        spinnerEditor.getModel().setStepSize(0.1);
        spinnerEditor.getModel().setValue(GlobalProperties.getDouble("Plant" + GlobalProperties.ZOOM_SUFFIX) * GlobalProperties.STORAGE_NUMBERS_COEF);

        spinnerEditor.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                GlobalProperties.set("Plant"+GlobalProperties.ZOOM_SUFFIX, ""+((SpinnerNumberModel)e.getSource()).getValue());
            }
        });

        addEditor("Zoom de la plante", spinner);
    }

//    private void setMissionZoomEditor() {
//        JSpinner spinner = new JSpinner();
//        JSpinner.NumberEditor spinnerEditor = new JSpinner.NumberEditor(spinner);
//        spinner.setEditor(spinnerEditor);
//        spinnerEditor.getModel().setMinimum(5);
//        spinnerEditor.getModel().setMaximum(30);
//        spinnerEditor.getModel().setStepSize(0.1);
//        spinnerEditor.getModel().setValue(GlobalProperties.getDouble("Mission" + GlobalProperties.ZOOM_SUFFIX) * GlobalProperties.STORAGE_NUMBERS_COEF);
//
//        spinnerEditor.getModel().addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                GlobalProperties.set("Mission"+GlobalProperties.ZOOM_SUFFIX, ""+((SpinnerNumberModel)e.getSource()).getValue());
//            }
//        });
//
//        addEditor("Zoom de la mission", spinner);
//    }


    private void setFontSizeEditor() {
        JSpinner spinner = new JSpinner();
        JSpinner.NumberEditor spinnerEditor = new JSpinner.NumberEditor(spinner);
        spinner.setEditor(spinnerEditor);
        spinnerEditor.getModel().setMinimum(0);
        spinnerEditor.getModel().setMaximum(100);
        spinnerEditor.getModel().setStepSize(1);
        spinnerEditor.getModel().setValue(GlobalProperties.getInteger("font_size") * GlobalProperties.STORAGE_NUMBERS_COEF);

        spinnerEditor.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                GlobalProperties.set("font_size", ""+((SpinnerNumberModel)e.getSource()).getValue());
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
}
