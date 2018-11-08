package com.newrelic.codingchallenge.server;

import org.openjdk.jmh.runner.RunnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class Server extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	public static void main(String[] args) {
		try {
			SocketListener server = new SocketListener();
			server.start();
			org.openjdk.jmh.Main.main(args);
		} catch (IOException e) {
			LOGGER.error("Unable to start server:  " + e);
		} catch (RunnerException e) {
			LOGGER.error("unable to start jmh  " + e);
		}
	}

}
