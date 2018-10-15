package com.newrelic.codingchallenge.server;

import com.newrelic.codingchallenge.server.service.TallyService;
import com.newrelic.codingchallenge.server.service.TallyServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Receives incoming messages from clients:
 * - if non-numeric, determine if it is "terminate".  If so, shutdown.  If not, discard.
 * - if length > 9, discard
 * - if length < 9, lpad with 0's
 * Messages, when 9 digits long, pass to queue.
 */
public class MessageFilter implements Callable<Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageFilter.class);
	private final Socket socket;

	private MessageValidator validator;
	private TallyService tallyService;

	public MessageFilter(Socket client) {
		this.socket = client;
		validator = new MessageValidatorImpl();
		tallyService = new TallyServiceImpl();
	}

	@Override
	public Integer call() throws InterruptedException {
		LOGGER.debug("Message filter has been called.");
		try (
				DataInputStream ois = new DataInputStream(socket.getInputStream())
		) {
			while (true) {
				String message = ois.readUTF();
				LOGGER.debug("Incoming message:  " + message);
				if (validator.messageAllNumeric(message)) {
					LOGGER.info("Numeric message received.");
					String validatedMessage = null;
					if (message.length() > 9) {
						LOGGER.info("Discarding message because it was greater than 9 digits:  " + message);
						// close connection; bye.
					} else if (message.length() < 9) {
						validatedMessage = StringUtils.leftPad(message, 9, '0');
					} else {
						validatedMessage = message;
					}
					if (validatedMessage != null) {
						LOGGER.debug("Got a valid message:  " + validatedMessage);
						tallyService.putNumberOnQueue(validatedMessage);
					}
				} else if (validator.messageTerminate(message)) {
					SocketManager.stopServer();
				} else {
					LOGGER.error("String input received.  Discarding message:  " + message);
				}
			}

		} catch (IOException e) {
			LOGGER.warn("Unable to receive message:  " + e);
		}

		return 1; // return is not used.  Exception thrown is. (for now... )
	}
}
