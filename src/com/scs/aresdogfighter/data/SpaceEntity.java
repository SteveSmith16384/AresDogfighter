package com.scs.aresdogfighter.data;

import com.jme3.math.Vector3f;

public class SpaceEntity {

	public enum Type {
		FRIENDLY_PLANET,
		DESERT_PLANET,
		SUN,
		MOON,
		PLAYERS_SHIP, 
		ENEMY_FIGHTER,
		FRIENDLY_FIGHTER,
		POLICE_SHIP,
		CIVILIAN,
		SPACESTATION, ASTEROID, CARGO, WORMHOLE,
		DEBRIS,
		SCENERY, // Doesn't get stored
		ROBOT
	}
	
	public Vector3f pos = new Vector3f();
	public Type type;

	public SpaceEntity(Type _type, Vector3f v) {
		this (_type, v.x, v.y, v.z);
	}
	

	public SpaceEntity(Type _type, float x, float y, float z) {
		super();
		
		type = _type;
		pos = new Vector3f(x, y, z);
	}
	
	
	@Override
	public String toString() {
		return "SpaceEntity:" + type.name();
	}

}
