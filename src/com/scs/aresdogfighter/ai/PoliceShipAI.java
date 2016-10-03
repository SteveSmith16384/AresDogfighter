package com.scs.aresdogfighter.ai;

import java.util.Iterator;

import com.scs.aresdogfighter.data.CaptainsOrder.OrderType;
import com.scs.aresdogfighter.data.Faction;
import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

import ssmith.util.GameLoopInterval;

public class PoliceShipAI extends AbstractShipAI {

	private GameLoopInterval hang_around_int = new GameLoopInterval(10);

	public PoliceShipAI(ManualShipControlModule _game, Ship1 _obj) {
		super(_game, _obj);
	}


	@Override
	public void process(float tpf) {
		super.process(tpf);
		if (this.current_target != null) {
			hang_around_int.restart();
		} else if (this.hang_around_int.hasHit(tpf)) {
			this.ship.order.setOrder(OrderType.FLY_AWAY);
		}
	}


	@Override
	public void processMessage(AIMessage msg) {
		Ship1 shooter = msg.shooter;
		Ship1 target = msg.target;
		float amt = msg.amt;

		if (msg.type == AIMessage.Type.SHOT_BY) {
			if (shooter.faction == Faction.POLICE) {
				throw new RuntimeException("Police shooting each other");
			}
			shooter.shipdata.bounty += 100;


		} else if (msg.type == AIMessage.Type.SEEN_SHOOTING) {
			if (shooter.faction != Faction.POLICE) {
				if (target.shipdata.bounty == 0) { // Allowed to shoot criminals
					if (target.faction == Faction.POLICE) { // higher if police ship
						shooter.shipdata.bounty += 50;
					} else {
						shooter.shipdata.bounty += 10;
					}
				}
			}
		}
	}

	@Override
	protected void selectEnemy(Iterator<Ship1> it) {
		while (it.hasNext()) {
			Ship1 enemy = it.next();
			if (enemy != this.ship && enemy.faction != Faction.POLICE) { // Don't target ourselves
				if (enemy.shipdata.bounty > 0) {
					/*curr_enemy = enemy;
					module.log(this.ship.getName() + ": Enemy " + curr_enemy.getName() + " chosen");
					this.getShip().order.setOrder(CaptainsOrder.Type.ATTACK, curr_enemy, Section.ENGINES);*/
					super.selectTarget(enemy);
				}
			}
		}

	}


	@Override
	public void hail() {
		module.log("The is the Galacticops.  Watch your step.  ");
	}


}
