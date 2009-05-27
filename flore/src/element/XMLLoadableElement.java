package element;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import java.io.File;
import java.net.URI;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Dimension;

import xml.XMLParser;
import xml.GlobalProperties;

/**Generic class to handle XML parsing for every distinct element of the game.
 *For example, insects, plants and so on are stored through XML files, and inherit this class to get free XML parsing and assets loading.
 *@author <a href="mailto:alcmene.gs@gmail.com">Matti Schneider-Ghibaudo</a>
 *@version 0.2
 */
public abstract class XMLLoadableElement {
	/**If set to true, will throw an exception if parser version and parsed file version differ.*/
	public final static boolean STRICT_VERSION_CHECKING = ! GlobalProperties.getBoolean("compatibility_mode");
	
	/**@name	Element description*/
	//@{
	private String ID;
    private String name;
	private String description;
	private Map<String, List<BufferedImage>> assets;
	//@}

	/**@name	Xpath expressions
	 *These expressions are used to parse the XML files.
	 *They represent the common data shared by all game elements represented by XML files.
	 */
	//@{
	private final String	EXPR_VERSION = rootElement() + "/id/../@version",
							EXPR_ID = rootElement() + "/id",
							EXPR_NAME = rootElement() + "/name[@lang='" + GlobalProperties.get("language") + "']",
							EXPR_ASSETS = rootElement() + "/assets[@type='" + GlobalProperties.get("assets_type") + "']",
							EXPR_DESCRIPTION = rootElement() + "/description",
							WIDTH_ATTRIBUTE = "width",
							HEIGHT_ATTRIBUTE = "height";
	//@}
	
	protected XMLParser parser;
	
	/**@name	Getters*/
	//@{
	public String ID() {
		return ID;
	}
	
	public String name() {
		return name;
	}
	
	public String description() {
		return description;
	}
	
	/**Version check.
	 *Since the parsing work is different for every file type, this value has to be redefined for every class wanting to parse an XML file.
	 *@see	checkVersion
	 */
	public abstract double parserVersion();
	
	/**The names of all assets nodes to load.
	 *Designed to be overriden for specific needs by the inheriting classes.
	 *@see	loadAssets
	 */
	public abstract String[] getAssetsNames();
	
	/**The tag name of the root element of the XML document.
	 *Designed to be overriden for specific needs by the inheriting classes.
	 *For example, the "Mission" class will most probably return "mission".
	 */
	public abstract String rootElement();
	
	/**A shortcut for assets that have only one image and a not a list of images.
	 *Equivalent to getAssets(key).get(0).
	 *@see	getAssets
	 */
	public BufferedImage getAsset(String key) {
		return getAssets(key).get(0);
	}
	
	/**The key has to be one from the String array returned by getAssetsNames.
	 *@see	getAssetsNames
	 */
	public List<BufferedImage> getAssets(String key) {
		return assets.get(key);
	}
	
	/**URI of the folder containing the default values for assets.
	 *The images inside have to be named from the key of the corresponding asset, and in the PNG format.
	 */
	protected URI defaultFolder() {
		try {
			File defaultBase = new File(GlobalProperties.get("default_folder"));
			return new URI(defaultBase.toURI() + "/" + this.getClass().getSimpleName() + "/");
		} catch (java.net.URISyntaxException e) {
			throw new RuntimeException("Impossible d'obtenir le dossier par défaut pour la classe \"" + this.getClass().getSimpleName() + "\" !\n" + e);
		}
	}
	
	/**Zoom level (Coefficient multiplicatif à appliquer aux images au chargement).
	 *Loaded from the config file.
	 */
	public double zoom() {
		return GlobalProperties.getDouble(this.getClass().getSimpleName() + GlobalProperties.ZOOM_SUFFIX);
	}
	//@}
	
	
	/**@name	Assets (images) management*/
	//@{
	protected Map<String, List<BufferedImage>> loadAssets(String query) {
		Map<String, List<Node>> map = parser.getNodesListsMap(query);
		Map<String, List<BufferedImage>> assets = new HashMap<String, List<BufferedImage>>();
		for (String key : map.keySet()) {
			List<Node> imagesNodes = map.get(key);		
			List<BufferedImage> images = new ArrayList<BufferedImage>(imagesNodes.size());
			for (Node node : imagesNodes) {
				try {
					images.add(loadImage(node));
				} catch (Exception e) {
					node.setTextContent(defaultFolder().resolve(key + ".png").toString());
					images.add(loadImage(node));
				}
			}
			
			if (images.isEmpty())
				throw new RuntimeException("Erreur à l'initialisation de l'élément " + ID() + " (\"" + name() + "\") : aucune image n'a pu être chargée !\nClé recherchée : " + key);

			assets.put(key, images);
		}
		return assets;
	}
	
	/**Resizes the given image to the given dimensions.
	 */
	private BufferedImage resize(BufferedImage image, Dimension dimensions) {
//		AffineTransform t = new AffineTransform();
//        t.scale(dimensions.getWidth() / image.getWidth(), dimensions.getHeight() / image.getHeight());
//        AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_BILINEAR);
//		java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
//		java.awt.GraphicsDevice gs = ge.getDefaultScreenDevice();
//		java.awt.GraphicsConfiguration gc = gs.getDefaultConfiguration();
//		BufferedImage result = gc.createCompatibleImage((int) dimensions.getWidth(), (int) dimensions.getHeight(), java.awt.Transparency.TRANSLUCENT);
//        return op.filter(image, result);
		java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
		java.awt.GraphicsDevice gs = ge.getDefaultScreenDevice();
		java.awt.GraphicsConfiguration gc = gs.getDefaultConfiguration();
//		double ratio = image.getWidth() / image.getHeight();
		BufferedImage result = gc.createCompatibleImage((int) (dimensions.getWidth() * zoom()), (int) (dimensions.getHeight() * zoom()), java.awt.Transparency.TRANSLUCENT);
		java.awt.Graphics2D g = result.createGraphics();
		g.drawImage(image, 0, 0, (int) (dimensions.getWidth() * zoom()), (int) (dimensions.getHeight() * zoom()), null);
		g.dispose();
		return result;
	}
	
	/**Returns a list of buffered images from a list of their paths, relative to the XML document of this element.
	 */
	public List<BufferedImage> loadImages(List<String> paths) {
		List<BufferedImage> result = new ArrayList<BufferedImage>(paths.size());
		for (String path : paths)
			result.add(loadImage(path));
		return result;
	}
	
	/**Returns an image, correctly loaded and resized according to the width and height attributes of the node, if available, and with the zoom factor from the zoom() method.
	 *@throws	RuntimeException	if the given image couldn't be loaded.
	 *@see	zoom
	 *@see	resize
	 *@see	loadImage(String)
	 */
	protected BufferedImage loadImage(Node node) {
		int width, height;
		try {
			width = new Integer(node.getAttributes().getNamedItem(WIDTH_ATTRIBUTE).getNodeValue());
			height = new Integer(node.getAttributes().getNamedItem(HEIGHT_ATTRIBUTE).getNodeValue());
		} catch (Exception e) {
			width = height = 0;
		}
		BufferedImage result = loadImage(node.getTextContent());
		if (width <= 0 || height <= 0) {
			width = result.getWidth();
			height = result.getHeight();
		}
		return resize(result, new Dimension(width, height));
	}
	
	/**Returns a loaded image from the given path.
	 *@throws	RuntimeException	if the given image couldn't be loaded.
	 */
	private BufferedImage loadImage(String path) {
		URI imageURI = parser.getDocumentURI();
		try {
			imageURI = imageURI.normalize().resolve(new URI(path));
			File image = new File(imageURI);
			if (! image.exists())
				throw new java.io.IOException("L'image demandée n'existe pas.");
			return ImageIO.read(image);
		} catch(Exception e) {
			throw new RuntimeException("Erreur (" + e + ") au chargement d'une image !\nFichier à charger : " + imageURI);
		}
	}
	
	/**Tells whether the given String is a valid key for an asset or not.
	 *I.e., tells if getAssets().contains(key);
	 */
	public boolean isAssetKey(String key) {
		String[] assetsKeys = getAssetsNames();
		for (int i = 0; i < assetsKeys.length; i++)
			if (key.equals(assetsKeys[i]))
				return true;
		return false;
	}
	//@}
	
	
	/**@name	XML parsing*/
	//@{
	
	/**Tells whether the loaded file is parsable or not.
 	 *@see	checkVersion
	 */
	public boolean checkVersion() {
		return checkVersion(parser.getDouble(EXPR_VERSION));
	}
		
	/**Tells whether the given file format version is parsable or not.
	 *Be careful as it may return true even though file version is not compatible, because the STRICT_VERSION_CHECKING mode would be deactivated.
 	 *@see	parserVersion
	 *@see	STRICT_VERSION_CHECKING
	 */
	public boolean checkVersion(double version) {
		double parserVersion = parserVersion();
		if (version > parserVersion) {
			System.err.println("File ID \"" + ID() + "\"'s version (" + version + ") is newer than this parser (" + parserVersion + ").\nYou should update this software.");
			return ! STRICT_VERSION_CHECKING;
		} else if (version < parserVersion) {
			System.err.println("Please note that file ID \"" + ID() + "\" uses an obsolete syntax (parser version : " + parserVersion + ", file syntax version : " + version + ").");
			return ! STRICT_VERSION_CHECKING;
		}
		return true;
	}	
	
	/**Loads an Element file from its ID.
	 *Just a gateway to loadFromXML(), hiding the file hierarchy to clients.
	 *@param	ID	the ID of the element to load.
	 *@throws	IllegalArgumentException	if the given file isn't parsable or doesn't exist.
	 */
	protected void load(String ID) {
		loadFromXML("../ressources/elements/" + ID + "/description.xml");
		if (! checkVersion())
			throw new RuntimeException("Incorrect file version, stopping execution.");
		if (! ID.equals(ID()))
			throw new RuntimeException("Folder ID (" + ID + ") and parsed inner ID (" + ID() + ") do not match !");
	}
	
	/**Tries to initialize a new Element from an XML description file.
	 *@param	file	the XML file to parse.
	 *@throws	IllegalArgumentException	if the given file isn't parsable by this version of the parser.
	 */
    private void loadFromXML(String file) {
		parser = new XMLParser(file);
		
		ID = parser.get(EXPR_ID);
		name = parser.get(EXPR_NAME);
		description = parser.get(EXPR_DESCRIPTION);
		
		assets = loadAssets(EXPR_ASSETS);
		
		parsePrivates();
	}
	
	/**The place where to put all element-specific parsing.
	 */
	protected abstract void parsePrivates();
	//@}
}
