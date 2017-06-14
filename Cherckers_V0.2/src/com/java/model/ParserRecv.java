 package com.java.model;

public class ParserRecv {
	private static final String TAG = "PARSER RECV";
	
	private int rep_numjoueur;
	private int oldx;
	private int oldy;
	private int newx;
	private int newy;
	
	public ParserRecv(String recv)
	{
		System.out.println("Message : " + recv);
		if(recv.contains("-") == false)
			return;
		
		String[] parts = recv.split("-");

		rep_numjoueur = Integer.parseInt(parts[0]);
		 if( (rep_numjoueur % 2) == 0) 
			 rep_numjoueur = 2;
		 else
			 rep_numjoueur = 1;
		
		oldx = Integer.parseInt(parts[1].substring(parts[1].indexOf(":") + 1, parts[1].length()));
		oldy = Integer.parseInt(parts[2].substring(parts[2].indexOf(":") + 1, parts[2].length()));
		newx = Integer.parseInt(parts[3].substring(parts[3].indexOf(":") + 1, parts[3].length()));
		newy = Integer.parseInt(parts[4].substring(parts[4].indexOf(":") + 1, parts[4].length()));

	}
	
	public int getRepnumjoueur() {
		return rep_numjoueur;
	}
	
	public int getOldx() {
		return oldx;
	}
	
	public int getOldy() {
		return oldy;
	}
	
	public int getNewx() {
		return newx;
	}
	
	public int getNewy() {
		return newy;
	}
	
}
