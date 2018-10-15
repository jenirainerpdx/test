package com.newrelic.codingchallenge.server.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class NotifyServiceImpl implements NotifyService {



	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private final TallyService tallyService = new TallyServiceImpl();

	@Override
	public void scheduleNotifications() {
		final Runnable notifier = () -> {
			tallyService.snapshot();
			Integer newUniques = tallyService.getNewUniques();
			Integer newDupes = tallyService.getNewDupes();
			Integer uniqueTotal = tallyService.getRunningTotalUnique();
			System.out.println(Thread.currentThread().getName() + "  Received " + newUniques +
					" unique numbers, " + newDupes + " duplicates. Unique total: "
					+ uniqueTotal);
		};
		final ScheduledFuture<?> notifierHandle = scheduler.scheduleAtFixedRate(
				notifier, 10, 10, TimeUnit.SECONDS);
	}
}
