package com.newrelic.codingchallenge.server;

import com.newrelic.codingchallenge.server.service.NotifyService;
import com.newrelic.codingchallenge.server.service.NotifyServiceImpl;
import com.newrelic.codingchallenge.server.service.TallyService;
import com.newrelic.codingchallenge.server.service.TallyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages pool of sockets; shuts down server cleanly
 */

public class SocketManager {

	private static final int CONNX = 5;
	private static final int SERVER_PORT = 4000;
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketManager.class);
	private static ExecutorService pool;
	private final NotifyService notifyService;
	private final TallyService tallyService;

	public SocketManager() {
		pool = Executors.newFixedThreadPool(CONNX);
		notifyService = new NotifyServiceImpl();
		tallyService = new TallyServiceImpl();
		try {
			SocketListener listener = new SocketListener();
			pool.submit(listener);
		} catch (IOException e) {
			LOGGER.error("Unable to construct SocketListener instance:  " + e);
		}
		notifyService.scheduleNotifications();
		tallyService.pollMessagesQueue();
	}

	class SocketListener implements Runnable {

		private ServerSocket serverSocket;

		public SocketListener() throws IOException {
			serverSocket = new ServerSocket(SERVER_PORT);
		}

		@Override
		public void run() {
			LOGGER.debug("SocketListener waiting for client connections.");
			while(true) {
				try {
					// note:  this is not correct.  We are opening unlimited sockets.
					// The pool is being used on the Listener Service.
					Socket client = serverSocket.accept();
					LOGGER.debug("Accepted client connection");
					MessageFilter messageFilter = new MessageFilter(client);
					pool.submit(messageFilter);
				} catch (Exception ie) {
					// write shutdown method to clear out log
					pool.shutdownNow();
					System.exit(1);
				}
			}
		}
	}

	public static void main(String[] args) {
		SocketManager server = new SocketManager();
	}

	public static void stopServer(){
		pool.shutdownNow();
		System.exit(0);
	}
}
