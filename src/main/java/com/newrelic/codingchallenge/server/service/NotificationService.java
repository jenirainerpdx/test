package com.newrelic.codingchallenge.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationService implements Runnable {

	private TallyService tallyService;
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

	public NotificationService(TallyService tally) {
		tallyService = tally;
	}

	@Override
	public void run() {
		// whatever notifier does.
		tallyService.snapshot();
		Integer newUniques = tallyService.getNewUniques();
		Integer newDupes = tallyService.getNewDupes();
		Integer uniqueTotal = tallyService.getRunningTotalUnique();

		LOGGER.error(Thread.currentThread().getName() + "  Received " + newUniques +
				" unique numbers, " + newDupes + " duplicates. Unique total: "
				+ uniqueTotal);

	}
}
