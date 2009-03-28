package element.mission;

import element.XMLLoadableElement;

public class Mission extends XMLLoadableElement {
    private final static String[] ASSETS_NAMES = new String[0];

    public Mission(String ID) {
        load(ID);
    }

    public String[] getAssetsNames() {
        return ASSETS_NAMES;
    }
}
