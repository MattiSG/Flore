package element.mission;

import element.XMLLoadableElement;

import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.awt.image.BufferedImage;

import org.w3c.dom.Node;
import java.net.URI;

public class Mission extends XMLLoadableElement {
	private final static double PARSER_VERSION = 0.22;
    private final static String[] ASSETS_NAMES = {"icon", "sky", "grass", "sun", "hole", "hole_full", "hole_selected", "hole_full_selected", "cloud"};
	private final static String ROOT = "mission";
	private final String	HINTS_EXPR = rootElement() + "/hints/*",
							PLANTS_EXPR = rootElement() + "/plants/*",
							GOAL_EXPR = rootElement() + "/goal/*";
	
	private int	difficulty,
				holes,
				timeLimit;
	/**Increasing helpfulness.
	 *I.e. the last element in the list is the answer.
	 */
	private PriorityQueue<String> hints;
	private Map<String, Integer>	plants,
									goal;

    public Mission(String ID) {
        load(ID);
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
	
	public PriorityQueue<String> hints() {
		return hints;
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
		List<String> path = new ArrayList<String>(1);
		for (String tag : tags) {
			if (! isAssetKey(tag))
				throw new RuntimeException("One of the assets nodes has unknown value \"" + tag + "\".");
			
			List<BufferedImage> images;
			path.add(parser.get(query + "/" + tag));
			try {
				images = loadImages(path);
			} catch (RuntimeException e) {
				path.clear();
				URI defaultImage = defaultFolder().resolve(tag + ".png");
				path.add(defaultImage.toString());
				images = loadImages(path);
			}
			
			if (images.isEmpty())
				throw new RuntimeException("Erreur à l'initialisation de l'élément " + ID() + " (\"" + name() + "\") : aucune image n'a pu être chargée !\nClé recherchée : " + tag);
			
			result.put(tag, images);
			path.clear();
		}
		return result;
    }
	
	protected void parsePrivates() {
		parseHints();
		parsePlants();
		parseGoal();
		difficulty = parser.getDouble(rootElement() + "/difficulty").intValue();
		holes = parser.getDouble(rootElement() + "/holes").intValue();
		timeLimit = parser.getDouble(rootElement() + "/time_limit").intValue();
	}
	
	/**Populates the hints list.*/
	private void parseHints() {
		List<Node> hintsNodes = parser.getNodes(HINTS_EXPR);
		Map<Integer, String> hintsMap = new TreeMap<Integer, String>(); //TODO : utiliser une file directement.
		for (Node node : hintsNodes)
        {
			hintsMap.put(-1 * Integer.decode(node.getAttributes().getNamedItem("level").getNodeValue()), node.getTextContent());
        }
		hints = new PriorityQueue<String>(hintsMap.values());
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
