package com.newrelic.codingchallenge.client;

import java.io.DataOutputStream;
import java.net.Socket;

public class SimpleClient {


	public static void main(String[] args) throws Exception {
		Socket clientSocket = new Socket("localhost", 4000);
		DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
		dos.writeUTF("19988888");
		dos.close();
		clientSocket.close();
	}
}
