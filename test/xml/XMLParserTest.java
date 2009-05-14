/**A set of tests for the XMLParser class.
*@author <a href="mailto:alcmene.gs@gmail.com">Matti Schneider-Ghibaudo</a>
*@version 0.1
*/

package test.xml;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

import org.w3c.dom.Node;

import xml.XMLParser;
import test.element.XMLLoadableElementTest;
import test.element.plant.PlantTest;

public class XMLParserTest {
	
	private static String testFile;
	
	private static XMLLoadableElementTest expectedValuesHolder;
	private XMLParser subject;
	
	private final static String EXPR_VERSION = "//id/../@version";
	private final static String EXPR_ID = "//id";
	private final static String EXPR_NAME = "//name[@lang='fr']";
	private final static String EXPR_LISTSMAP_TEST = "//assets[@type='standard']";
	private final static String EXPR_LIST_TEST = EXPR_LISTSMAP_TEST + "/shaft";
	private final static String EXPR_DESCRIPTION = "//description";
	
	@BeforeClass
	public static void classSetUp() {
		expectedValuesHolder = new PlantTest();
		expectedValuesHolder.setUp();
		File tmp = new File("../ressources/elements/" + expectedValuesHolder.EXPECTED_ID + "/description.xml"); //WTF ?!? Well, Java seems to be unable to resolve a relative path without this... Don't ask me !
		testFile = tmp.getAbsolutePath();
		try {
			File f = new File(testFile);
			if (! f.exists())
				throw new java.io.IOException();
		} catch(java.io.IOException e) {
			fail("The testing file \"" + testFile + "\"doesn't exist !\nPlease check the PlantTest file.");
		}
	}
	
	@Before
	public void setUp() {
		subject = new XMLParser(testFile);
	}
	
	@Test
	public void XMLParserInitTest() {
		assertNotNull("String constructor created a null object !", subject);
		subject = new XMLParser(new File(testFile));
		assertNotNull("File constructor created a null object !", subject);
	}
	
	@Test
	public void stringParsingTest() {
		assertEquals(expectedValuesHolder.EXPECTED_ID, subject.get(EXPR_ID));
		assertEquals(expectedValuesHolder.EXPECTED_NAME, subject.get(EXPR_NAME));
		assertEquals(expectedValuesHolder.EXPECTED_DESCRIPTION, subject.get(EXPR_DESCRIPTION));
	}
	
	@Test
	public void doubleParsingTest() {
		assertNotNull(subject.getDouble(EXPR_VERSION));
	}
	
	public void checkList(List<String> list) {
		assertFalse(list.isEmpty());
		for (String s : list) {
			assertNotNull(s);
			assertFalse("Parsed string is empty !", s.length() == 0);
		}
	}
	
	@Test
	public void listParsingTest() {
		checkList(subject.getValues(EXPR_LIST_TEST));
	}
	
	@Test
	public void listsMapParsingTest() {
		Map<String, List<String>> map = subject.getListsMap(EXPR_LISTSMAP_TEST);
		assertFalse("getListsMap returned an empty map for the Xpath expression \"" + EXPR_LISTSMAP_TEST + "\" !", map.isEmpty());
		for (List<String> list : map.values())
			checkList(list);
	}
	
	@Test
	public void nodesListsMapParsingTest() {
		Map<String, List<Node>> nodesMap = subject.getNodesListsMap(EXPR_LISTSMAP_TEST);
		Map<String, List<String>> stringsMap = new HashMap<String, List<String>>();

		for (Map.Entry<String, List<Node>> entry : nodesMap.entrySet()) {
			List<Node> nodes = entry.getValue();
			List<String> strings = new ArrayList(nodes.size());
			for (Node node : nodes)
				strings.add(node.getTextContent());
			stringsMap.put(entry.getKey(), strings);
		}
		
		Map<String, List<String>> expectedStringsMap = subject.getListsMap(EXPR_LISTSMAP_TEST);
		assertEquals(expectedStringsMap, stringsMap);
	}
}