package com.newrelic.codingchallenge.server.service;

import com.newrelic.codingchallenge.model.Request;

public interface TallyService{
	Integer getNewUniques();

	Integer getNewDupes();

	Integer getRunningTotalUnique();

	void snapshot();

	void stopService();

	void resetCounters();

	void putNumberOnQueue(Request request);

}
