package com.newrelic.codingchallenge.server.service;

public interface TallyService {
	Integer getNewUniques();

	Integer getNewDupes();

	Integer getRunningTotalUnique();

	void snapshot();

	void resetCounters();

	void pollMessagesQueue();

	void putNumberOnQueue(String message);

	String readFromQueue();

	int getQueueSize();
}
