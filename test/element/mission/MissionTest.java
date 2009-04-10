/**A set of tests for the Plant class.
 *@author <a href="mailto:alcmene.gs@gmail.com">Matti Schneider-Ghibaudo</a>
 *@version 0.1
 */

package test.element.mission;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import test.element.XMLLoadableElementTest;
import element.mission.Mission;


public class MissionTest extends XMLLoadableElementTest {
	
	protected String[] EXPECTED_ORDERED_HINTS = {"Level 0 hint", "Level 1 hint", "Level 10 hint"};
	protected final int	EXPECTED_HOLES = 5,
						EXPECTED_TIME_LIMIT = 120,
						EXPECTED_DIFFICULTY = 0;
	protected Map<String, Integer> plants, goal;

	@Before
	public void setUp() {
		EXPECTED_ID = "specs_mission";
		EXPECTED_NAME = "Specifications Mission";
		EXPECTED_DESCRIPTION = "Ce fichier sert de specifications pour les fichiers decrivant des missions.";
		
		try {
			subject = new Mission(EXPECTED_ID);
		} catch (NullPointerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		plants = new HashMap<String, Integer>();
		plants.put("rosa", 4);
		plants.put("specs_plant", 2);
		
		goal = new HashMap<String, Integer>();
		goal.put("coccinelle", 3);
		goal.put("specs_creature", 1);
	}
	
	@Test
	public void hintsParsingTest() {
		List<String> hints = ((Mission) subject).hints();
		assertNotNull("Hints map is null !", hints);
		assertFalse("Hints map is empty !", hints.isEmpty());
		assertEquals("Hints list size isn't the expected one !", EXPECTED_ORDERED_HINTS.length, hints.size());
		for (int i = 0; i < hints.size(); i++) 
			assertEquals("Hint number " + (i + 1) + " incorrectly parsed.", EXPECTED_ORDERED_HINTS[i], hints.get(i));
	}
	
	@Test
	public void plantsParsingTest() {
		assertEquals("Plants map incorrectly parsed !", plants, ((Mission) subject).plants());
	}
	
	@Test
	public void goalParsingTest() {
		assertEquals("Goal map incorrectly parsed !", goal, ((Mission) subject).goal());
	}
	
	@Test
	public void holesParsingTest() {
		assertEquals("Holes number incorrectly parsed !", EXPECTED_HOLES, ((Mission) subject).holes());
	}
	
	@Test
	public void difficultyParsingTest() {
		assertEquals("Difficulty number incorrectly parsed !", EXPECTED_DIFFICULTY, ((Mission) subject).difficulty());
	}
	
	@Test
	public void timeLimitParsingTest() {
		assertEquals("Time limit number incorrectly parsed !", EXPECTED_TIME_LIMIT, ((Mission) subject).timeLimit());
	}
}
