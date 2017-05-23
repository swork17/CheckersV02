package com.java.model;

import java.awt.EventQueue;


import com.java.view.Board;
import com.java.view.Checkers;

public class main {

	public static void main(String[] args) {
		   Runnable r = new Runnable() {
              @Override
              public void run() {
            	  /*String bip = "bip.mp3";
            	  Media hit = new Media(new File(bip).toURI().toString());
            	  MediaPlayer mediaPlayer = new MediaPlayer(hit);
            	  mediaPlayer.play();*/
                 new Checkers("Checkers");
              }
           };
           EventQueue.invokeLater(r);
	}
}
