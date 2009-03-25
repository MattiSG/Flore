package element.creature;

import element.XMLLoadableElement;

public class Creature extends XMLLoadableElement {
	private final static String[] ASSETS_NAMES = {"still", "left", "right", "up", "down"};
	
    public Creature(String ID) {
		load(ID);
    }

	public String[] getAssetsNames() {
        return ASSETS_NAMES;
    }
}
