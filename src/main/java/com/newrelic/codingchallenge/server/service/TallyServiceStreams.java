package com.newrelic.codingchallenge.server.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.newrelic.codingchallenge.model.MessagesReceivedCounter;
import com.newrelic.codingchallenge.model.Request;
import com.newrelic.codingchallenge.model.RequestImpl;
import com.newrelic.codingchallenge.model.ValueMap;
import com.newrelic.codingchallenge.server.SocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class TallyServiceStreams implements TallyService {

	ThreadFactory pollerThreads = new ThreadFactoryBuilder().setNameFormat("pollerThreads-%d").build();
	private ScheduledExecutorService tallyPoller = Executors.newScheduledThreadPool(30, pollerThreads);
	private static MessagesReceivedCounter totalMessagesCounter;
	private static int priorTotalReceived = 0;
	private static int rollingTotalReceived = 0;
	private static int rollingUniqueCount = 0;
	private static int priorUniqueCount = 0;
	private static MessagesReceivedCounter duplicateCounter;
	private static MessagesReceivedCounter uniqueCounter;
	private static ValueMap valueMap;
	private static LoggingService loggingService;

	private static final Logger LOGGER = LoggerFactory.getLogger(TallyServiceStreams.class);
	private int rollingDupeCount =0;
	private int currentTotalUnique =0;

	public TallyServiceStreams(ValueMap values, MessagesReceivedCounter counter,
							MessagesReceivedCounter dupeCounter,
							MessagesReceivedCounter uniques
	) {
		valueMap = values;
		totalMessagesCounter = counter;
		duplicateCounter = dupeCounter;
		uniqueCounter = uniques;
		schedulePolling();
		loggingService = new FileIOLoggingService();
	}

	private void schedulePolling(){
		tallyPoller.scheduleAtFixedRate(() -> {
			run();
		}, 5, 200L, TimeUnit.MILLISECONDS);
	}


	private void run(){
		LinkedBlockingQueue<String> queue = SocketListener.messagesQueue;
		Stream.generate(() -> {
			try {
				return queue.take();
			} catch (InterruptedException e) {
				LOGGER.error(e.getLocalizedMessage());
				return "terminate";
			}
		})
				.parallel()
				.map(s -> { LOGGER.debug("mapping:  " + s);
					totalMessagesCounter.gotAMessage(); return s; })
				.distinct()
				.map(s -> { LOGGER.debug("mapping:  " + s); uniqueCounter.gotAMessage(); return s;})
				.forEach(o -> {
					LOGGER.debug("handling request:  " + o);
					loggingService.logMessage(o);
				});
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
		return currentTotalUnique;
	}

	@Override
	public void snapshot() {
		int currentTotalReceived = totalMessagesCounter.getTotalReceivedCount();
		rollingTotalReceived = currentTotalReceived - priorTotalReceived;
		currentTotalUnique = uniqueCounter.getTotalReceivedCount();
		rollingUniqueCount = currentTotalUnique - priorUniqueCount;
		rollingDupeCount = rollingTotalReceived - rollingUniqueCount;

		LOGGER.debug("Total received count:  " + rollingTotalReceived);
		LOGGER.debug("Unique received count:  " + uniqueCounter.getTotalReceivedCount());

		// reset prior values to current after all calculations are done.
		priorTotalReceived = currentTotalReceived;
		priorUniqueCount = currentTotalUnique;
	}

	@Override
	public void stopService() {

	}

	@Override
	public void resetCounters() {

	}

	@Override
	public void putNumberOnQueue(Request request) {
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
}
