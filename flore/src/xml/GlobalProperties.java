package xml;

import java.io.File;
import java.net.URI;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.awt.Font;

/**This class holds the global options used throughout the game.
 */
public class GlobalProperties {
	/**The types of assets available to the player, in decreasing standardization order.
	 *Put otherwise, the order of the types in this array gives the importance of the different types: ALL elements should provide particular "standard" assets, and may optionnally give others, in decreasing importance.
	 */
	public static final String[] AVAILABLE_ASSETS_TYPES = {"standard", "high_contrast"}; //WARNING : ORDER DOES MATTER ! ALWAYS PUT STANDARD IN THE FIRST PLACE !
	
	public static final String[] AVAILABLE_LANGUAGES = {"fr"};
	
	/**Get default options values.
	 *Feel free to add your own values !
	 */
	public static Properties defaults() {
		Properties result = new Properties();
		result.setProperty(TEST_KEY, TEST_VALUE);
		result.setProperty("debug", "false");
		result.setProperty("compatibility_mode", "false");
		result.setProperty("Creature" + ZOOM_SUFFIX, "1.5");
		result.setProperty("Plant" + ZOOM_SUFFIX, "1");
		result.setProperty("Mission" + ZOOM_SUFFIX, "1");
		result.setProperty("default_folder", "../ressources/elements/defaults/");
		result.setProperty("Creature_Speed", "3");
		result.setProperty("coef_croissance_plantes", "1");
		result.setProperty("language", AVAILABLE_LANGUAGES[0]);
		result.setProperty("assets_type", AVAILABLE_ASSETS_TYPES[0]);
		result.setProperty("font_size", "40");
		return result;
	}
	
	
	
	public static final String	TEST_KEY = "authors",
								TEST_VALUE = "Alcmene, Tipoun, Wiz :)",
								ZOOM_SUFFIX = "_zoom";
	
	private static final String PROP_FILE_PATH = "../ressources/flore_config.xml";
	private static File PROP_FILE;
	private static final double DEFAULT_DOUBLE_VALUE = 1.0;
	private static final int    DEFAULT_INTEGER_VALUE = 1;
	
	private static Properties props;
	
	private static void init() {
		if (props == null) {
			props = defaults();
			try {
				PROP_FILE = new File(PROP_FILE_PATH).getCanonicalFile();
				props.loadFromXML(new FileInputStream(PROP_FILE));
			} catch (java.io.FileNotFoundException fnf) {
				store();
			} catch (Exception e) {
				throw new RuntimeException("Configuration file \"" + PROP_FILE + "\" can't be read!\n" + e);
			}			
		}
	}
	
	/**Writes the default values for options to the config file.
	 */
	public static void revertDefaults() {
		props = defaults();
		store("Reverted to default properties on " + new java.util.Date(System.currentTimeMillis()) + ".");
	}
	
	/**Store options with a timetag as comment.
	 */
	public static void store() {
		store("Saved options on " + new java.util.Date(System.currentTimeMillis()) + ".");
	}
	
	/**Store options with the given comments.
	 *<strong>WARNING</strong> for numeric values, store them by 10 times the value you want them to be read. This is due to a limitation of the GUI input discovered too late to be corrected.
	 *@param	comments	comments to add to the file.
	 */
	public static void store(String comments) {
		try {
			props.storeToXML(new FileOutputStream(PROP_FILE), comments);
		} catch (Exception e) {
			throw new RuntimeException("Configuration file \"" + PROP_FILE + "\" can't be written to!\n" + e);
		}
	}
	
	
	/**@name	Values getters*/
	//@{
	/**Returns the value for the given key, or null if none is found.
	 */
	public static String get(String key) {
		init();
		return props.getProperty(key);
	}
	
	/**Returns the value for the given key as a Integer, or a default value if none is found.
	 *@see	DEFAULT_INTEGER_VALUE
	 */	
	public static Integer getInteger(String key) {
		init();
		try {
			return new Integer(props.getProperty(key));
		} catch (NumberFormatException e) {
			System.err.println("Couldn't parse option \"" + key + "\" as integer ! Returned default value " + DEFAULT_INTEGER_VALUE + " instead.");
			return DEFAULT_INTEGER_VALUE;
		} catch (NullPointerException n) {
			System.err.println("Couldn't get key \"" + key + "\" ! Returned default value " + DEFAULT_INTEGER_VALUE + " instead.");
			return DEFAULT_INTEGER_VALUE;
		}
	}
	
	/**Returns the value for the given key as a Double, or a default value if none is found.
	 *@see	DEFAULT_DOUBLE_VALUE
	 */	
	public static Double getDouble(String key) {
		init();
		try {
			return new Double(props.getProperty(key));
		} catch (NumberFormatException e) {
			System.err.println("Couldn't parse option \"" + key + "\" as double ! Returned default value " + DEFAULT_DOUBLE_VALUE + " instead.");
			return DEFAULT_DOUBLE_VALUE;
		} catch (NullPointerException n) {
			System.err.println("Couldn't get key \"" + key + "\" ! Returned default value " + DEFAULT_DOUBLE_VALUE + " instead.");
			return DEFAULT_DOUBLE_VALUE;
		}
	}
	
	/**Returns the value for the given key as a Boolean, or false if none is found.
	 *A key is evaluated to true if its value is "true", as described in Boolean.valueOf(String).
	 *@see	Boolean.valueOf
	 */	
	public static Boolean getBoolean(String key) {
		init();
		Boolean value = new Boolean(props.getProperty(key));
		return (value == null ? Boolean.FALSE : value);
	}
	//@}
	
	/**@name	Values setters*/
	//@{
	public static void set(String key, String value) {
		init();
		props.put(key, value);
		store();
	}
	//@}
}
