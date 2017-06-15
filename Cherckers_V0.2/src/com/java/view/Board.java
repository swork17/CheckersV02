package com.java.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.MouseEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.java.model.ParserRecv;
import com.java.model.SocketManager;

@SuppressWarnings("serial")

public class Board extends JComponent {

	private final static int SQUAREDIM = (int) (Checker.getDimension() * 1.25);
	private final int BOARDDIM = 10 * SQUAREDIM;
	private Dimension dimPrefSize;
	private boolean inDrag = false;
	private int deltax, deltay;
	private int oldcx, oldcy;
	private PosCheck posCheck;
	private List<PosCheck> posChecks;
	public static int tour = 2;
	public Checkers checkers;
	
	private class PosCheck {
		public Checker checker;
		public int cx;
		public int cy;
	}
	
	public Board(Checkers _checkers) 
	{
		checkers = _checkers;
		posChecks = new ArrayList<>();
		dimPrefSize = new Dimension(BOARDDIM, BOARDDIM);
		

		addMouseListener(new MouseAdapter()
		{			
			@Override
			public void mousePressed(MouseEvent me) {
								
				if(checkers.myTurn == false)
				{
					System.out.println("Ce n'est pas votre tour ...");
					return;
				}
				
				int x = me.getX();
				int y = me.getY();
				
				for (PosCheck posCheck: posChecks)
					if (Checker.contains(x, y, posCheck.cx, posCheck.cy))
					{
						// Pion de l'adversaire
						if(  (Checkers.joueur_nb == 1 && posCheck.checker.getType() == CheckerType.CHECKER_JOUEUR1)
				    	  || (Checkers.joueur_nb == 2 && posCheck.checker.getType() == CheckerType.CHECKER_JOUEUR2))
						{
							System.out.println("Ce ne sont pas vos pions !");
							return;
						}
						
						Board.this.posCheck = posCheck;
						oldcx = posCheck.cx;
						oldcy = posCheck.cy;
						deltax = x - posCheck.cx;
						deltay = y - posCheck.cy;
						inDrag = true;
						return;
					}
			}

			@Override
			public void mouseReleased(MouseEvent me) {
				if (inDrag)
					inDrag = false;
				else
					return;

				int x = me.getX();
				int y = me.getY();
				posCheck.cx = (x - deltax) / SQUAREDIM * SQUAREDIM + 
						SQUAREDIM / 2;
				posCheck.cy = (y - deltay) / SQUAREDIM * SQUAREDIM + 
						SQUAREDIM / 2;

				// Verification de l'emplacement du pion 
				boolean isValid = true;

				// Non valide si le pion sort du cadre
				if(posCheck.cx < 0 || posCheck.cy < 0
						|| posCheck.cy >= dimPrefSize.getHeight()
						|| posCheck.cx >= dimPrefSize.getWidth())
					isValid = false;

				int column = ((posCheck.cx - (SQUAREDIM /2)) / SQUAREDIM) + 1;
				int line = ((posCheck.cy - (SQUAREDIM /2)) / SQUAREDIM) + 1;

				int old_column = ((oldcx - (SQUAREDIM /2)) / SQUAREDIM) + 1;
				int old_line = ((oldcy - (SQUAREDIM /2)) / SQUAREDIM) + 1;

				if (line % 2 == 1) {
					if (column % 2 == 0)
						isValid = false;
				} else {
					if (column % 2 == 1)
						isValid = false;
				}
				
				int posChecker = 0;
				boolean f_manger = false;
				
				if (Board.this.posCheck.checker.getType() == CheckerType.CHECKER_JOUEUR2){
					if (tour % 2 != 0) {
						JOptionPane.showMessageDialog(null, "Ce n'est pas votre tour ...");
						isValid = false;
					}
				}
				else if (Board.this.posCheck.checker.getType() == CheckerType.CHECKER_JOUEUR1){
					if (tour % 2 == 0) {
						JOptionPane.showMessageDialog(null, "Ce n'est pas votre tour ...");
						isValid = false;
					}
				}
				
				if(	Board.this.posCheck.cx == oldcx && Board.this.posCheck.cy == oldcy)
				{
					isValid = false;
					System.out.println("Le pion n'a pas bouge, pas de changement de joueur !"); 
				}
				
				if(isValid == true) {
					for (PosCheck posCheck: posChecks)
					{				
						if (line > (old_line+1) 
								|| column > (old_column + 1) 
								|| line < ( old_line - 1) 
								|| column < (old_column - 1)) {
							isValid = false; 
							break;
						}
						
						// Possibilite de manger le pion
						if (posCheck != Board.this.posCheck && 
								posCheck.cx == Board.this.posCheck.cx &&
								posCheck.cy == Board.this.posCheck.cy)
						{
							if(posCheck.checker.getType() == Board.this.posCheck.checker.getType()){
								System.out.println("Impossible de manger ses propres pions !"); 
								isValid = false;
								break;
							}
							else{
								// Calcul de la nouvelle position du pion
								int newx = Board.this.posCheck.cx + (Board.this.posCheck.cx - oldcx);
								int newy = Board.this.posCheck.cy + (Board.this.posCheck.cy - oldcy);

								if(posLibre(newx, newy) == false){
									System.out.println("Impossible de manger le pion, l'emplacement est occupe !"); 
									isValid = false;
									break;
								}
								
								f_manger = true;
								// Deplacement du pion
								Board.this.posCheck.cx = newx;
								Board.this.posCheck.cy = newy;
								posChecks.remove(posChecker); // Suppression du pion mange
								set_score(); // Actualise le score
								revalidate();
								repaint();
								isValid = true;
								break;
							}
						}
						posChecker = posChecker + 1;
					}
				}
				
				// Empeche de retourner en arriere si on ne mange pas un pion
				if(f_manger == false && isValid == true) {
					if(Board.this.posCheck.checker.getType() == CheckerType.CHECKER_JOUEUR1
							&& oldcy > Board.this.posCheck.cy) {
						
						JOptionPane.showMessageDialog(null, "Impossible de retourner en arriere");
						isValid = false;
					}
					
					if(Board.this.posCheck.checker.getType() == CheckerType.CHECKER_JOUEUR2
							&& oldcy < Board.this.posCheck.cy) 
					{
						JOptionPane.showMessageDialog(null, "Impossible de retourner en arriere");
						isValid = false;
					}
				}

				// Repositionnement si la position n'est pas valide
				if(isValid == false)
				{
					Board.this.posCheck.cx = oldcx;
					Board.this.posCheck.cy = oldcy;
				}
				else 
				{
					tour++;
					set_score(); // Actualise le score
					
					revalidate();
					repaint();
					
					SocketManager.send(Checkers.joueur_nb + "-OX:" + oldcx + "-OY:" + oldcy 
							+ "-NX:" + Board.this.posCheck.cx + "-NY:" + Board.this.posCheck.cy);
					
					posCheck = null;

					SocketManager.attenteAdv = true;
				}
					
							
				posCheck = null;
				repaint();
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent me) {
				if (inDrag) {
					posCheck.cx = me.getX() - deltax;
					posCheck.cy = me.getY() - deltay;
					repaint();
				}
			}
		});
		
	} 

	public void add(Checker checker, int row, int col) {
		if (row < 1 || row > 10)
			throw new IllegalArgumentException("row out of range: " + row);
		if (col < 1 || col > 10)
			throw new IllegalArgumentException("col out of range: " + col);
		PosCheck posCheck = new PosCheck();
		posCheck.checker = checker;
		posCheck.cx = (col - 1) * SQUAREDIM + SQUAREDIM / 2;
		posCheck.cy = (row - 1) * SQUAREDIM + SQUAREDIM / 2;
		for (PosCheck _posCheck: posChecks)
			if (posCheck.cx == _posCheck.cx && posCheck.cy == _posCheck.cy)
				System.out.println("L'emplacement (" + row + "," + col + ") est occupe");
		posChecks.add(posCheck);
	}
	
	// Effectue le déplacement de l'adversaire
	public void moveCheckersAdv()
	{
		String msg = SocketManager.wait_recv();
		ParserRecv parseur = new ParserRecv(msg);
		
		// Ne dois pas prendre en compte son propre message
		if(parseur.getRepnumjoueur() == Checkers.joueur_nb)
		{
			System.out.println("Attente du message de l'adversaire");
			moveCheckersAdv();
			return;
		}
		
		System.out.println("parseur, getnumjoueur : " + parseur.getRepnumjoueur());
		
		int eatx = 0; 
		int eaty = 0;
		
		for (PosCheck posCheck: posChecks)
			if (posCheck.cx == parseur.getOldx() && posCheck.cy == parseur.getOldy())
			{				
				posCheck.cx = parseur.getNewx();
				posCheck.cy = parseur.getNewy();
				
				
				eatx = (parseur.getNewx() + parseur.getOldx()) / 2;
				eaty = (parseur.getNewy() + parseur.getOldy()) / 2;

				System.out.println("oldx : " + parseur.getOldx() + " // newx : " + parseur.getNewx() +" // eatx : " + eatx);
				System.out.println("oldy : " + parseur.getOldy() + " // newy : " + parseur.getNewy() +" // eaty : " + eaty);

				break;
			}			
		
		PosCheck eatCheck = null;
		
		// Vérifie si un pion doit être mangé et le récupère pour suppression
		if(eatx != 0 && eaty != 0)
			for (PosCheck posCheck: posChecks)
				if (posCheck.cx == eatx && posCheck.cy == eaty)
				{
					eatCheck = posCheck;
				}

		// Suppression du pion mangé
		if(eatCheck != null)
			posChecks.remove(eatCheck); 

		
		tour++;
		checkers.myTurn = true;
		set_score(); // Actualise le score

		revalidate();
		repaint();
		return;
		
	} // Fin moveCheckersAdv
	
	// Permet de savoir si l'emplacement est libre
	private boolean posLibre(int posx, int posy){
		
		boolean isLibre = true;
		
		// Retourne false si le pion sort du cadre
		if(posx < 0 || posy < 0
				|| posx >= dimPrefSize.getHeight()
				|| posy >= dimPrefSize.getWidth())
			return false;
		
		for (PosCheck posCheck: posChecks)
		{				
			if (posCheck != Board.this.posCheck && 
					posCheck.cx == posx &&
					posCheck.cy == posy)
			{
				isLibre = false;
				break;
			}
		}
			
		return isLibre;
	}
	
	// Retourne le score 
	private void set_score() {
		int nbCheckhaut = 0;
		int nbCheckbas = 0;
		
		for (PosCheck posCheck: posChecks)
		{	
			if(posCheck.checker.getType() == CheckerType.CHECKER_JOUEUR1) {
				nbCheckhaut = nbCheckhaut + 1;
			}
			else {
				nbCheckbas = nbCheckbas + 1;
			}
		}
		
		checkers.set_lbscore(nbCheckhaut, nbCheckbas);
	}

	@Override
	public Dimension getPreferredSize() {
		return dimPrefSize;
	}

	@Override
	protected void paintComponent(Graphics g) {
		paintCheckerBoard(g);
		for (PosCheck posCheck: posChecks)
			if (posCheck != Board.this.posCheck)
				posCheck.checker.draw(g, posCheck.cx, posCheck.cy);

		if (posCheck != null)
			posCheck.checker.draw(g, posCheck.cx, posCheck.cy);
	}

	private void paintCheckerBoard(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (int row = 0; row < 10; row++) {
			g.setColor(((row & 1) != 0) ? Color.WHITE : Color.LIGHT_GRAY);
			for (int col = 0; col < 10; col++)
			{
				g.fillRect(col * SQUAREDIM, row * SQUAREDIM, SQUAREDIM, SQUAREDIM);
				g.setColor((g.getColor() == Color.LIGHT_GRAY) ? Color.WHITE : Color.LIGHT_GRAY);
			}
		}
	}
}