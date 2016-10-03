package com.scs.aresdogfighter.data;

import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public abstract class AbstractInstallation implements IProcessable {
	
	protected ManualShipControlModule module;
	protected String name;
	protected float weight;
	protected float power_req;
	public float health;
	protected GameObject ship;
	
	public AbstractInstallation(ManualShipControlModule _game, String _name, float _weight, float _power_req) {
		super();
		
		module = _game;
		name = _name;
		weight = _weight;
		power_req = _power_req;
		health = 1f;
	}

	
	/*public float getPowerReq() {
		return this.power_req;
	}*/
	
	
	public float getWeight() {
		return this.weight;
	}
	
	
	public void damage(float a) {
		health -= a;
		if (health < 0) {
			health = 0;
		}
	}
	
	
	public String getName() {
		return name;
	}
	
	
}
