package com.java.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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

import com.java.model.SocketManager;


public class Checkers extends JFrame
{
	public static Board board;
	private JLabel lbscore;
	protected JLabel lb_turn;	
	public boolean myTurn = true;
	public static int joueur_nb;
	
   public Checkers(int nb)
   {
      super("Checkers joueur " + nb);	 
      joueur_nb = nb;
      
      setIconImage(new ImageIcon("img/icone.png").getImage());
      setResizable(false);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
     
      init_menu();
      init_board();
      init_score();
      set_lbscore(20, 20);
      pack();
      setVisible(true);
	  setLocationRelativeTo(null);
	  
	  // Lance le thread pour envoyer/r�cuperer les r�ponses du serveur
	  new Thread(new SocketManager()).start();
   }
   
   private void init_board(){
	   
	  if(board != null)
		  remove(board);
	  
      board = new Board(this);
      for (int i=1; i < 11; i++) {
    	  for (int j=1; j < 11; j++) {
    		  if(((i+j) % 2)==0) 
    			  if (i < 5) 
    				  board.add(new Checker(CheckerType.CHECKER_JOUEUR1), i, j);
    		  else 
    			  if (i > 6)
    				  board.add(new Checker(CheckerType.CHECKER_JOUEUR2), i, j);
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
			      Board.tour = 2;
			      init_score();
			      set_lbscore(20, 20);
			      SocketManager.send("11");
	    	}
	    });

		
		// Option "Quitter"
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK));
		
		quit.addActionListener(new ActionListener(){
			@Override
	    	public void actionPerformed(ActionEvent e){
				SocketManager.close_all();
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
		lb_turn = new JLabel("Ton tour");
		lbscore = new JLabel("Score : 20 - 20");
		
		score.setLayout(new BorderLayout());
		score.add(lbscore, BorderLayout.WEST);
		score.add(lb_turn, BorderLayout.EAST);
		score.setBorder(BorderFactory.createLineBorder(Color.black));
		add(score, BorderLayout.SOUTH);
	}  
	
	public void set_lbscore(int nbCheckHaut, int nbCheckbas) {
		String lb = "Score : " +  nbCheckHaut + " - " + nbCheckbas;
		String w_turn = "Ton tour";
		myTurn = true;
			
		if(  (Board.tour % 2 == 0 && joueur_nb == 2)
		   || Board.tour % 2 != 0 && joueur_nb == 1)
		{
			myTurn = false;
			w_turn = "En attente de votre adversaire";
		}
		
		if(    (joueur_nb == 1 && nbCheckHaut == 0)
			|| (joueur_nb == 2 && nbCheckbas == 0))
			w_turn = "Gagne";
			
		if(    (joueur_nb == 1 && nbCheckbas == 0)
			|| (joueur_nb == 2 && nbCheckHaut == 0))
			w_turn = "Perdu";
					
			lb_turn.setText(w_turn);
		
		lbscore.setText(lb);
		repaint();
		
	}
}
