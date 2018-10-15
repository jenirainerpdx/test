package com.newrelic.codingchallenge.server.service;

/**
 * Every 10 seconds send message to console out with message like:
 * Received 50 unique numbers, 2 duplicates. Unique total: 567231
 */
public interface NotifyService {

	void scheduleNotifications();

}
