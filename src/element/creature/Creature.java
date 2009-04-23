package element.creature;

import java.net.URI;

import element.XMLLoadableElement;

public class Creature extends XMLLoadableElement {
    /*
    int posX;
    int posY;
    int mvt; // 0 = up, 1 = right, 2 = down, 3 = left
    float mvtT;
    */

	private final static double PARSER_VERSION = 0.4;
	private final static String DEFAULT_FOLDER = "../defaults/creature/";
	private final static String[] ASSETS_NAMES = {"still", "left", "right", "up", "down"};
	private final static String ROOT = "creature";
	
    public Creature(String ID) {
		load(ID);
    }
	
	public double parserVersion() {
		return PARSER_VERSION;
	}

	public String[] getAssetsNames() {
        return ASSETS_NAMES;
    }
	
	public URI defaultFolder() {
		try {
			return new URI(DEFAULT_FOLDER);
		} catch (java.net.URISyntaxException e) {
			System.err.println("Default folder is unreachable.");
			throw new RuntimeException(e);
		}
	}
	
	public String rootElement() {
		return ROOT;
	}
}
