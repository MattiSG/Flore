/**A set of tests for the Element class.
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


public class XMLLoadableElementTest {
	
	public final static String TEST_FILE = "test";
	public final static String EXPECTED_ID = "test";
	public final static String EXPECTED_NAME = "Testing plant file";
	public final static String EXPECTED_DESCRIPTION = "Lorem ipsum dolor sit amet.";
	
	protected XMLLoadableElement subject;
	
	@Test
	public void parsingTest() {
		assertEquals("ID badly parsed : ", EXPECTED_ID, subject.ID());
		assertEquals("Name badly parsed : ", EXPECTED_NAME, subject.name());
		assertEquals("Description badly parsed : ", EXPECTED_DESCRIPTION, subject.description());
	}
}