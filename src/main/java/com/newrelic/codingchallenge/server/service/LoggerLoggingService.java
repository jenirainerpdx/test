package com.newrelic.codingchallenge.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerLoggingService implements LoggingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerLoggingService.class);

	public LoggerLoggingService(){
	}

	@Override
	public void logMessage(String messageIn) {
		LOGGER.debug(messageIn);
	}

	public void stopService(){
		// nothing needed here.
	}

}
