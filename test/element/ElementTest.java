/**A set of tests for the Element class.
*@author <a href="mailto:alcmene.gs@gmail.com">Matti Schneider-Ghibaudo</a>
*@version 0.1
*/

package test.element;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import element.Element;
import element.plant.Plant;


public class ElementTest {
	
	public static final String TEST_FILE = "ressources/Plantes/rosa/description.xml";

	private Element subject;
	private static final String EXPECTED_ID = "rosa";
	private static final String EXPECTED_NAME = "rose";
	
	
	@Before
	public void setUp() {
		subject = new Plant(TEST_FILE);
	}
	
	@Test
	public void parsingTest() {
		assertEquals("ID badly parsed", EXPECTED_ID, subject.ID());
		assertEquals("Name badly parsed", EXPECTED_NAME, subject.name());
	}
}