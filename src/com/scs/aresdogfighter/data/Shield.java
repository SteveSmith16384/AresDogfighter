package com.scs.aresdogfighter.data;

import com.scs.aresdogfighter.modules.ManualShipControlModule;


/*
 * Shields - no damage to hulll unless shields out (or weapon can bypass shields).  Once shields out, hull gets damaged, does not recharge.
 * Damage to shield unit only affects recharge rate, not protection
 *
 */
public class Shield extends AbstractInstallation {

	private float max_level, curr_level;

	public enum Type {
		SHIELD_1, SHIELD_2;
	}

	public static Shield Factory(ManualShipControlModule game, Type type) {
		switch (type) {
		case SHIELD_1:
			return new Shield(game, "Std Shield", 1f, .2f, 1f);
		case SHIELD_2:
			return new Shield(game, "Deflector Shield", 2f, .5f, 2f);
		default:
			throw new RuntimeException("Unknown type: " + type);
		}
	}


	private Shield(ManualShipControlModule game, String name, float weight, float _protec_level, float power_req) {
		super(game, name, weight, power_req);

		max_level = _protec_level;
		this.curr_level = max_level;
	}


	/**
	 * Retuns if changed
	 */
	@Override
	public void process(float tpf) {
		if (curr_level < max_level)  {
			curr_level += tpf/100;
			if (curr_level > max_level)  {
				curr_level = max_level;
			}
		}
	}


	@Override
	public void damage(float a) {
		super.damage(a);
		this.curr_level -= a;
		if (curr_level < 0) {
			curr_level = 0;
		}
	}


	public float getLevel() {
		return this.curr_level;
	}


	/*public float reduceLevel(float a) {
		this.curr_level -= a;
		float rem = curr_level;
		if (curr_level < 0) {
			curr_level = 0;
		}
		return rem;
	}
	/*public float getProtection() {
		return this.dam_reduction_pcent * health * curr_level;
	}*/

}
