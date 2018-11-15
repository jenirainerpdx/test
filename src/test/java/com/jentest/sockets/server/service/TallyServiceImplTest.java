package com.jentest.sockets.server.service;

import com.jentest.sockets.model.MessagesReceivedCounter;
import com.jentest.sockets.model.Request;
import com.jentest.sockets.model.RequestImpl;
import com.jentest.sockets.model.ValueMap;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class TallyServiceImplTest {

	public static final Integer ONE = new Integer(1);
	private TallyServiceImpl classUnderTest;
	private static final Integer ZERO = new Integer(0);
	private static final Integer FIVE = new Integer(5);
	private static final Logger LOGGER = LoggerFactory.getLogger(TallyServiceImplTest.class);

	@Before
	public void setup(){
		classUnderTest = new TallyServiceImpl(new ValueMap(),
				new MessagesReceivedCounter(),
				new MessagesReceivedCounter(),
				new MessagesReceivedCounter());

	}

//	@Test
	public void simpleTestEmptyValues() {
		classUnderTest.snapshot();
		assertEquals(ZERO, classUnderTest.getNewDupes());
		assertEquals(ZERO, classUnderTest.getRunningTotalUnique());
		assertEquals(ZERO, classUnderTest.getNewUniques());
	}

	@Test
	public void simpleTestHappyPath() throws InterruptedException {
		setupValues();
		classUnderTest.snapshot();
		takeAPicture(1);
		assertEquals(new Integer(3), classUnderTest.getNewUniques());
		assertEquals(new Integer(3), classUnderTest.getRunningTotalUnique());
		assertEquals(new Integer(9), classUnderTest.getNewDupes());
		// now add more values and test again.
		buildAndSubmit("123456789"); // already in
		buildAndSubmit("123"); // new
		buildAndSubmit("999999999"); // new
		classUnderTest.snapshot();
		takeAPicture(2);
		assertEquals("999999999 and 123 are new.", new Integer(2), classUnderTest.getNewUniques());
		assertEquals("The three values setup in setupValues and the two just added.", FIVE, classUnderTest.getRunningTotalUnique());
		assertEquals("123456789 is a dupe.", ONE, classUnderTest.getNewDupes());
		classUnderTest.snapshot();
		takeAPicture(3);
		assertEquals("We added nothing new.  Just did a snapshot.", ZERO, classUnderTest.getNewDupes());
		assertEquals("We added nothing new.  Just did a snapshot.", ZERO, classUnderTest.getNewUniques());
		assertEquals("We added nothing new.  Just did a snapshot.", FIVE, classUnderTest.getRunningTotalUnique());
		classUnderTest.snapshot();
		takeAPicture(4);
		assertEquals("Once again, added nothing.", ZERO, classUnderTest.getNewUniques());
		assertEquals("Once again, added nothing.", ZERO, classUnderTest.getNewDupes());
		assertEquals("Once again, added nothing.", FIVE, classUnderTest.getRunningTotalUnique());
		buildAndSubmit("8888888");
		buildAndSubmit("123");
		classUnderTest.snapshot();
		takeAPicture(5);
		assertEquals("123 is a dupe.", ONE, classUnderTest.getNewDupes());
		assertEquals("8888 series is new.", ONE, classUnderTest.getNewUniques());
		assertEquals("Added one new to our set of 5.", new Integer(6), classUnderTest.getRunningTotalUnique());
	}


	private void takeAPicture(int iteration) {
		LOGGER.debug("************* SNAPSHOT: " + iteration + " **********************");
		LOGGER.debug("New uniques: " + classUnderTest.getNewUniques());
		LOGGER.debug("New dupes:  " + classUnderTest.getNewDupes());
		LOGGER.debug("Running Total all unique:  " + classUnderTest.getRunningTotalUnique());
		classUnderTest.showMe();
	}

	private void buildAndSubmit(String s) {
		Request request = new RequestImpl(s, Thread.currentThread().getName());
		classUnderTest.putNumberOnQueue(request);
	}


	private void setupValues() {
		for (int i = 0; i < 10; i++) {
			Request request = new RequestImpl("123456789",
					Thread.currentThread().getName());
			classUnderTest.putNumberOnQueue(request);
		}
		Request request = new RequestImpl("000123456",
							Thread.currentThread().getName());
		classUnderTest.putNumberOnQueue(request);
		Request request1 = new RequestImpl("888888999",
							Thread.currentThread().getName());
		classUnderTest.putNumberOnQueue(request1);
	}

}