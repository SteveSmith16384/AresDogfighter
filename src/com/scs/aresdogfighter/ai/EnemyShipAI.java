package com.scs.aresdogfighter.ai;

import java.util.Iterator;

import com.scs.aresdogfighter.data.Faction;
import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class EnemyShipAI extends AbstractShipAI {

	public EnemyShipAI(ManualShipControlModule _game, Ship1 _obj) {
		super(_game, _obj);
		
		this.selectTarget(_game.getSpaceStation());
	}


	@Override
	public void processMessage(AIMessage msg) {
		Ship1 shooter = msg.shooter;
		Ship1 target = msg.target;
		float amt = msg.amt;

		if (msg.type == AIMessage.Type.SHOT_BY) {
			//if (shooter.shipdata.getThreat() <= ship.shipdata.getThreat()) {
			//module.game.game_data.factiondata.faction_data[Faction.QUAZIARGS].standing[shooter.faction]--;
			if (this.current_target == null) {
				this.selectTarget(shooter);
			}
			/*} else {
			this.runAway(shooter);
		}*/

		} else if (msg.type == AIMessage.Type.SEEN_SHOOTING) {
			/*if (this.curr_enemy == null) {
				if (target.faction == this.getShip().faction) {
					this.selectTarget(shooter, Section.SHIELD);
				}
			}*/
		}
	}


	/*
	 * This is only run if the ship currently has no enemy.
	 * 
	 * @see com.scs.aresdogfighter.ai.AbstractShipAI#selectEnemy(java.util.Iterator)
	 */
	@Override
	protected void selectEnemy(Iterator<Ship1> it) {
		Ship1 best_enemy = null;
		int enemy_level = 1;
		while (it.hasNext()) {
			Ship1 enemy = it.next();
			if (enemy.faction != ship.faction && enemy.faction != Faction.POLICE) { // Don't target ourselves
				/*int standing = enemy.getStanding(this.ship.faction); 
				if (standing < enemy_level) {
					best_enemy = enemy;
					enemy_level = standing;
				}*/
			}
		}
		if (best_enemy != null) {
			this.selectTarget(best_enemy);

		}
	}


	@Override
	public void hail() {
		module.log("Your race will be elimiated.");
	}


}
