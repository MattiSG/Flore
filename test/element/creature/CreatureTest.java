/**A set of tests for the Creature class.
 *@author <a href="mailto:alcmene.gs@gmail.com">Matti Schneider-Ghibaudo</a>
 *@version 0.1
 */

package test.element.creature;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import test.element.XMLLoadableElementTest;
import element.creature.Creature;


public class CreatureTest extends XMLLoadableElementTest {
	
	private static String[] EXPECTED_EATS = {"ID1", "ID2", "ID3"};
	
	@Before
	public void setUp() {
		EXPECTED_ID = "specs_creature";
		EXPECTED_NAME = "Specifications Creature";
		EXPECTED_DESCRIPTION = "Ce fichier sert de specifications pour les fichiers decrivant des creatures.";
		
		try {
			subject = new Creature(EXPECTED_ID);
		} catch (NullPointerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void eatsParsingTest() {
		List<String> eats = ((Creature) subject).eats();
		assertEquals("Parsed eats list doesn't have the expected size !", EXPECTED_EATS.length, eats.size());
		for (int i = 0; i < EXPECTED_EATS.length; i++)
			assertTrue("Parsed eats list doesn't contain the expected \"" + EXPECTED_EATS[i] + "\" element !", eats.contains(EXPECTED_EATS[i]));
	}
}