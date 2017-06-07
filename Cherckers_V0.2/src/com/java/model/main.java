package com.java.model;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

import com.java.view.Board;
import com.java.view.Checkers;

public class main {

	public static void main(String[] args) {
		 if (SocketManager.init_Socket() == 0) {
			 int nb = Integer.parseInt(SocketManager.wait_recv());
			 Runnable r = new Runnable() {
              @Override
              public void run() {
            	  
            	  new Checkers(nb);
              }
           };
           EventQueue.invokeLater(r);
		} 
		else {
			JOptionPane.showMessageDialog(null, "Erreur de connexion au serveur !", "Connexion impossible", JOptionPane.ERROR_MESSAGE);
			System.out.println("[SERVER]Connection error..");
		}
	}
}