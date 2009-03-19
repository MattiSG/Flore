package element;

import java.util.Map;
import java.util.List;

import javax.xml.xpath.*;

import java.awt.image.BufferedImage;

abstract public class Element {
	/**Version check. If the version of the parser is smaller than the file asks for, the file won't be parsed.*/
	public final static int PARSER_VERSION = 1;
	
	protected String ID;
    protected String name;
	protected String description;
	protected Map<String, List<BufferedImage>> assets;

	private final static String EXPR_ID = "ID";
	private final static String EXPR_NAME = "name[lang=fr]";
	private final static String EXPR_ASSETS = "assets";
	private final static String EXPR_DESCRIPTION = "description";
	
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
	//@}
	
	
	/**@name	XML parsing*/
	//@{
    public void loadFromXML(String XML) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			ID = xpath.evaluate(EXPR_ID, XML);
			name = xpath.evaluate(EXPR_NAME, XML);
			description = xpath.evaluate(EXPR_DESCRIPTION, XML);
		} catch (XPathExpressionException e) {
			System.err.println("Erreur à l'analyse d'un fichier d'élément (insecte ou plante) !\nLa version est-elle correcte ?\n" + e);
		}
	}
	//@}
}
