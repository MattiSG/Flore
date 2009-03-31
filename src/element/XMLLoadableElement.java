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
abstract public class XMLLoadableElement {
	
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
	private final static String EXPR_VERSION = "//id/../@version";
	private final static String EXPR_ID = "//id";
	private final static String EXPR_NAME = "//name[@lang='fr']";
	private final static String EXPR_ASSETS = "//assets[@type='standard']";
	private final static String EXPR_DESCRIPTION = "//description";
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
	
	/**The key has to be one from the String array returned by getAssetsNames.
	 *@see	getAssetsNames
	 */
	public List<BufferedImage> getAssets(String key) {
		return assets.get(key);
	}
	//@}
	
	
	/**@name	XML parsing*/
	//@{
	/**Tells whether the given file format version is parsable or not.
 	 *@see	parserVersion
	 */
	public boolean checkVersion(double version) {
		double parserVersion = parserVersion();
		if (version > parserVersion) {
			System.err.println("The given file's version (" + version + ") is newer than this parser (" + parserVersion + ").\nI'll try to read it, but be aware that you may get errors ! You should update this software.");
			return true;
		} else if (version < parserVersion) {
			System.err.println("Parser version check temporarily disabled for developement, but be aware that this file uses an obsolete syntax.");
			return true;
		}
		return version == parserVersion;
	}	
	
	/**Loads an Element file from its ID.
	 *Just a gateway to loadFromXML(), hiding the file hierarchy to clients.
	 *@param	ID	the ID of the element to load.
	 *@throws	IllegalArgumentException	if the given file isn't parsable or doesn't exist.
	 */
	protected void load(String ID) {
		loadFromXML("../ressources/elements/" + ID + "/description.xml");
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
	
	protected Map<String, List<BufferedImage>> loadAssets(String query) {
		Map<String, List<String>> map = parser.getListsMap(query);
		Map<String, List<BufferedImage>> assets = new HashMap<String, List<BufferedImage>>();
		for (String key : map.keySet()) {
			List<String> imagesPaths = map.get(key);
			if (imagesPaths.size() == 0)
				throw new RuntimeException("Erreur à l'initialisation de l'élément " + ID() + " (\"" + name() + "\") : aucune image n'a pu être chargée !\nClé recherchée : " + key);
			
			List<BufferedImage> images = new ArrayList<BufferedImage>(imagesPaths.size());
			for (int i = 0; i < imagesPaths.size(); i++) {
				URI imageURI = null;
				try {
					imageURI = parser.getDocumentURI();
					imageURI = imageURI.normalize().resolve(imagesPaths.get(i));
					images.add(ImageIO.read(new File(imageURI)));
					if (! new File(imageURI).exists())
						throw new java.io.IOException();
				} catch(java.io.IOException e) {
					throw new RuntimeException("Erreur (" + e + ") au chargement d'une image !\nFichier à charger : " + imageURI);
				}
			}
			assets.put(key, images);
		}
		return assets;
	}
	//@}
}
