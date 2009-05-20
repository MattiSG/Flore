/**A set of tests for the GlobalProperties class.
*@author <a href="mailto:alcmene.gs@gmail.com">Matti Schneider-Ghibaudo</a>
*@version 0.1
*/

package test.xml;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import xml.GlobalProperties;

public class GlobalPropertiesTest {
	private static final String	TEST_KEY = GlobalProperties.TEST_KEY,
								EXPECTED_VALUE = GlobalProperties.TEST_VALUE;
	
	@Test
	public void defaultValueTest() {
		assertEquals(EXPECTED_VALUE, GlobalProperties.get(TEST_KEY));
	}
	
	@Test
	public void setValueTest() {
		String written = EXPECTED_VALUE + "a";
		GlobalProperties.set(TEST_KEY, written);
		assertEquals(written, GlobalProperties.get(TEST_KEY));
		GlobalProperties.set(TEST_KEY, EXPECTED_VALUE);
		defaultValueTest();
	}
	
	@Test
	public void storeTest() {
		try {
			GlobalProperties.store("GlobalProperties class was successfully tested on " +  new java.util.Date(System.currentTimeMillis()) + " ! :)");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}