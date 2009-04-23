package element;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import java.io.File;
import java.net.URI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import xml.XMLParser;

/**Generic class to handle XML parsing for every distinct element of the game.
 *For example, insects, plants and so on are stored through XML files, and inherit this class to get free XML parsing and assets loading.
 *@author <a href="mailto:alcmene.gs@gmail.com">Matti Schneider-Ghibaudo</a>
 *@version 0.2
 */
public abstract class XMLLoadableElement {
	
	protected XMLParser parser;
	
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
	private final String EXPR_VERSION = rootElement() + "/id/../@version";
	private final String EXPR_ID = rootElement() + "/id";
	private final String EXPR_NAME = rootElement() + "/name[@lang='fr']";
	private final String EXPR_ASSETS = rootElement() + "/assets[@type='standard']";
	private final String EXPR_DESCRIPTION = rootElement() + "/description";
	//@}
	
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
	
	/**The key has to be one from the String array returned by getAssetsNames.
	 *@see	getAssetsNames
	 */
	public List<BufferedImage> getAssets(String key) {
		return assets.get(key);
	}
	
	/**URI of the folder containing the default values for assets.
	 *The images inside have to be named from the key of the corresponding asset, and in the PNG format.
	 */
	protected abstract URI defaultFolder();
	//@}
	
	
	/**@name	Assets (images) management*/
	//@{
	protected Map<String, List<BufferedImage>> loadAssets(String query) {
		Map<String, List<String>> map = parser.getListsMap(query);
		Map<String, List<BufferedImage>> assets = new HashMap<String, List<BufferedImage>>();
		for (String key : map.keySet()) {
			List<String> imagesPaths = map.get(key);
			List<BufferedImage> images;
			try {
				images = loadImages(imagesPaths);
			} catch (RuntimeException e) {
				imagesPaths.clear();
				URI defaultImage = defaultFolder().resolve(key + ".png");
				imagesPaths.add(defaultImage.toString());
				images = loadImages(imagesPaths);
			}
			
			if (images.isEmpty())
				throw new RuntimeException("Erreur à l'initialisation de l'élément " + ID() + " (\"" + name() + "\") : aucune image n'a pu être chargée !\nClé recherchée : " + key);
			
			assets.put(key, images);
		}
		return assets;
	}
	
	/**Returns a list of images from a list of their paths, relative to the XML document of this element.
	 */
	public List<BufferedImage> loadImages(List<String> paths) {
		List<BufferedImage> result = new ArrayList<BufferedImage>(paths.size());
		for (String path : paths) {
			URI imageURI = parser.getDocumentURI();
			try {
				imageURI = imageURI.normalize().resolve(new URI(path));
				File image = new File(imageURI);
				if (! image.exists())
					throw new java.io.IOException();
				result.add(ImageIO.read(image));
			} catch(java.io.IOException e) {
				throw new RuntimeException("Erreur (" + e + ") au chargement d'une image !\nFichier à charger : " + imageURI);
			} catch(java.net.URISyntaxException e) {
				throw new RuntimeException("Erreur (" + e + ") au chargement d'une image !\nFichier à charger : " + imageURI);
			}
		}
		return result;
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
 	 *@see	parserVersion
	 */
	public boolean checkVersion(double version) {
		double parserVersion = parserVersion();
		if (version > parserVersion) {
			System.err.println("The given file's version (" + version + ") is newer than this parser (" + parserVersion + ").\nI'll try to read it, but be aware that you may get errors ! You should update this software.");
//			return false;
		} else if (version < parserVersion) {
			System.err.println("Parser version check temporarily disabled for developement, but be aware that this file uses an obsolete syntax.");
//			return false;
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
	}
	//@}
}
