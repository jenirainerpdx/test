package com.newrelic.codingchallenge.server;

import org.apache.commons.lang3.StringUtils;

public class MessageValidatorImpl implements MessageValidator {

	@Override
	public Boolean messageAllNumeric(String message) {
		Boolean messageContainsNonNumericCharacters = Boolean.FALSE;
		if (StringUtils.isNumeric(message)) {
			messageContainsNonNumericCharacters = Boolean.TRUE;
		}
		return messageContainsNonNumericCharacters;
	}

	@Override
	public Boolean messageTerminate(String message) {
		Boolean terminate = Boolean.FALSE;
		if (message.equalsIgnoreCase("terminate")) {
			terminate = Boolean.TRUE;
		}
		return terminate;
	}
}
