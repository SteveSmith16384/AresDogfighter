package com.scs.aresdogfighter.equipment;

import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class ManualShield extends AbstractEquipment {
	
	public ManualShield(ManualShipControlModule _game, String _name, float _weight, float _power_req, float _reload, int _num_missiles) {
		super(_game, "ManualShield", 1, 1, 3f, -1); // todo
	}

	
	@Override
	public void use(float tpf, boolean started) {

	}


	@Override
	public String getDisplayValue() {
		return null;
	}

}
