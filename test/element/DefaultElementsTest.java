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
		String[][] ids = {MISSIONS_IDS, PLANTS_IDS, CREATURES_IDS};
		for (int i = 0; i < ids.length; i++) {
			for (int j = 0; j < ids[i].length; j++) {
				String id = ids[i][j];
				try {
					switch (i) {
						case 0 :
							runTests(new Mission(ids[i][j]));
							break;
						case 1 :
							runTests(new Plant(ids[i][j]));
							break;
						case 2 :
							runTests(new Creature(ids[i][j]));
							break;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					fail(e.toString());
				}
			}
		}
	}
	
	public static void runTests(XMLLoadableElement subject) {
		assertTrue("Element ID \"" + subject.ID() + "\" has an incompatible version number.", subject.checkVersion());
		XMLLoadableElementTest.runTests(subject);
	}
}