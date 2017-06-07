package com.java.model;

import java.awt.EventQueue;


import com.java.view.Board;
import com.java.view.Checkers;

public class main {

	public static void main(String[] args) {
		 if (SocketManager.init_Socket() == 0) {
		String nb = SocketManager.wait_recv();
		   Runnable r = new Runnable() {
              @Override
              public void run() {
            	 
            		  new Checkers("Checkers joueur " + nb);
              }
           };
           EventQueue.invokeLater(r);
	} 
	else
		  System.out.println("[SERVER]Connection error..");
	}
}