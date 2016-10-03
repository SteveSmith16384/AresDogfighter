package com.scs.aresdogfighter.events;

import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class DistressCallEvent extends AbstractEvent {

	public DistressCallEvent(ManualShipControlModule module) {
		super(module);
	}

	
	@Override
	public void setupEvent() {
		module.game.game_data.current_sector.entities.add(new SpaceEntity(SpaceEntity.Type.CIVILIAN, 0, 0, 0)); // Todo - rnd location
		module.game.game_data.current_sector.entities.add(new SpaceEntity(SpaceEntity.Type.ENEMY_FIGHTER, 200, 200, 200)); // Todo - rnd location
		module.game.game_data.current_sector.entities.add(new SpaceEntity(SpaceEntity.Type.ENEMY_FIGHTER, -200, -200, -200)); // Todo - rnd location
	}


	@Override
	public void processLongInterval() {
		// todo - end event when ships destroyed
	}


	@Override
	public String getName() {
		return "Distress Call";
	}

}
