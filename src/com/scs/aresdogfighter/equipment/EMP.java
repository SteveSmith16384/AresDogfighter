package com.scs.aresdogfighter.equipment;

import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class EMP extends AbstractEquipment {

	public EMP(ManualShipControlModule _game) {
		super(_game, "EMP", 1f, 1f, 1f, 1);
	}

	
	@Override
	public void use(float tpf, boolean started) {
		
	}


	@Override
	public String getDisplayValue() {
		return null;
	}


}
