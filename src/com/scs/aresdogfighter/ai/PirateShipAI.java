package com.scs.aresdogfighter.ai;

import java.util.Iterator;

import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class PirateShipAI extends AbstractShipAI {
/*
	private Objective objective; // todo - use this

	private enum Objective {
		WANDER, PIRATE, TRADE, BOUNTY_HUNTER;
	}

*/
	public PirateShipAI(ManualShipControlModule _game, Ship1 _obj) {
		super(_game, _obj);
	}


	@Override
	public void processMessage(AIMessage msg) {
		Ship1 shooter = msg.shooter;
		Ship1 target = msg.target;
		float amt = msg.amt;

		if (msg.type == AIMessage.Type.SHOT_BY) {
			//if (shooter.shipdata.getThreat() <= ship.shipdata.getThreat()) {
				if (this.curr_enemy == null) {
					this.selectTarget(shooter);
				}
			/*} else {
				this.runAway(shooter);
			}*/

		}
	}

	
	@Override
	protected void selectEnemy(Iterator<Ship1> it) {
		// Do nothing?
	}


	@Override
	public void hail() {
		module.log("Hand over your cargo");
	}


}
