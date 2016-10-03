package com.scs.aresdogfighter.data;

import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class CaptainsOrder {

	private ManualShipControlModule module;
	private OrderType type = OrderType.HALT;
	private OrderType prev_type;
	public GameObject target; // The entity to attack/fly towards/avoid (not neccesarily the same as our overall target)
	private ShipData shipdata;

	public enum OrderType {
		ATTACK, MOVE_TOWARDS, HALT, EVADE, 
		//DOCK, 
		//MANUAL_CONTROL, 
		TURN_UNTIL_TARGET_BEHIND, EVASIVE_MANOUVRE, FLY_AWAY,
		//AVOID_CRASH_; // For avoiding planets/capital ships
	}


/*	public enum Section {
		ENGINES, WEAPONS, SHIELD;// CARGO, POWER
	}
*/

	public CaptainsOrder(ManualShipControlModule _module, ShipData _shipdata) {
		super();

		module = _module;
		shipdata = _shipdata;
	}


	public OrderType getType() {
		return type;
	}


	public void setType(OrderType t) {
		if (t != this.type) {
			this.prev_type = type;
			type = t;
			if (Statics.DEBUG_AI) {
				module.debug(shipdata + " OrderType changed to " + t);
			}
		}
	}
	
	
	public void restoreType() {
		setType(this.prev_type);
	}


	public void setOrder(OrderType t) {
		this.setOrder(t, null);
	}


	public void setOrder(OrderType t, GameObject targ) {
		this.type = t;
		this.target = targ;
		//this.section = section;

		if (Statics.DEBUG_AI) {
			module.debug("Order changed to " + this);
		}
	}


	public String getStringRepresentation() {
		StringBuilder str = new StringBuilder("Current Order: ");
		switch (type) {
		case ATTACK:
			str.append("Attack " + this.target.getName());
			break;
		case TURN_UNTIL_TARGET_BEHIND:
			str.append("TUTB " + this.target.getName());
			break;
		case EVASIVE_MANOUVRE:
			str.append("Manouvre");
			break;
		/*case DOCK:
			str.append("Dock at " + this.target.getName());
			break;*/
		case EVADE:
			str.append("Evade " + this.target.getName());
			break;
		case HALT:
			str.append("Halt");
			break;
		case MOVE_TOWARDS:
			str.append("Move towards " + this.target.getName());
			break;
		case FLY_AWAY:
			str.append("Fly away");
			break;
		/*case MANUAL_CONTROL:
			str.append("Manual Control");
			break;*/
		default:
			//throw new RuntimeException("Unknown type:" + type);
			str.append("Unknown: " + type.name());

		}
		return str.toString();
	}


	@Override
	public String toString() {
		return type + " (" + target + ")";
	}


}
