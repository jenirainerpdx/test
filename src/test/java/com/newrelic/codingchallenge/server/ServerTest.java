package com.newrelic.codingchallenge.server;

import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

// looking for throughput details - requires that server is already running.
@State(Scope.Thread)
public class ServerTest {

	static private PrintWriter writer;
	static private OutputStream outputStream;
	static private Socket socket;


	public void openConnections() throws IOException {
		socket = new Socket("127.0.0.1", 4000);
		outputStream = socket.getOutputStream();
		writer = new PrintWriter(outputStream);
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@Test
	public void sendin1000() {
		try {
			openConnections();
		} catch (IOException e) {
			System.out.println("Unable to open connection:  " + e);
		}
		for (int i=0; i < 1000; i++){
			writeNumber(100000000 + i);
		}
		try {
			closeWriterAndSocket();
		} catch (IOException e) {
			System.out.println("Unable to close connections:  " + e);
		}
	}

	private void closeWriterAndSocket() throws IOException {
		writer.flush();
		writer.close();
		socket.close();
	}

	public void writeNumber(final int num) {
		writer.println(String.format("%09d", num));
		writer.flush();
	}

}