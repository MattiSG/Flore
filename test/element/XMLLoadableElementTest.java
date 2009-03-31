/**A set of tests for the Element class.
*@author <a href="mailto:alcmene.gs@gmail.com">Matti Schneider-Ghibaudo</a>
*@version 0.2
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


public abstract class XMLLoadableElementTest {
	
	public String EXPECTED_ID;
	public String EXPECTED_NAME;
	public String EXPECTED_DESCRIPTION;
	
	protected XMLLoadableElement subject;
	
	public abstract void setUp();
	
	@Test
	public void initTest() {
		assertNotNull("Constructor created a null object !", subject);
	}
	
	@Test
	public void parsingTest() {
		assertEquals("ID badly parsed : ", EXPECTED_ID, subject.ID());
		assertEquals("Name badly parsed : ", EXPECTED_NAME, subject.name());
		assertEquals("Description badly parsed : ", EXPECTED_DESCRIPTION, subject.description());
	}
	
	@Test
	public void assetsLoading() {
		for (String key : subject.getAssetsNames()) {
			assertNotNull("The assets list \"" + key + "\" was null.", subject.getAssets(key));
			assertFalse("The assets list \"" + key + "\" was empty.", subject.getAssets(key).isEmpty());
		}
	}
}