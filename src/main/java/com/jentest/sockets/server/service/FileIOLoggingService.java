package com.jentest.sockets.server.service;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

@State(Scope.Benchmark)
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

	@Override
	public void logMessages(List<String> messages) {
		LOGGER.debug("processing " + messages.size() + " messages.");
		ByteBuffer[] messageBuffers = new ByteBuffer[messages.size()];
		for (int i = 0; i < messageBuffers.length; i++) {
			String s = messages.get(i);
			byte[] buffer = (s + "\n").getBytes();
			ByteBuffer writeBuffer = ByteBuffer.wrap(buffer);
			messageBuffers[i] = writeBuffer;
		}

		try {
			fileChannel.write(messageBuffers);
		} catch (IOException e) {
			LOGGER.error("Unable to write messages to log:  " + e);
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
