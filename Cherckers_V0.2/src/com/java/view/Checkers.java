package com.java.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


public class Checkers extends JFrame
{
   public Checkers(String title)
   {
      super(title);
      
      setIconImage(new ImageIcon("img/icone.png").getImage());
      setResizable(false);
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
	  setLocationRelativeTo(null);

   }
   

}