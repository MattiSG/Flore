package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.xml.XMLParserTest;
import test.element.plant.PlantTest;
import test.element.creature.CreatureTest;
import test.element.mission.MissionTest;
import test.element.DefaultElementsTest;
import test.element.AllAssetsTypesTest;

@RunWith(Suite.class)
@SuiteClasses({
	DefaultElementsTest.class,
	AllAssetsTypesTest.class
})

public class ExtendedTests {}
