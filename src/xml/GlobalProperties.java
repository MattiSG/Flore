package xml;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**This class holds the global options used throughout the game.
 */
public class GlobalProperties {
	private static final String PROP_FILE = "../ressources/flore_config.xml";
	private static final double DEFAULT_DOUBLE_VALUE = 1.0;
	
	private static Properties props;
	
	private static void init() {
		if (props == null) {
			props = new Properties();
			try {
				props.loadFromXML(new FileInputStream(PROP_FILE));
			} catch (Exception e) {
				throw new RuntimeException("Configuration file \"" + PROP_FILE + "\" can't be read!\n" + e);
			}			
		}
	}
	
	/**Writes the default values for options to the config file.*/
	public void revertDefaults() {
		props = new Properties();
		props.setProperty("debug", "false");
		try {
			props.storeToXML(new FileOutputStream(PROP_FILE), "Reverted to default properties on " + new java.util.Date(System.currentTimeMillis()) + ".");
		} catch (Exception e) {
			throw new RuntimeException("Configuration file \"" + PROP_FILE + "\" can't be written to!\n" + e);
		}
	}
	
	public static String get(String key) {
		init();
		return props.getProperty(key);
	}
	
	public static Double getDouble(String key) {
		init();
		try {
			return new Double(props.getProperty(key));
		} catch (NumberFormatException e) {
			System.err.println("Couldn't parse option \"" + key + "\" as double ! Returned default value " + DEFAULT_DOUBLE_VALUE + " instead.");
			return DEFAULT_DOUBLE_VALUE;
		}
	}
}