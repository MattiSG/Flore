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

public abstract class XMLLoadableElementTest {
	
	public String EXPECTED_ID;
	public String EXPECTED_NAME;
	public String EXPECTED_DESCRIPTION;
	
	protected XMLLoadableElement subject;
	
	public abstract void setUp();
	
	@Test
	public void initTest() {
		initTest(subject);
	}
	
	public static void initTest(XMLLoadableElement subject) {
		assertNotNull("Constructor created a null object !", subject);
	}
	
	@Test
	public void versionCheckTest() {
		assertTrue("Specification file version doesn't match parser version.", subject.checkVersion());
		if (XMLLoadableElement.STRICT_VERSION_CHECKING) {
			double[] illegalVersions = {-1, 0, subject.parserVersion() - 0.1};
			for (int i = 0; i < illegalVersions.length; i++)
				assertFalse("An illegal version number (" + illegalVersions[i] +") was accepted.", subject.checkVersion(illegalVersions[i]));
		} else {
			System.out.println("[NOTE] Strict version checking is deactivated. Some tests have been disabled. (see XMLLoadableElement#STRICT_VERSION_CHECKING for more details)");
		}
	}
	
	@Test
	public void parsingTest() {
		assertEquals("ID badly parsed : ", EXPECTED_ID, subject.ID());
		assertEquals("Name badly parsed : ", EXPECTED_NAME, subject.name());
		assertEquals("Description badly parsed : ", EXPECTED_DESCRIPTION, subject.description());
	}
	
	@Test
	public void assetsLoading() {
		assetsLoading(subject);
	}
	
	public static void assetsLoading(XMLLoadableElement subject) {
		for (String key : subject.getAssetsNames()) {
			assertNotNull("The assets list \"" + key + "\" was null.", subject.getAssets(key));
			assertFalse("The assets list \"" + key + "\" was empty.", subject.getAssets(key).isEmpty());
			assertFalse("The assets list \"" + key + "\" contains a null value.", subject.getAssets(key).contains(null));
			assertEquals("The asset from getAsset() isn't the same as the one from getAssets().get(0).", subject.getAssets(key).get(0), subject.getAsset(key));
		}
	}
	
	@Test
	public void assetsKeysCheck() {
		assetsKeysCheck(subject);
	}
	
	public static void assetsKeysCheck(XMLLoadableElement subject) {
		String[] assetsKeys = subject.getAssetsNames();
		for (int i = 0; i < assetsKeys.length; i++)
			assertTrue("A valid key (" + assetsKeys[i] + ") wasn't recognized as such !", subject.isAssetKey(assetsKeys[i]));
		assertFalse("An empty key was recognized as valid !", subject.isAssetKey(""));
	}
	
	public static void runTests(XMLLoadableElement subject) {
		initTest(subject);
		assetsLoading(subject);
		assetsKeysCheck(subject);
	}
}