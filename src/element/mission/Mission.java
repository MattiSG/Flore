package element.mission;

import element.XMLLoadableElement;

import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import org.w3c.dom.Node;

public class Mission extends XMLLoadableElement {
	private final static double PARSER_VERSION = 0.2;
    private final static String[] ASSETS_NAMES = {"icon", "sky", "grass", "sun", "hole"};
	private final static String ROOT = "mission";
	private final String	HINTS_EXPR = rootElement() + "/hints/*",
							PLANTS_EXPR = rootElement() + "/plants/*",
							GOAL_EXPR = rootElement() + "/goal/*";
	
	private int	difficulty,
				holes,
				timeLimit;
	/**Ordered list, with increasing helpfulness.
	 *I.e. the last element in the list is the answer.
	 */
	private SortedMap<Integer, String> hints;
	private Map<String, Integer>	plants,
									goal;

    public Mission(String ID) {
        load(ID);
		parseHints();
		parsePlants();
		parseGoal();
		difficulty = parser.getDouble(rootElement() + "/difficulty").intValue();
		holes = parser.getDouble(rootElement() + "/holes").intValue();
		timeLimit = parser.getDouble(rootElement() + "/time_limit").intValue();
    }
	
	/**@name	Getters*/
	//@{
	public int difficulty() {
		return difficulty;
	}
	
	public int holes() {
		return holes;
	}
	
	public int timeLimit() {
		return timeLimit;
	}
	
	public List<String> hints() {
		return new ArrayList<String>(hints.values());
	}
	
	/**Returns a map whose keys are plants IDs and values are the number of seeds allowed for this ID.
	 */
	public Map<String, Integer> plants() {
		return plants;
	}
	
	/**Returns a map whose keys are creatures IDs and values are the number needed for this ID.
	 */
	public Map<String, Integer> goal() {
		return goal;
	}
	//@}
	
	
	/**@name	Unmarshalling*/
	//@{
	public double parserVersion() {
		return PARSER_VERSION;
	}
	
    public String[] getAssetsNames() {
        return ASSETS_NAMES;
    }
	
	public String rootElement() {
		return ROOT;
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
	
	/**Populates the hints list.*/
	private void parseHints() {
		List<Node> hintsNodes = parser.getNodes(HINTS_EXPR);
		hints = new TreeMap<Integer, String>();
		for (Node node : hintsNodes)
			hints.put(Integer.decode(node.getAttributes().getNamedItem("level").getNodeValue()), node.getTextContent());
	}
	
	/**Parses a Map whose keys are the value of the given attribute name and values are the value of the node itself.
	 *@param	nodesExpr	the Xpath expression that represents the list of nodes to parse the map from.
	 *@param	attributeName	the attribute to parse the keys of the map from.
	 */
	private Map<String, Integer> parseStringIntegerMap(String nodesExpr, String attributeName) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		List<Node> nodes = parser.getNodes(nodesExpr);
		result = new HashMap<String, Integer>();
		for (Node node : nodes)
			result.put(node.getAttributes().getNamedItem(attributeName).getNodeValue(), Integer.parseInt(node.getTextContent()));
		return result;
	}
	
	/**Populates the plants map.*/
	private void parsePlants() {
		plants = parseStringIntegerMap(PLANTS_EXPR, "id");
	}
	
	/**Populates the plants map.*/
	private void parseGoal() {
		goal = parseStringIntegerMap(GOAL_EXPR, "id");
	}
	//@}

}
