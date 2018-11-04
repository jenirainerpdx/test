package com.newrelic.codingchallenge.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.newrelic.codingchallenge.model.MessagesReceivedCounter;
import com.newrelic.codingchallenge.model.ValueMap;
import com.newrelic.codingchallenge.server.service.NotificationService;
import com.newrelic.codingchallenge.server.service.TallyService;
import com.newrelic.codingchallenge.server.service.TallyServiceImpl;
import com.newrelic.codingchallenge.server.service.TallyServiceStreams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;


/**
 * @author Brian Goetz and Tim Peierls originally
 * adapted by Jeni Rainer
 * <p>
 * <p>
 * Based on LifecycleWebServer from JCIP with heavy modifications
 * <p/>
 * Web server with shutdown support
 */
public class SocketListener {

	static ThreadFactory server = new ThreadFactoryBuilder().setNameFormat("server-%d").build();
	private static final ExecutorService exec = Executors.newFixedThreadPool(5, server);
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketListener.class);
	static ThreadFactory notificationsThreads = new ThreadFactoryBuilder().setNameFormat("Notifications-%d").build();
	private static final ScheduledExecutorService notificationScheduler =
			Executors.newScheduledThreadPool(1, notificationsThreads);
	private static final ValueMap valueMap = new ValueMap();
	private static final MessagesReceivedCounter messagesReceivedCounter = new MessagesReceivedCounter();
	private static final MessagesReceivedCounter dupeCounter = new MessagesReceivedCounter();
	private static final MessagesReceivedCounter uniqueCounter = new MessagesReceivedCounter();
	private TallyService tallyService;

	public static LinkedBlockingQueue<String> messagesQueue;

	public void start() throws IOException {
		messagesQueue = new LinkedBlockingQueue<>();
		ServerSocket socket = new ServerSocket(4000);
		tallyService = new TallyServiceStreams(valueMap, messagesReceivedCounter, dupeCounter, uniqueCounter);
		scheduleNotifications();
		while (!exec.isShutdown()) {
			try {
				final Socket conn = socket.accept();
				exec.execute(() -> {
					try {
						handleRequest(conn);
					} catch (InterruptedException e) {
						LOGGER.error("Unable to shutdown service.  " + e);
						exec.shutdownNow();
					}
				});
			} catch (RejectedExecutionException e) {
				if (!exec.isShutdown())
					LOGGER.error("task submission rejected", e);
			}
		}
	}

	/**
	 * Runnable for scheduling the Notification service.
	 */
	private void scheduleNotifications() {
		notificationScheduler.scheduleAtFixedRate(() -> {
			NotificationService notificationService = new NotificationService(tallyService);
			notificationService.run();
		}, 0, 10L, TimeUnit.SECONDS);
	}

	/**
	 * Public interface call to shutdown the server
	 */
	public void stop() throws InterruptedException {
		// force snapshot and log.
		tallyService.snapshot();

		tallyService.stopService();
		exec.shutdown();
		if (exec.awaitTermination(2, TimeUnit.SECONDS)) {
			LOGGER.warn("shutting down.");
		}
		System.exit(1);
	}

	void handleRequest(Socket connection) throws InterruptedException {
		boolean closeStream = false;
		try (
				InputStream is = connection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				Scanner scanner = new Scanner(isr);

		) {
			LOGGER.debug(Thread.currentThread().getName() + "  Accepted client connection");

			while (!closeStream) {
				String strValue = null;
				if (scanner.hasNext()) {
					strValue = scanner.nextLine();
				}

				if (StringUtils.length(strValue) != 9) {
					LOGGER.error("invalid input:  " + strValue);
					closeStream = true;
				} else if (StringUtils.isNumeric(strValue)){
					messagesQueue.add(strValue);
				} else {
					LOGGER.error("We have a string:  " + strValue);
					if (strValue.equalsIgnoreCase("terminate")) {
						stop();
					} else {
						LOGGER.error("invalid input:  " + strValue);
						closeStream = true;
					}
				}
			}

		} catch (IOException e) {
			LOGGER.error("Unable to open input stream for client:  " + e);
		}
		if (closeStream) {
			try {
				connection.close();
			} catch (IOException e) {
				LOGGER.error("Unable to close connection:  " + e);
			}
		}
	}

}