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
	
	public static final String TEST_FILE = "test";
	
	
	@Before
	public void setUp() {
		try {
			subject = new Plant(TEST_FILE);
		} catch (NullPointerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void assetsLoading() {
	}
}