package com.scs.aresdogfighter.ai;

import java.util.Iterator;

import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class CivilianShipAI extends AbstractShipAI {
	
	private boolean been_shot = false;

	public CivilianShipAI(ManualShipControlModule _game, Ship1 _obj) {
		super(_game, _obj);
	}


	@Override
	public void processMessage(AIMessage msg) {
		Ship1 shooter = msg.shooter;
		Ship1 target = msg.target;
		float amt = msg.amt;

		if (msg.type == AIMessage.Type.SHOT_BY) {
			been_shot = true;
			this.runAway(shooter);
		}
	}

	@Override
	protected void selectEnemy(Iterator<Ship1> it) {
		// Do nothing?
	}


	@Override
	public void hail() {
		if (been_shot) {
			module.log("We're under attack!  Can you help us?");
		} else {
			module.log("Hello");
		}
	}


}
