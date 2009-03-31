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

import test.element.XMLLoadableElementTest;
import element.mission.Mission;


public class MissionTest extends XMLLoadableElementTest {
	
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
	}
}
