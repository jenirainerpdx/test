package com.newrelic.codingchallenge.model;

public interface Request {

	Integer getRequestId();
	void setRequestId(Integer id);
	String getStringMessage();
	Integer getIntegerMessage();
	Boolean isValid();
	Boolean isTerminate();
	String getThreadOrigin();
}
