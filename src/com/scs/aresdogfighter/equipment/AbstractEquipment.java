package com.scs.aresdogfighter.equipment;

import ssmith.lang.NumberFunctions;

import com.jme3.font.BitmapFont;
import com.scs.aresdogfighter.data.AbstractInstallation;
import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

/**
 * For equipment that can be selected then used.
 *
 */
public abstract class AbstractEquipment extends AbstractInstallation {

	private float reload_time;
	private float this_reload_time;
	private float time_until_next_shot;
	public int num_items;

	
	public AbstractEquipment(ManualShipControlModule _game, String _name, float _weight, float _power_req, float _reload, int _num_missiles) {
		super(_game, _name, _weight, _power_req);

		reload_time = _reload;
		this.num_items = _num_missiles;
	}
	
	
	public abstract String getDisplayValue();

	public abstract void use(float tpf, boolean started);


	@Override
	public void process(float tpf) {
		time_until_next_shot -= tpf;
	}

	
	protected boolean checkIfCanShoot(boolean restart) {
		if (this.num_items <= 0) {
			return false;
		}
		boolean can_shoot = time_until_next_shot <= 0;
		if (can_shoot && restart) {
			this.this_reload_time = reload_time + NumberFunctions.rndFloat(-1, 1); 
			time_until_next_shot = this_reload_time;
			num_items--;
		}
		return can_shoot;
	}


	public int getNumItems() {
		return this.num_items;
	}


}
