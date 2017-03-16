package com.java.view;

import java.awt.BorderLayout;
import java.awt.Color;
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
	private Board board;
	
   public Checkers(String title)
   {
      super(title);	 
      
      setIconImage(new ImageIcon("img/icone.png").getImage());
      setResizable(false);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
     
      init_menu();
      init_board();
      init_score();
    	  
      pack();
      setVisible(true);
	  setLocationRelativeTo(null);

   }
   
   private void init_board(){
	   
	  if(board != null)
		  remove(board);
	  
      board = new Board();
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
      
      add(board);
      pack();
   }
   
	private void init_menu() {
		JMenuBar bar = new JMenuBar();
		
		JMenu file = new JMenu("Fichier");
		JMenuItem new_game = new JMenuItem("Nouvelle partie" );
		JMenuItem quit = new JMenuItem("Quitter" );
			
		JMenu game = new JMenu("Jeu");
		JMenuItem createServer = new JMenuItem("Organiser");
		JMenuItem join = new JMenuItem("Rejoindre");
		JMenuItem save = new JMenuItem("Sauvegarder");
		JMenuItem load = new JMenuItem("Charger");
		JMenuItem restart = new JMenuItem("Recommencer");
		
		JMenu about = new JMenu("A propos");
		JMenuItem rules = new JMenuItem("Regle du jeu" );
		JMenuItem help = new JMenuItem("?" );
		
		file.add(new_game);
		file.addSeparator();
		file.add(quit);
		game.add(createServer);
		game.add(join);
		game.add(save);
		game.add(load);
		game.add(restart);
		about.add(rules);
		about.add(help);
		
		bar.add(file);
		bar.add(game);
		bar.add(about);
		
		// Option "Nouvelle partie"
		new_game.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		new_game.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		init_board();
	    	}
	    });

		
		// Option "Quitter"
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK));
		
		quit.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		System.exit(0);
	    	}
	    });
		
		// Option "?"
		help.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
    		JOptionPane.showMessageDialog(null, "Createurs : Kevin BOUCHER - Fabien DIDIER\nVersion : 0.1",
						    		            "Information", JOptionPane.NO_OPTION);
	    	}
	    });

		add(bar,BorderLayout.NORTH);
	}
	
	private void init_score() {

		JPanel score = new JPanel();
		score.add(new JLabel("Score : 0"));
		score.setBorder(BorderFactory.createLineBorder(Color.black));
		add(score, BorderLayout.SOUTH);
	}  

}