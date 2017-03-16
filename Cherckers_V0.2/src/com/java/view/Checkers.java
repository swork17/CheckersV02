package com.java.view;

import java.awt.EventQueue;

import javax.swing.JFrame;


public class Checkers extends JFrame
{
   public Checkers(String title)
   {
      super(title);
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      Board board = new Board();
      for (int i=1; i < 11; i++) {
    	  for (int j=1; j < 11; j++) {
    		  if(((i+j) % 2)==0) 
    			  if (i < 5) 
    				  board.add(new Checker(CheckerType.BLACK_REGULAR), i, j);
    		  else 
    			  if (i > 6)
    				  board.add(new Checker(CheckerType.RED_REGULAR), i, j);
    	  }
      }
      setContentPane(board);
      pack();
      setVisible(true);
   }
}