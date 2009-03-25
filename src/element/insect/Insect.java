package element.insect;

import element.XMLLoadableElement;

public class Insect extends XMLLoadableElement {
	private final static String[] ASSETS_NAMES = {"still", "left", "right", "up", "down"};
	
    public Insect(String ID) {
		load(ID);
    }

	protected String[] getAssetsNames() {
        return ASSETS_NAMES;
    }
}
