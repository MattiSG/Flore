package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.element.plant.PlantTest;
import test.element.creature.CreatureTest;

@RunWith(Suite.class)
@SuiteClasses({
//	PlantTest.class
//	CreatureTest.class //add when creature_test will have its assets
})

public class RunAndPray {}
