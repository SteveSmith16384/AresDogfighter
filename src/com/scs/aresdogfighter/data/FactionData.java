package com.scs.aresdogfighter.data;
/*
public class FactionData {

	public Faction[] faction_data = new Faction[Faction.MAX_FACTION+1];

	public FactionData() {
		faction_data[Faction.PLAYER] = new Faction();//"Players Faction");
		faction_data[Faction.QUAZIARGS] = new Faction();//"Quaziargs");
		faction_data[Faction.NIBLYS] = new Faction();//"Niblypiblys");

		// Set up standings
		// Quaziargs hate everyone
		faction_data[Faction.QUAZIARGS].standing[Faction.NIBLYS] = -1;
		faction_data[Faction.QUAZIARGS].standing[Faction.PLAYER] = -1;
		faction_data[Faction.QUAZIARGS].standing[Faction.CIVILIAN] = -4; // Go for civilians before others
		
		// Niblys only hate quaziargs
		faction_data[Faction.NIBLYS].standing[Faction.QUAZIARGS] = -1;
		faction_data[Faction.NIBLYS].standing[Faction.PLAYER] = 1;
		
	}

/*
	public Faction.Standing getFactionStanding(int our_faction, int their_faction) {
		try {
			return faction_data[our_faction].standing[their_faction];
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}


	public void getFactionStanding(int our_faction, int their_faction, Faction.Standing standing) {
		faction_data[our_faction].standing[their_faction] = standing;
	}
*/
//}
