package com.jentest.sockets.server.service;

import java.util.List;

public interface LoggingService {

	void logMessage(String messageIn);
	void logMessages(List<String> messages);
	void stopService();
}
