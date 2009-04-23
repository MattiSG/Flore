/**A set of tests for the default Elements distributed with the basic Flore edition.
 *Tries to instantiate each one of the standard elements to distribute, in order to check their semantical correctness.
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

import element.XMLLoadableElement;
import element.plant.Plant;
import element.creature.Creature;
import element.mission.Mission;

public class DefaultElementsTest {
	
	public final static String[] MISSIONS_IDS = {"mission_1"};
	public final static String[] PLANTS_IDS = {"rosa", "mimosa"};
	public final static String[] CREATURES_IDS = {"aphidoidea", "coccinelle"};	
	
	@Test
	public void elementTest() {
		try {
			for (int i = 0; i < MISSIONS_IDS.length; i++)
				runTests(new Mission(MISSIONS_IDS[i]));
			for (int i = 0; i < PLANTS_IDS.length; i++)
				runTests(new Plant(PLANTS_IDS[i]));
			for (int i = 0; i < CREATURES_IDS.length; i++)
				runTests(new Creature(CREATURES_IDS[i]));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	public static void runTests(XMLLoadableElement subject) {
		assertTrue("Element ID \"" + subject.ID() + "\" has an incompatible version number.", subject.checkVersion());
		XMLLoadableElementTest.runTests(subject);
	}
}