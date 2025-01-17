package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.xml.XMLParserTest;
import test.xml.GlobalPropertiesTest;
import test.element.plant.PlantTest;
import test.element.creature.CreatureTest;
import test.element.mission.MissionTest;
import test.element.DefaultElementsTest;

@RunWith(Suite.class)
@SuiteClasses({
	XMLParserTest.class,
	PlantTest.class,
	CreatureTest.class,
	MissionTest.class,
	GlobalPropertiesTest.class
})

public class RunAndPray {}
