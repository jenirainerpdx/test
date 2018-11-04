package com.newrelic.codingchallenge.model;

import java.util.concurrent.atomic.AtomicInteger;

public class MessagesReceivedCounter {

	public final AtomicInteger totalMessagesReceived = new AtomicInteger();

	public int gotAMessage(){
		return totalMessagesReceived.incrementAndGet();
	}

	public int gotNMessages(int n) { return totalMessagesReceived.addAndGet(n); }

	public int getTotalReceivedCount(){
		return totalMessagesReceived.get();
	}
}
