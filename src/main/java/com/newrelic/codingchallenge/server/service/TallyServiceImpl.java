package com.newrelic.codingchallenge.server.service;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TallyServiceImpl implements TallyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TallyServiceImpl.class);
	static final Multiset values;
	private static int rollingUniqueCount;
	private static int rollingDupeCount;
	private static int rollingTotal;
	private static int priorTotalCount;
	private static int priorRollingUnique;
	private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private final LoggingService loggingService = new LoggingServiceImpl();
	private static ConcurrentLinkedQueue<String> messagesQueue;

	@Override
	public void pollMessagesQueue() {
		final Runnable poller = () -> {
			String message = readFromQueue();
			if (message != null) {
				boolean logit = false;
				if (!values.contains(message)) {
					logit = true; // only log unique values.
				}
				values.add(message);
				LOGGER.debug("Just pulled " + message + " from queue and put it into our multiset.  " +
						"  Queue size:  " + getQueueSize());
				if (logit) {
					loggingService.logMessage(message);
				}
			} else {
				LOGGER.debug("Empty queue.");
			}

		};
		final ScheduledFuture<?> pollingHandle = executor.scheduleAtFixedRate(
				poller, 10, 50, TimeUnit.MILLISECONDS);
	}

	static {
		// intentionally not concurrent; does not need to be
		// because we are dealing w/ one thread on the back end.
		values = HashMultiset.create();
		messagesQueue = new ConcurrentLinkedQueue<>();
	}

	public void snapshot() {

		int currentTotalCount = values.size(); // this is total size
		int currentUniqueCount = values.elementSet().size();
		int changeInTotalCount = currentTotalCount - priorTotalCount;
		int changeInUniqueCount = currentUniqueCount - rollingTotal;
		int currentDupeCount = changeInTotalCount - changeInUniqueCount;

		priorRollingUnique = rollingTotal;


		rollingUniqueCount = currentUniqueCount - priorRollingUnique;
		rollingTotal = currentUniqueCount;
		rollingDupeCount = currentDupeCount;
		priorTotalCount = currentTotalCount;
	}

	@Override
	public void resetCounters() {
		rollingDupeCount = 0;
		rollingUniqueCount = 0;
		rollingTotal = 0;
		priorTotalCount = 0;
	}

	@Override
	public Integer getNewUniques() {
		return rollingUniqueCount;
	}


	@Override
	public Integer getNewDupes() {
		return rollingDupeCount;
	}

	@Override
	public Integer getRunningTotalUnique() {
		return rollingTotal;
	}


	@Override
	public void putNumberOnQueue(String message) {
		LOGGER.debug("adding " + message + " to the queue.");
		messagesQueue.offer(message);
	}

	@Override
	public String readFromQueue(){
		return messagesQueue.poll();
	}

	@Override
	public int getQueueSize() {
		return messagesQueue.size();
	}
}
