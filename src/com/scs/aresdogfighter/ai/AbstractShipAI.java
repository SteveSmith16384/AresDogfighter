package com.scs.aresdogfighter.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.data.CaptainsOrder;
import com.scs.aresdogfighter.data.CaptainsOrder.OrderType;
import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public abstract class AbstractShipAI implements IProcessable {

	private static final int MAX_MSGS = 10;

	protected ManualShipControlModule module;
	protected Ship1 ship;
	private float next_enemy_check = 10;
	protected GameObject current_target; // do not set directly, use selectTarget()
	private List<AIMessage> msgs = new ArrayList<AIMessage>();

	public AbstractShipAI(ManualShipControlModule _game, Ship1 _obj) {
		super();

		module = _game;
		ship = _obj;
	}


	public void damagedBy2(Ship1 shooter, float amt) {
		if (msgs.size() < MAX_MSGS) {
			this.msgs.add(new AIMessage(AIMessage.Type.SHOT_BY, shooter, amt));
		}
	}


	public void seenShooting2(Ship1 shooter, Ship1 target, float amt) {
		if (msgs.size() < MAX_MSGS) {
			this.msgs.add(new AIMessage(AIMessage.Type.SEEN_SHOOTING, shooter, target, amt));
		}
	}


	public abstract void processMessage(AIMessage msg);

	protected Ship1 getShip() {
		return ship;
	}


	@Override
	public void process(float tpf) { // todo - check currenttarget and captainsorder target match
		if (this.msgs.size() > 0) {
			AIMessage msg = this.msgs.remove(0);
			this.processMessage(msg);
		}
		// Look for enemy
		if (current_target == null) {
			next_enemy_check -= tpf;
			if (next_enemy_check < 0) {
				next_enemy_check = Statics.ENEMY_CHECK_INT;
				List<Ship1> list = module.getShips(ship.getNode().getWorldTranslation());
				Iterator<Ship1> it = list.iterator();
				/*if (Statics.AUTO_TARGET_PLAYER) {
				while (it.hasNext()) {
					Ship1 enemy = it.next();
					if (enemy == module.players_ship) {
						if (this.ship.canSee(module.players_ship)) {
							this.selectTarget(enemy, Section.ENGINES);
						} else {
							module.debug("Ship can't see player");
						}
					}
				}

				} else {*/
					this.selectEnemy(it);
				//}
			}
		} else {
			// Check if enemy destroyed
			if (this.current_target.isAlive() == false) {
				current_target = null;
				this.getShip().order.setOrder(CaptainsOrder.OrderType.HALT, null);
			} else {
				//this.selectTarget(enemy);
			}
		}
		if (current_target == null && ship.order.getType() == OrderType.ATTACK) {
			ship.order.setOrder(OrderType.HALT);
		}
	}


	protected abstract void selectEnemy(Iterator<Ship1> it);

	protected void selectTarget(GameObject enemy) {
		current_target = enemy;
		this.getShip().order.setOrder(CaptainsOrder.OrderType.ATTACK, current_target);
		if (Statics.DEBUG_AI) {
			module.log(this.ship.getName() + ": Enemy target " + current_target.getName() + " chosen");
		}

	}


	protected void runAway(Ship1 enemy) {
		if (current_target != enemy || this.getShip().order.getType() != CaptainsOrder.OrderType.EVADE) {
			current_target = enemy;
			/*if (Statics.DEBUG_AI) {
				module.log(this.ship.getName() + ": Evading " + curr_enemy.getName());
			}*/
			this.getShip().order.setOrder(CaptainsOrder.OrderType.EVADE, current_target);
		}

	}


	public abstract void hail();
	
}
