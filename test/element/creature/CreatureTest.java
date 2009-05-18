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

import java.io.File;

import java.util.List;
import java.util.Map;

import java.awt.Dimension;

import test.element.XMLLoadableElementTest;
import element.creature.Creature;


public class CreatureTest extends XMLLoadableElementTest {
	
	private static final String[] EXPECTED_BRINGS = {"ID1", "ID2", "ID3"};
	private static final Double[] EXPECTED_BRINGS_PROBABILITIES = {0.8, 0.3, 0.5};
	private static final Dimension EXPECTED_DIMENSIONS = new Dimension(100, 200);
	private static final int EXPECTED_LIFETIME = 10;
	
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
	public void bringsParsingTest() {
		Map<String, Double> brings = ((Creature) subject).brings();
		assertNotNull("Parsed brings map was null !", brings);
		assertEquals("Parsed brings map doesn't have the expected size !", EXPECTED_BRINGS.length, brings.size());
		for (int i = 0; i < EXPECTED_BRINGS.length; i++) {
			assertTrue("Parsed brings map doesn't contain the expected \"" + EXPECTED_BRINGS[i] + "\" element !", brings.containsKey(EXPECTED_BRINGS[i]));
			assertEquals("Parsed brings map doesn't have the expected value for key \"" + EXPECTED_BRINGS[i] + "\" !", EXPECTED_BRINGS_PROBABILITIES[i], brings.get(EXPECTED_BRINGS[i]));
		}
	}
	
	@Test
	public void dimensionsParsingTest() {
		java.awt.image.BufferedImage i = ((Creature) subject).getAssets(subject.getAssetsNames()[0]).get(0);
		Dimension dimensions = new Dimension((int) (i.getWidth() / subject.zoom()), (int) (i.getHeight() / subject.zoom()));
		assertNotNull("Parsed dimensions are null !", dimensions);
		assertEquals(EXPECTED_DIMENSIONS, dimensions);
	}
	
	@Test
	public void lifetimeParsingTest() {
		assertEquals(EXPECTED_LIFETIME, ((Creature) subject).lifetime());
	}
	
	@Test
	public void soundParsingTest() {
		File sound = ((Creature) subject).sound();
		assertNotNull(sound);
		assertTrue("Given sound file doesn't exist !", sound.exists());
		assertTrue("Given sound file is not readable !", sound.canRead());
	}
}