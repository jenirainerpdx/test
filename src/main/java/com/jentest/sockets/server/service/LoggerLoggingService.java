package com.jentest.sockets.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LoggerLoggingService implements LoggingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerLoggingService.class);

	public LoggerLoggingService(){
	}

	@Override
	public void logMessage(String messageIn) {
		LOGGER.debug(messageIn);
	}

	@Override
	public void logMessages(List<String> messages) {
		for (String s : messages) {
			logMessage(s);
		}
	}

	public void stopService(){
		// nothing needed here.
	}

}
