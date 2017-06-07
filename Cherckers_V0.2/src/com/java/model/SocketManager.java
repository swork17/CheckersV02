package com.java.model;
import java.io.*;
import java.net.*;
public class SocketManager {

	private static final int port = 9876;
	private static BufferedReader plec;
	private static PrintWriter pred;
	private static Socket socket;

	public static int init_Socket() {
		try {
		socket = new Socket("127.0.0.1", port);
		//socket.setSoTimeout(50000);
		System.out.println("SOCKET = " + socket);

		plec = new BufferedReader(
				new InputStreamReader(socket.getInputStream())
				);

		pred = new PrintWriter(
				new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())),
				true);
		return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	public static void send(String info) {
		pred.println(info);
	}
	
	public static String wait_recv() {
		String tmp = null;
		try {
		tmp = plec.readLine();
		//Thread.sleep(200);
		System.out.println(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}

	public static void close_all() {
		try {
		plec.close();
		pred.close();
		socket.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
