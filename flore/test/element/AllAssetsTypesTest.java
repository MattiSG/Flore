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

import test.RunAndPray;
import xml.GlobalProperties;

public class AllAssetsTypesTest {
	private static String previousType;
	
	@BeforeClass
	public static void classSetUp() {
		previousType = GlobalProperties.get("assets_type");
	}
	
	@After
	public void tearDown() {
		GlobalProperties.set("assets_type", previousType);
		GlobalProperties.store();
	}
	
	
	@Test
	public void allAssetsTypesTest() {
		for (String type : GlobalProperties.AVAILABLE_ASSETS_TYPES) {
			GlobalProperties.set("assets_type", type);
			System.out.print("Asset type \"" + type + "\"");
			 
			System.out.println(" was correctly handled.");
		}
	}
}
