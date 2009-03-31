package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.element.plant.PlantTest;
import test.element.creature.CreatureTest;
import test.element.mission.MissionTest;
import test.xml.XMLParserTest;

@RunWith(Suite.class)
@SuiteClasses({
	PlantTest.class,
	CreatureTest.class,
	MissionTest.class,
	XMLParserTest.class
})

public class RunAndPray {}
