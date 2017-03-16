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
    
      g.setColor(checkerType == CheckerType.BLACK_REGULAR ? Color.DARK_GRAY : 
                 Color.PINK);
      
      g.fillRoundRect(x, y, DIMENSION, DIMENSION, 45, 45);
      g.setColor(Color.WHITE);
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
}