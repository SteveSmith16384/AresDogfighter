package com.scs.aresdogfighter.events;

import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class EnemyShipArrivedEvent extends AbstractEvent {

	public EnemyShipArrivedEvent(ManualShipControlModule module) {
		super(module);
	}


	@Override
	public void setupEvent() {
		if (module.num_enemies < Statics.MAX_ENEMY_FIGHTERS) { // don't create too many!
			//todo - re-add  module.addEntity(SpaceEntity.Type.ENEMY_FIGHTER, module.spacestation.getNode().getWorldTranslation().mult(-1)); // Start opposite sun
			module.addEntity(SpaceEntity.Type.ENEMY_FIGHTER, 50, 50, 50);
		}
	}


	@Override
	public String getName() {
		return "Enemy Ship Arrived";
	}

}
