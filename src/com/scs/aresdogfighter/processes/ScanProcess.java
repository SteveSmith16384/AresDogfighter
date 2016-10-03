package com.scs.aresdogfighter.processes;

import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class ScanProcess implements IProcessable {

	private ManualShipControlModule module;
	private GameObject obj;
	private float time_left;
	
	public ScanProcess(ManualShipControlModule mod, GameObject _obj, float dur) {
		super();
		
		module = mod;
		obj = _obj;
		time_left = dur;
	}

	
	@Override
	public void process(float tpf) {
		this.time_left -= tpf;
		if (this.time_left < 0) {
			this.module.log("Scan complete");
			if (obj instanceof Ship1) {
				Ship1 ship = (Ship1)obj;
				this.module.log("Shields: " + ship.shipdata.shield.getName());
				this.module.log("Engines: " + ship.shipdata.engine.getName());
				this.module.log("Weapons: " + ship.shipdata.weapon.getName());
			} else {
				this.module.log("Nothing of note");
			}
			this.module.removeObject(this);
		}
	}

}
