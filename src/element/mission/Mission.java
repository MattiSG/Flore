package element.mission;

import element.XMLLoadableElement;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import org.w3c.dom.Node;

public class Mission extends XMLLoadableElement {
	private final static double PARSER_VERSION = 0.2;
    private final static String[] ASSETS_NAMES = {"icon", "sky", "grass", "sun", "hole"};

    public Mission(String ID) {
        load(ID);
    }

	protected Map<String, List<BufferedImage>> loadAssets(String query) {
		Map<String, List<BufferedImage>> result = new HashMap<String, List<BufferedImage>>();
		List<String> tags = parser.getTags(query + "/*");
		List<String> paths = new ArrayList<String>(1);
		for (String tag : tags) {
			if (! isAssetKey(tag))
				throw new RuntimeException("One of the assets nodes has unknown value \"" + tag + "\".");
			paths.add(parser.get(query + "/" + tag));
			result.put(tag, loadImages(paths));
		}
		return result;
    }

	public double parserVersion() {
		return PARSER_VERSION;
	}
	
    public String[] getAssetsNames() {
        return ASSETS_NAMES;
    }
}
