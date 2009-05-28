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
	public final static double STORAGE_NUMBERS_COEF = 10.0; //this is to circumvent a GUI limitation (can't edit below integers). Yeah, that sucks.
	
	private final static int	FONT_SIZE_MIN = 10,
								FONT_SIZE_MAX = 120,
								ZOOM_MIN = 5,
								ZOOM_MAX = 40;
	
    private JPanel centerPanel = new JPanel();
    private Font font = new Font(null, Font.BOLD, GlobalProperties.getInteger("font_size"));

    public OptionsEditor() {
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        setFontSizeEditor();
        setCreatureZoomEditor();
        setPlantZoomEditor();
        setMissionZoomEditor();
        setAssetType();
        setLanguage();

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

    private void setCreatureZoomEditor() {
        addEditor("Taille des créatures", zoomSpinner("Creature"));
    }

    private void setPlantZoomEditor() {
        addEditor("Taille des plantes", zoomSpinner("Plant"));
    }

    private void setMissionZoomEditor() {
        addEditor("Taille des décors", zoomSpinner("Mission"));
    }
	
	private JSpinner zoomSpinner(final String element) {
		int value = (int) (GlobalProperties.getDouble(element + GlobalProperties.ZOOM_SUFFIX) * STORAGE_NUMBERS_COEF);
		if (value < ZOOM_MIN || value > ZOOM_MAX)
			value = new Integer(GlobalProperties.defaults().getProperty(element + GlobalProperties.ZOOM_SUFFIX));
		JSpinner result = new JSpinner(new SpinnerNumberModel(value, ZOOM_MIN, ZOOM_MAX, 5));
		result.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				GlobalProperties.set(element + GlobalProperties.ZOOM_SUFFIX, "" + ((SpinnerNumberModel) e.getSource()).getNumber().doubleValue() / STORAGE_NUMBERS_COEF);
			}
		});
        ((JSpinner.DefaultEditor) result.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ESCAPE == e.getKeyCode()) 
					dispose();
			}
		});
		return result;
	}

    private void setFontSizeEditor() {
		int value = (int) GlobalProperties.getInteger("font_size");
		if (value < FONT_SIZE_MIN || value > FONT_SIZE_MAX)
			value = new Integer(GlobalProperties.defaults().getProperty("font_size"));
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, FONT_SIZE_MIN, FONT_SIZE_MAX, 5));

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
