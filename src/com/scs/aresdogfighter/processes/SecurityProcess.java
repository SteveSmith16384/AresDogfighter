package com.scs.aresdogfighter.processes;
/*
import ssmith.util.GameLoopInterval;

import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.data.Faction;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class SecurityProcess implements IProcessable {

	private ManualShipControlModule module;
	private GameLoopInterval check_int = new GameLoopInterval(10);
	private float police_required;

	public SecurityProcess(ManualShipControlModule mod) {
		super();

		module = mod;
	}


	@Override
	public void process(float tpf) {
		if (check_int.hasHit(tpf)) {
			if (police_required > 0) {
				police_required -= 0.1f;
			}
			int num_police = module.getNumPolice();
			if (num_police < this.police_required) {
				module.launchNPCShip(SpaceEntity.Type.POLICE_SHIP);
			}
		}

	}


	public void shotFired(Ship1 shooter) {
		//police_required += 0.1f;
	}


	public void shipDestroyed(Ship1 ship, Ship1 shooter) {
		if (shooter.faction != Faction.POLICE) { 
			police_required += 1;//module.game.game_data.getCurrentSector().security_level;
			if (ship.faction == Faction.POLICE) {
				police_required += 1;//module.game.game_data.getCurrentSector().security_level; // inc if police destroyed
			}
			if (Statics.DEBUG) {
				module.debug(police_required + " police required");
			}
		}
	}

}
*/