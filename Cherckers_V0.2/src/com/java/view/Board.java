package com.java.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")

public class Board extends JComponent {

	public static int tour = 2;
	public static boolean f_joueur1 = true;

	private final static int SQUAREDIM = (int) (Checker.getDimension() * 1.25);
	private final int BOARDDIM = 10 * SQUAREDIM;
	private Dimension dimPrefSize;
	private boolean inDrag = false;
	private int deltax, deltay;
	private int oldcx, oldcy;
	private PosCheck posCheck;
	private List<PosCheck> posChecks;

	public Board(Checkers checkers) {
		posChecks = new ArrayList<>();
		dimPrefSize = new Dimension(BOARDDIM, BOARDDIM);

		addMouseListener(new MouseAdapter()
		{			
			@Override
			public void mousePressed(MouseEvent me) {
				
				int x = me.getX();
				int y = me.getY();
				
				for (PosCheck posCheck: posChecks)
					if (Checker.contains(x, y, posCheck.cx, 
							posCheck.cy))
					{
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
					System.out.println("Le pion n'a pas boug�, pas de changement de joueur !"); 
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
						
						System.out.println("Impossible de retourner en arri�re !"); 
						JOptionPane.showMessageDialog(null, "Impossible de retourner en arriere");
						isValid = false;
					}
					
					if(Board.this.posCheck.checker.getType() == CheckerType.CHECKER_JOUEUR2
							&& oldcy < Board.this.posCheck.cy) {
						System.out.println("Impossible de retourner en arri�re !");
						JOptionPane.showMessageDialog(null, "Impossible de retourner en arriere");
						isValid = false;
					}
				}
				
				// Repositionnement si la position n'est pas valide
				if(isValid == false)
				{
					Board.this.posCheck.cx = oldcx;
					Board.this.posCheck.cy = oldcy;
				} else {
					tour++;
					set_score(checkers); // Actualise le score
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
				throw new AlreadyOccupiedException("L'emplacement (" + row + "," +
						col + ") est occupe");
		posChecks.add(posCheck);
	}
	
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
	private void set_score(Checkers checkers) {
		int nbCheckhaut = 0;
		int nbCheckbas = 0;
		
		for (PosCheck posCheck: posChecks)
		{	
			if(posCheck.checker.getType() == CheckerType.CHECKER_JOUEUR1)
				nbCheckhaut = nbCheckhaut + 1;
			else 
				nbCheckbas = nbCheckbas + 1;
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

	private class PosCheck {
		public Checker checker;
		public int cx;
		public int cy;
	}
}