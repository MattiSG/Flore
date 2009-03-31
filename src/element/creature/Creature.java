package element.creature;

import element.XMLLoadableElement;

public class Creature extends XMLLoadableElement {
	private final static double PARSER_VERSION = 0;
	private final static String[] ASSETS_NAMES = {"still", "left", "right", "up", "down"};
	
    public Creature(String ID) {
		load(ID);
    }
	
	public double parserVersion() {
		return PARSER_VERSION;
	}

	public String[] getAssetsNames() {
        return ASSETS_NAMES;
    }
}
