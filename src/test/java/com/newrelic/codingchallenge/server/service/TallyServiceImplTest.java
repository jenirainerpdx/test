package com.newrelic.codingchallenge.server.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TallyServiceImplTest {

	public static final Integer ONE = new Integer(1);
	private TallyServiceImpl classUnderTest;
	private static final Integer ZERO = new Integer(0);
	private static final Integer FIVE = new Integer(5);

	@Before
	public void setup(){
		classUnderTest = new TallyServiceImpl();
	}


	@Test
	public void simpleTestEmptyValues(){

		TallyServiceImpl.values.clear();
		classUnderTest.resetCounters();
		classUnderTest.snapshot();
		assertEquals(ZERO, classUnderTest.getNewDupes());
		assertEquals(ZERO, classUnderTest.getRunningTotalUnique());
		assertEquals(ZERO, classUnderTest.getNewUniques());
	}

	@Test
	public void simpleTestHappyPath(){
		TallyServiceImpl.values.clear();
		classUnderTest.resetCounters();
		setupValues();
		classUnderTest.snapshot();
		assertEquals(new Integer(3), classUnderTest.getNewUniques());
		assertEquals(new Integer(3), classUnderTest.getRunningTotalUnique());
		assertEquals(new Integer(9), classUnderTest.getNewDupes());
		// now add more values and test again.
		TallyServiceImpl.values.add("123456789"); // already in
		TallyServiceImpl.values.add("123"); // new
		TallyServiceImpl.values.add("999999999"); // new
		classUnderTest.snapshot();
		assertEquals("999999999 and 123 are new.", new Integer(2), classUnderTest.getNewUniques());
		assertEquals("The three values setup in setupValues and the two just added.", FIVE, classUnderTest.getRunningTotalUnique());
		assertEquals("123456789 is a dupe.", ONE, classUnderTest.getNewDupes());
		classUnderTest.snapshot();
		assertEquals("We added nothing new.  Just did a snapshot.", ZERO, classUnderTest.getNewDupes());
		assertEquals("We added nothing new.  Just did a snapshot.", ZERO, classUnderTest.getNewUniques());
		assertEquals("We added nothing new.  Just did a snapshot.", FIVE, classUnderTest.getRunningTotalUnique());
		classUnderTest.snapshot();
		assertEquals("Once again, added nothing.", ZERO, classUnderTest.getNewUniques());
		assertEquals("Once again, added nothing.", ZERO, classUnderTest.getNewDupes());
		assertEquals("Once again, added nothing.", FIVE, classUnderTest.getRunningTotalUnique());
		TallyServiceImpl.values.add("8888888");
		TallyServiceImpl.values.add("123");
		classUnderTest.snapshot();
		assertEquals("123 is a dupe.", ONE, classUnderTest.getNewDupes());
		assertEquals("8888 series is new.", ONE, classUnderTest.getNewUniques());
		assertEquals("Added one new to our set of 5.", new Integer(6), classUnderTest.getRunningTotalUnique());

	}


	private void setupValues(){
		for (int i = 0; i < 10; i++) {
			TallyServiceImpl.values.add("123456789");
		}
		TallyServiceImpl.values.add("000123456");
		TallyServiceImpl.values.add("888888999");
	}

}