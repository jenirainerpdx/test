package com.newrelic.codingchallenge.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.assertTrue;

public class ServerTest {

	@Test
	public void notATest(){
		assertTrue("This is a placeholder.", true);
	}

//	@Test
	public void tryTenClients() throws Exception {
		//String[] args = new String[]{};
		//SocketManager.main(args);
		long n;

		for (int i=0; i < 11; i++) {
			n = Math.round((Math.random()*4839) + i);
			System.out.println("value of n:  " + n);
			if (n > 999999999) {
				n = n/10; // close enough
			}

			sendit(n);
		}
		sendit("nope");
		// sendit("terminate");
		assertTrue("This is just to demo how to call the server.", true);
	}

	private void sendit(long n) throws IOException {
		Socket client = new Socket("localhost", 4000);
		DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		dos.writeUTF("" + n);
		dos.close();
		client.close();
	}

	private void sendit(String msg) throws IOException {
		Socket client = new Socket("localhost", 4000);
		DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		dos.writeUTF(msg);
		dos.close();
		client.close();
	}

}