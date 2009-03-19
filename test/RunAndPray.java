package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.element.ElementTest;

@RunWith(Suite.class)
@SuiteClasses({
	ElementTest.class
})

public class RunAndPray {}
