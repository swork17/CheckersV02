package com.java.view;

import java.awt.Color;
import java.awt.Graphics;

public final class Checker
{
   private final static int DIMENSION = 50;

   private CheckerType checkerType;

   public Checker(CheckerType checkerType)
   { 
      this.checkerType = checkerType;
   }

   public void draw(Graphics g, int cx, int cy)
   {
      int x = cx - DIMENSION / 2;
      int y = cy - DIMENSION / 2;
    
      g.setColor(checkerType == CheckerType.CHECKER_JOUEUR1 ? Color.DARK_GRAY : 
                 Color.PINK);
      
      g.fillRoundRect(x, y, DIMENSION, DIMENSION, 45, 45);
      
      if( (Checkers.joueur_nb == 1 && checkerType == CheckerType.CHECKER_JOUEUR1)
    	  || (Checkers.joueur_nb == 2 && checkerType == CheckerType.CHECKER_JOUEUR2))
    	  g.setColor(Color.GRAY); // Pion de l'adversaire
      else
    	  g.setColor(Color.WHITE); // Pion du joueur 
      
      g.drawRoundRect(x, y, DIMENSION, DIMENSION, 45, 45);
   }
   
   public static boolean contains(int x, int y, int cx, int cy)
   {
      return (cx - x) * (cx - x) + (cy - y) * (cy - y) < DIMENSION / 2 * 
             DIMENSION / 2;
   }
   
   public static int getDimension()
   {
      return DIMENSION;
   }
   
   public CheckerType getType() {
	   return this.checkerType;
   }
   
   
   public Checker getChecker() {
	   return this;
   }
}