package com.scs.aresdogfighter.events;

import ssmith.lang.NumberFunctions;

import com.jme3.math.Vector3f;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class MeteorStormEvent extends AbstractEvent {

	private static final int NUM_METEORS = 10;

	private int meteors_left = NUM_METEORS;

	public MeteorStormEvent(ManualShipControlModule module) {
		super(module);
	}


	@Override
	public void setupEvent() {
		if (module.num_asteroids < Statics.MAX_ASTEROIDS) { // don't create too many!
		while (meteors_left > 0) {
			meteors_left--;
			Vector3f vp = module.players_avatar.getNode().getWorldTranslation();
			float x = NumberFunctions.rndFloat(vp.x-200, vp.x+200);
			float y = NumberFunctions.rndFloat(vp.y-200, vp.y+200);
			float z = NumberFunctions.rndFloat(vp.z-200, vp.z+200);
			module.addEntity(SpaceEntity.Type.ASTEROID, x, y, z);
		}
		}
	}


	@Override
	public String getName() {
		return "Meteor Storm";
	}

}
