package com.scs.aresdogfighter.data;

public class Faction {

	public static final int NONE = -1;
	public static final int PLAYER_FRIENDLY = 0;
	public static final int POLICE = 1;
	public static final int ENEMY_SHIP = 2;
	//public static final int NIBLYS = 3;
	public static final int CIVILIAN = 4;
	//public static final int PIRATE = 5;
	public static final int MAX_FACTION = 4;
	//, FLHURGS, MALMYDONS, AI;
	
	//public String name;
	public int standing[] = new int[Faction.MAX_FACTION+1]; // Standing against other factions.  The higher, the more friendly.
	
	public Faction() {//String _name) {
		super();
		
		//name = _name;
		
	}
	
	
	public static String GetName( int i) {
		switch (i) {
		case ENEMY_SHIP: return "Enemy"; 
		case PLAYER_FRIENDLY: return "Wingman"; 
		case CIVILIAN: return "Civilian"; 
		//case PIRATE: return "Pirate"; 
		case NONE: return "None"; 
		default: throw new RuntimeException("Unknown faction:" + i);
		}
	}
	
}
