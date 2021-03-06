package com.jentest.sockets.server.service;

import com.jentest.sockets.model.Request;
import com.jentest.sockets.model.RequestImpl;
import com.jentest.sockets.model.ValueMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValueMapTest {

	private ValueMap classUnderTest;

	@Before
	public void setup(){
		classUnderTest = new ValueMap();
	}

	@Test
	public void test(){
		for (int i = 0; i < 10; i++) {
			Request r = new RequestImpl("" + i, Thread.currentThread().getName());
			classUnderTest.acceptEvent(r);
		}
		assertEquals(10, classUnderTest.size());
		classUnderTest.getEventsFromLastTenSeconds();
		assertEquals(10, classUnderTest.size());
	}
}
