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
	
	protected String EXPECTED_ID;
	protected String EXPECTED_NAME;
	protected String EXPECTED_DESCRIPTION;
	
	protected XMLLoadableElement subject;
	
	@Test
	public void initTest() {
//		System.out.println("ID : " + EXPECTED_ID + "\nName : " + EXPECTED_NAME + "\nsubject : " + subject);
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
		for (String key : subject.getAssetsNames())
			assertFalse("An empty assets list was detected.", subject.getAssets(key).isEmpty());
	}
}