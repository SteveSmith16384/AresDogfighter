package com.scs.aresdogfighter.data;

import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.equipment.AbstractEquipment;
import com.scs.aresdogfighter.gameobjects.Missile;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class MissileLauncher extends AbstractEquipment {

	public static MissileLauncher Factory(ManualShipControlModule game) {
		return new MissileLauncher(game, "Missile Launcher", 1f, 20, .4f, 7, 1f, 5);
	}


	private MissileLauncher(ManualShipControlModule game, String name, float weight, float _rng, float dam, float _reload, float power_req, int _num_missiles) {
		super(game, name, weight, power_req, _reload, _num_missiles);
	}


	@Override
	public void use(float tpf, boolean started) {
		/*if (started) {
			if (ship.order.target != null) {
				if (checkIfCanShoot(true)) {
					module.log("Missile launched at " + ship.order.target.name);
					Missile missile = new Missile(module, module.getAssetManager(), ship, ship.order.target, 300f, 1f);
					missile.addToGame(module.rootNode);
					//return true;
				} else {
					module.log("Missile launcher offline");
					if (!Statics.MUTE && ship == module.players_ship) {
						ship.cant_shoot.play();
					}
				}
			} else {
				if (ship == module.players_ship) {
					module.log("No target for Missile");
					if (!Statics.MUTE) {
						ship.cant_shoot.play();
					}
				}
			}
		}*/

	}


	@Override
	public String getDisplayValue() {
		return "" + this.num_items;
	}


}
