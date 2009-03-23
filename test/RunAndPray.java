package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.element.ElementTest;
import test.element.plant.PlantTest;

@RunWith(Suite.class)
@SuiteClasses({
	PlantTest.class
})

public class RunAndPray {}
