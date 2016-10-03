package com.scs.aresdogfighter.data;

import java.io.Serializable;

public class GameData implements Serializable {
	
	public static final long serialVersionUID = 1l;
	
	/*public FactionData factiondata = new FactionData();
	public transient String mission_text = "";
	public AbstractMission current_mission;
	public SectorData start_sector;
*/
	public SectorData current_sector;

	public GameData() {
		super();
		
		/*universe = new UniverseData();
		sector_x = universe.start_sec.x;
		sector_y = universe.start_sec.y;*/
		
		//start_sector = new SectorData("Origin", 0, 0);
		//start_sector.generateDefaultSectorEntities();
		
		//current_sector = start_sector;
	}
	
	/*
	public void setCurrentSector(int x, int y) {
		sector_x = x;
		sector_y = y;
	}
	*/

	public SectorData getCurrentSector() {
		if (current_sector == null) {
			current_sector = new SectorData("Sector", 0, 0);
		}
		return current_sector;
	}
	
	
/*	public List<SpaceEntity> getObjects() {
		return this.universe.getSectorData(sector_x, sector_y);
	}
	*/

}
