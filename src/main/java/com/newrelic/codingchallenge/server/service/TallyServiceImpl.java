package com.newrelic.codingchallenge.server.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.newrelic.codingchallenge.model.MessagesReceivedCounter;
import com.newrelic.codingchallenge.model.Request;
import com.newrelic.codingchallenge.model.RequestImpl;
import com.newrelic.codingchallenge.model.ValueMap;
import com.newrelic.codingchallenge.server.SocketListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class TallyServiceImpl implements TallyService, Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(TallyServiceImpl.class);
	private static int rollingUniqueCount;
	private static int rollingDupeCount;
	private static int rollingTotal;
	private static int priorTotalCount;
	private final LoggingService loggingService = new LoggerLoggingService();

	private static ValueMap valueMap;
	private static MessagesReceivedCounter totalMessagesCounter;
	private static MessagesReceivedCounter duplicateCounter;
	ThreadFactory pollerThreads = new ThreadFactoryBuilder().setNameFormat("pollerThreads-%d").build();
	private ScheduledExecutorService tallyPoller = Executors.newScheduledThreadPool(30, pollerThreads);
	private static int currentRollingUniqueTotal;
	private static int priorRollingUnique;
	private static MessagesReceivedCounter uniqueCounter;


	public TallyServiceImpl(ValueMap values, MessagesReceivedCounter counter,
							MessagesReceivedCounter dupeCounter,
							MessagesReceivedCounter uniques
			) {
		valueMap = values;
		totalMessagesCounter = counter;
		duplicateCounter = dupeCounter;
		uniqueCounter = uniques;
		schedulePolling();
	}

	private void schedulePolling(){
		tallyPoller.scheduleAtFixedRate(() -> run(),
										5,
										2000L,
								TimeUnit.MILLISECONDS);
	}

	public void snapshot() {

		ConcurrentNavigableMap<Integer, Integer> tenSecondMap = valueMap.getEventsFromLastTenSeconds();

		// Totals:
		int currentTotalCount = totalMessagesCounter.getTotalReceivedCount();
		int changeInTotalCount = currentTotalCount - priorTotalCount; //5
		// now set prior to current
		priorTotalCount = currentTotalCount; //5
		LOGGER.debug("Current total:  " + currentTotalCount + "  changeInTotal:  " + changeInTotalCount);


		currentRollingUniqueTotal = valueMap.size();
		rollingUniqueCount = currentRollingUniqueTotal - priorRollingUnique;
		priorRollingUnique = currentRollingUniqueTotal;

		rollingDupeCount = changeInTotalCount - rollingUniqueCount; // 5

		rollingTotal = valueMap.size();
		for (Integer val : tenSecondMap.values()) {
			loggingService.logMessage(StringUtils.leftPad("" + val, 9, "0"));
		}
	}

	public void stopService(){
		snapshot();
		loggingService.stopService();
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

	void putNumberOnQueue(Request request) {
		totalMessagesCounter.gotAMessage();
		if (valueMap.valueExists(request.getIntegerMessage())) {
			duplicateCounter.gotAMessage();
			LOGGER.debug("Found a value which is already in the map.  Not logging this one.  " + request.getIntegerMessage());
			// do we need to add this to the map? maybe.
		} else {
			uniqueCounter.gotAMessage();
			valueMap.acceptEvent(request);
		}
	}

	public void showMe() {
		this.valueMap.showme();
	}

	@Override
	public void run() {
		LOGGER.trace("in tallyservice.run");
		// only gets numeric messages.
		LinkedBlockingQueue<String> queue = SocketListener.messagesQueue;
		if (queue.isEmpty()) {
			LOGGER.trace("no messages in the queue to process.");
		} else {
			List<String> readMessages = new ArrayList<>();
			queue.drainTo(readMessages);
			LOGGER.debug("just added to tmc.  : " + totalMessagesCounter.getTotalReceivedCount());
			for (String s : readMessages) {
				Request request = new RequestImpl(s, Thread.currentThread().getName());
				putNumberOnQueue(request);
			}
		}
	}
}

