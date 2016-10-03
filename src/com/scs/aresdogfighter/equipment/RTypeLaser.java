package com.scs.aresdogfighter.equipment;

import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class RTypeLaser extends AbstractEquipment {
	
	private float current_power;
	private boolean charging = false;

	public RTypeLaser(ManualShipControlModule _mod, String _name, float _weight, float _power_req, float _reload, int _num_missiles) {
		super(_mod, "RTypeLaser", 1, 1, 3f, -1);
	}

	
	@Override
	public void use(float tpf, boolean started) {
		charging = started;
		if (started) {
		} else {
			//module.players_ship.shoot(null); // todo - more power
			current_power = 0;
		}
	}


	@Override
	public String getDisplayValue() {
		return String.format("%d", (int)(current_power*100));
	}

	
	@Override
	public void process(float tpf) {
		super.process(tpf);
		
		if (charging) {
			this.current_power += tpf;
		}
	}

	

}
