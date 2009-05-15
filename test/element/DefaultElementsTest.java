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

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import element.XMLLoadableElement;
import element.plant.Plant;
import element.creature.Creature;
import element.mission.Mission;

public class DefaultElementsTest {
	
	public final static String[] MISSIONS_IDS = {"mission_1", "mission_2"};
	public final static String[] PLANTS_IDS = {"rosa", "mimosa", "buddleia", "serrulata"};
	public final static String[] CREATURES_IDS = {"aphidoidea", "coccinelle", "sphingidae", "papilionidae", "lycaenidae"};	

	private XMLLoadableElement subject;
	
	@Test
	public void testElements() {
		String currentId = "";
		try {
			for (int i = 0; i < CREATURES_IDS.length; i++) {
				currentId = CREATURES_IDS[i];
				subject = new Creature(currentId);
				runTests();
			}
			for (int i = 0; i < PLANTS_IDS.length; i++) {
				currentId = PLANTS_IDS[i];
				subject = new Plant(currentId);
				runTests();
			}
			for (int i = 0; i < MISSIONS_IDS.length; i++) {
				currentId = MISSIONS_IDS[i];
				subject = new Mission(currentId);
				runTests();
			}
		} catch (Exception e) { 
			e.printStackTrace(); 
			fail("Error with element \"" + currentId + "\" : " + e.toString()); 
		}
	}
	
	public void runTests() {
		assertTrue("Element ID \"" + subject.ID() + "\" has an incompatible version number.", subject.checkVersion());
		XMLLoadableElementTest.runTests(subject);
		checkDependencies();
	}
	
	public void checkDependencies() {
		if (subject.getClass() == Mission.class) {
			checkPlantsExistence(new ArrayList<String>(((Mission) subject).plants().keySet()));
			checkCreaturesExistence(new ArrayList<String>(((Mission) subject).goal().keySet()));
		} else if (subject.getClass() == Creature.class) {
			checkCreaturesExistence(new ArrayList<String>(((Creature) subject).brings().keySet()));
		} else if (subject.getClass() == Plant.class) {
			checkCreaturesExistence(new ArrayList<String>(((Plant) subject).brings().keySet()));
		}
	}
	
	public void checkExistence(List<String> ids) {
		for (String id : ids)
			if (! (checkCreatureExistence(id) || checkPlantExistence(id)))
				fail("No loadable element was found for ID \"" + id + "\", used in element \"" + subject.ID() + "\".");
	}

	public void checkCreaturesExistence(List<String> ids) {
		for (String id : ids)
			if (! checkCreatureExistence(id))
				fail("No loadable creature was found for ID \"" + id + "\", used in element \"" + subject.ID() + "\".");
	}

	public void checkPlantsExistence(List<String> ids) {
		for (String id : ids)
			if (! checkPlantExistence(id))
				fail("No loadable plant was found for ID \"" + id + "\", used in element \"" + subject.ID() + "\"."); 
	}
	
	public boolean checkCreatureExistence(String id) {
		try {
			new Creature(id);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean checkPlantExistence(String id) {
		try {
			new Plant(id);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
}
