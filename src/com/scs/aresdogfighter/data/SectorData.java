package com.scs.aresdogfighter.data;

import java.util.ArrayList;
import java.util.List;

import ssmith.lang.NumberFunctions;

import com.jme3.math.Vector3f;

public class SectorData {
	
	private static final int SIZE = 4;
	private static final float GAP = 30f;
	private SpaceEntity.Type space[][][];
	
	public List<SpaceEntity> entities = new ArrayList<SpaceEntity>();;
	public int x, y;
	//public int security_level = NumberFunctions.rnd(0, 3);
	public String name;
	//public int num_nibbly_fighters = 1; //NumberFunctions.rnd(1, 3);
	//public int num_quaz_fighters = 1; //NumberFunctions.rnd(0, 2);
	//public int num_police = 0;
	//public int num_others = 0; //NumberFunctions.rnd(1, 3);
	//public int owned_by = NumberFunctions.rnd(2, 4);
	//public int num_debris = NumberFunctions.rnd(2, 8);

	
	public SectorData(String _name, int _x, int _y) {
		super();

		name = _name;
		x = _x;
		y = _y;
	}

	
	public String getName() {
		return "Sector_" + x + "_" + y;
	}
	
	
	public void generateKrakatoaEntities() {
		entities.add(new SpaceEntity(SpaceEntity.Type.SUN, 0, 0, 0));
		entities.add(new SpaceEntity(SpaceEntity.Type.FRIENDLY_PLANET, 250, 0, 0));
		entities.add(new SpaceEntity(SpaceEntity.Type.MOON, 270, 0, 0));
		entities.add(new SpaceEntity(SpaceEntity.Type.SPACESTATION, 310, 0, 0));
		entities.add(new SpaceEntity(SpaceEntity.Type.PLAYERS_SHIP, 350, 0, 0));
		
		// Asteroids
		for (int i=0 ; i<10 ; i++) {
			entities.add(new SpaceEntity(SpaceEntity.Type.ASTEROID, GetRandomVec(400)));
		}

	}
	
	
	private static Vector3f GetRandomVec(float max) {
		return new Vector3f(NumberFunctions.rndFloat(-max, max), NumberFunctions.rndFloat(-max, max), NumberFunctions.rndFloat(-max, max));
	}
	
/*	public void generateDefaultSectorEntities() {
		space = new SpaceEntity.Type[SIZE][SIZE][SIZE];
		setCell(SpaceEntity.Type.PLAYERS_SHIP);
		setCell(SpaceEntity.Type.FRIENDLY_PLANET);
		//setCell(SpaceEntity.Type.SPACESTATION);
		/*for (int i = 0 ; i<num_quaz_fighters ; i++) {
			setCell(SpaceEntity.Type.ENEMY_FIGHTER);
		}
		for (int i = 0 ; i<num_nibbly_fighters ; i++) {
			setCell(SpaceEntity.Type.FRIENDLY_FIGHTER);
		}
		for (int i = 0 ; i<num_police ; i++) {
			setCell(SpaceEntity.Type.POLICE_SHIP);
		}
		for (int i = 0 ; i<num_others ; i++) {
			setCell(SpaceEntity.Type.PIRATE_SHIP);
		}
		for (int i = 0 ; i<num_debris ; i++) {
			setCell(SpaceEntity.Type.ASTEROID);
		}
		*/
/*
		for (int z=0 ; z<SIZE ; z++) {
			for (int y=0 ; y<SIZE ; y++) {
				for (int x=0 ; x<SIZE ; x++) {
					if (space[x][y][z] != null) {
						entities.add(new SpaceEntity(space[x][y][z], x*GAP, y*GAP, z*GAP));
					}
				}
			}
		}
	}
	
	
	private void setCell(SpaceEntity.Type e) {
		int x = getNum();
		int y = getNum();
		int z = getNum();
		while (space[x][y][z] != null) {
			x = getNum();
			y = getNum();
			z = getNum();
		}
		space[x][y][z] = e;
	}
	
	
	private int getNum() {
		return NumberFunctions.rnd(0, SIZE-1);
	}*/
}
