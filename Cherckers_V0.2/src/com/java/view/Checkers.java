package com.java.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
	private JLabel lbscore;
	JButton bt_turn;
	public boolean myTurn = true;
	
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
	  
      board = new Board(this);
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
			      init_score();
	    	}
	    });

		
		// Option "Quitter"
		//quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK));
		
		quit.addActionListener(new ActionListener(){
			@Override
	    	public void actionPerformed(ActionEvent e){
	    		System.exit(0);
	    	}
	    });
		
		// Option "?"
		help.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
    		JOptionPane.showMessageDialog(null, "Createurs : Kevin BOUCHER - Fabien DIDIER\nVersion : 0.2",
						    		            "Information", JOptionPane.NO_OPTION);
	    	}
	    });

		add(bar,BorderLayout.NORTH);
	}
	
	private void init_score() {

		JPanel score = new JPanel();
		bt_turn = new JButton();
		lbscore = new JLabel("Score : 20 - 20");
		bt_turn.setText("Terminer son tour");
		bt_turn.setEnabled(true);
		myTurn = true;
		
		bt_turn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				if(bt_turn.isEnabled()) {
					bt_turn.setText("A votre adversaire");
					bt_turn.setEnabled(false);
					myTurn = false;
				}
				else {
					bt_turn.setText("Terminer son tour");
					bt_turn.setEnabled(true);
					myTurn = true;
				}
			}
		});
		
		score.setLayout(new BorderLayout());
		score.add(lbscore, BorderLayout.WEST);
		score.add(bt_turn, BorderLayout.EAST);
		score.setBorder(BorderFactory.createLineBorder(Color.black));
		add(score, BorderLayout.SOUTH);
	}  
	
	public void set_lbscore(int nbCheckHaut, int nbCheckbas) {
		String lb = "Score : " +  nbCheckHaut + " - " + nbCheckbas;
		
		if(nbCheckHaut == 0 || nbCheckbas == 0) 
		{
			lb = "Fin de la partie";
			bt_turn.setEnabled(false);
			myTurn = false;
		}
		
		lbscore.setText(lb);
	}
}
