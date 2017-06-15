package com.java.model;
import java.io.*;
import java.net.*;
import java.util.Timer;

import com.java.view.Board;
import com.java.view.Checkers;
public class SocketManager implements Runnable {

	private static final int port = 9876;
	private static BufferedReader plec;
	private static PrintWriter pred;
	private static Socket socket;
	public static boolean endThreadServer = false;
	public static boolean attenteAdv = false;

	public static int init_Socket() {
		try {
		// socket = new Socket("37.59.36.109", port);
		socket = new Socket("127.0.0.1", port);
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
		System.out.println("Message reçu : " + tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}

	public static void close_all() {
		try {
		endThreadServer = true;
		plec.close();
		pred.close();
		socket.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Constructeur
	public SocketManager() { }

	@Override
	public void run() {
		System.out.println("Démarrage du thread SocketManager");
		
		if(Checkers.joueur_nb == 2)
		{
			System.out.println("Premier tour du joueur 1, en attente ...");
			Checkers.board.moveCheckersAdv();
		}
		
		while(!endThreadServer)
		{
			if(attenteAdv)
			{
				Checkers.board.moveCheckersAdv();
				attenteAdv = false;
			}
			
			// Allègement du CPU
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
