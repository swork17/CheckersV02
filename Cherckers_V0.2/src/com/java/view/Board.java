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

@SuppressWarnings("serial")

public class Board extends JComponent {

	private final static int SQUAREDIM = (int) (Checker.getDimension() * 1.25);
	private final int BOARDDIM = 10 * SQUAREDIM;
	private Dimension dimPrefSize;
	private boolean inDrag = false;
	private int deltax, deltay;
	private PosCheck posCheck;
	private int oldcx, oldcy;
	private List<PosCheck> posChecks;

	public Board() {
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
				
				if(isValid == true)
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

								if(isOccupe(newx, newy) == true){
									System.out.println("Impossible de manger le pion, l'emplacement est occupe !"); 
									isValid = false;
									break;
								}
								
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

				// Repositionnement si la position n'est pas valide
				if(isValid == false)
				{
					Board.this.posCheck.cx = oldcx;
					Board.this.posCheck.cy = oldcy;
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
	
	// Permet de savoir si l'emplacement est occupe
	private boolean isOccupe(int posx, int posy){
		
		boolean isOccupe = false;
		
		for (PosCheck posCheck: posChecks)
		{				
			if (posCheck != Board.this.posCheck && 
					posCheck.cx == posx &&
					posCheck.cy == posy)
			{
				isOccupe = true;
				break;
			}
		}
			
		return isOccupe;
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