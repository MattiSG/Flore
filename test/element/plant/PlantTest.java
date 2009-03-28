/**A set of tests for the Plant class.
 *@author <a href="mailto:alcmene.gs@gmail.com">Matti Schneider-Ghibaudo</a>
 *@version 0.1
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
import element.XMLLoadableElement;
import element.plant.Plant;


public class PlantTest extends XMLLoadableElementTest {
	
	@Before
	public void setUp() {
		EXPECTED_ID = "specs_seed";
		EXPECTED_NAME = "Testing plant file";
		EXPECTED_DESCRIPTION = "Lorem ipsum dolor sit amet.";
		
		try {
			subject = new Plant(EXPECTED_ID);
		} catch (NullPointerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
