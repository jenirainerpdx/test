package com.newrelic.codingchallenge.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestImpl implements Request {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestImpl.class);
	private String message;
	private Integer integerMessage;
	private String threadOrigin;
	private Integer requestId;
	private static final MessagesReceivedCounter sequenceGenerator =
			new MessagesReceivedCounter();

	public RequestImpl(String msg, String origin) {
		message = msg;
		threadOrigin = origin;
		requestId = sequenceGenerator.gotAMessage();
		try {
			integerMessage = Integer.parseInt(msg);
		} catch (NumberFormatException cce) {
			LOGGER.error("tried to parse: " + msg + " got NFE.  " + cce);
		}
	}

	@Override
	public Integer getRequestId() {
		return requestId;
	}

	@Override
	public void setRequestId(Integer id) {
		requestId = id;
	}

	@Override
	public String getStringMessage() {
		return message;
	}

	@Override
	public Integer getIntegerMessage() {
		return integerMessage;
	}

	@Override
	public Boolean isValid() {
		Boolean valid = Boolean.FALSE;
		if (message == null) {
			valid = Boolean.FALSE;
			return valid;
		}
		if (isTerminate()) {
			valid = Boolean.TRUE;
		} else {
			try {
				if (Integer.parseInt(message) > 0) {
					valid = Boolean.TRUE;
				}
			} catch (ClassCastException cce) {
				// not an integer.
				valid = Boolean.FALSE;
			}
		}
		return valid;
	}

	@Override
	public Boolean isTerminate() {
		Boolean term = Boolean.FALSE;
		if (message.equalsIgnoreCase("terminate")) {
			term = Boolean.TRUE;
		}
		return term;
	}

	public void setMessage(String message) {
		this.message = message;
		setIntegerMessage(Integer.parseInt(message));
	}

	public void setIntegerMessage(Integer integerMessage) {
		this.integerMessage = integerMessage;
	}

	public String getMessage() {
		return message;
	}

	public String getThreadOrigin() {
		return threadOrigin;
	}

	public void setThreadOrigin(String threadOrigin) {
		this.threadOrigin = threadOrigin;
	}

	@Override
	public String toString() {
		return "RequestImpl{" +
				"integerMessage=" + integerMessage +
				", threadOrigin='" + threadOrigin + '\'' +
				", requestId=" + requestId +
				'}';
	}
}
