package com.newrelic.codingchallenge.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileIOLoggingService implements LoggingService {

	private FileChannel fileChannel;
	private static final Logger LOGGER = LoggerFactory.getLogger(FileIOLoggingService.class);


	public FileIOLoggingService() {
		try {
			fileChannel = new RandomAccessFile("numbers.log", "rw").getChannel();
		} catch (FileNotFoundException e) {
			LOGGER.error("Unable to access file.");
		}
	}

	@Override
	public void logMessage(String messageIn) {
		byte[] buffer = (messageIn + "\n").getBytes();

		ByteBuffer wrBuf;
		wrBuf = ByteBuffer.wrap(buffer);
		try {
			fileChannel.write(wrBuf);
		} catch (IOException e) {
			LOGGER.error("Unable to write to file:  " + e);
		}
	}

	public void stopService() {
		try {
			fileChannel.close();
		} catch (IOException e) {
			LOGGER.error("Unable to close file channel.");
		}
	}
}
