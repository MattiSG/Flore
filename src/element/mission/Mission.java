package element.mission;

import element.XMLLoadableElement;

public class Mission extends XMLLoadableElement {
	private final static double PARSER_VERSION = 0;
    private final static String[] ASSETS_NAMES = new String[0];

    public Mission(String ID) {
        load(ID);
    }

	public double parserVersion() {
		return PARSER_VERSION;
	}
	
    public String[] getAssetsNames() {
        return ASSETS_NAMES;
    }
}
