package com.newrelic.codingchallenge.server;

public interface MessageValidator {

	Boolean messageAllNumeric(String message);
	Boolean messageTerminate(String message);

}
