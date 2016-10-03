package com.scs.aresdogfighter.ai;

import java.util.Iterator;

import com.scs.aresdogfighter.data.Faction;
import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class FriendlyShipAI extends AbstractShipAI {

	public FriendlyShipAI(ManualShipControlModule _game, Ship1 _obj) {
		super(_game, _obj);
	}


	@Override
	public void processMessage(AIMessage msg) {
		Ship1 shooter = msg.shooter;
		Ship1 target = msg.target;
		float amt = msg.amt;

		if (msg.type == AIMessage.Type.SHOT_BY) {
			//module.game.game_data.factiondata.faction_data[Faction.NIBLYS].standing[shooter.faction]--;
			//if (shooter.shipdata.getThreat() <= ship.shipdata.getThreat()) {
				if (this.current_target == null) {
					this.selectTarget(shooter);
				}
			/*} else {
				this.runAway(shooter);
			}*/

		} else if (msg.type == AIMessage.Type.SEEN_SHOOTING) {
			/*if (target.faction == Faction.QUAZIARGS) {
				module.game.game_data.factiondata.faction_data[Faction.NIBLYS].standing[shooter.faction] += amt;
			}*/

			if (this.current_target == null) {
				if (target.faction == this.getShip().faction) {
					this.selectTarget(shooter);
				}
			}	
		}
	}


	@Override
	protected void selectEnemy(Iterator<Ship1> it) {
		while (it.hasNext()) {
			Ship1 enemy = it.next();
			if (enemy.faction != ship.faction && enemy.faction != Faction.POLICE) { // Don't target ourselves
				//if (this.module.game.game_data.factiondata.faction_data[Faction.NIBLYS].standing[enemy.faction] < 0) {
					this.selectTarget(enemy);
				//}
			}
		}

	}


	@Override
	public void hail() {
		module.log("Hello from " + ship.name);
	}


}
