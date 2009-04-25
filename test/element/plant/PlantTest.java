/**A set of tests for the Plant class.
 *@author <a href="mailto:alcmene.gs@gmail.com">Matti Schneider-Ghibaudo</a>
 *@version 0.2
 */

package test.element.plant;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import test.element.XMLLoadableElementTest;
import element.plant.Plant;


public class PlantTest extends XMLLoadableElementTest {
	protected final int	EXPECTED_SUN = 15,
						EXPECTED_TIME = 10,
						EXPECTED_WATER = 5;
	
	@Before
	public void setUp() {
		EXPECTED_ID = "specs_plant";
		EXPECTED_NAME = "Specifications Plante";
		EXPECTED_DESCRIPTION = "Ce fichier sert de specifications pour les fichiers decrivant des plantes.";
		
		try {
			subject = new Plant(EXPECTED_ID);
		} catch (NullPointerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void sunParsingTest() {
		assertEquals("Sun incorrectly parsed !", EXPECTED_SUN, ((Plant) subject).neededSun());
	}
	
	@Test
	public void timeParsingTest() {
		assertEquals("Time incorrectly parsed !", EXPECTED_TIME, ((Plant) subject).neededTime());
	}
	
	@Test
	public void waterParsingTest() {
		assertEquals("Water incorrectly parsed !", EXPECTED_WATER, ((Plant) subject).neededWater());
	}
}
