package com.newrelic.codingchallenge.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingServiceImpl implements LoggingService {


	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingServiceImpl.class);

	@Override
	public String logMessage(String messageIn) {
		LOGGER.debug(messageIn);
		return messageIn;
	}

}
