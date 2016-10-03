package com.scs.aresdogfighter.data;

import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class Weapon extends AbstractInstallation { 

	public enum Type {
		NONE, STD_LASER, HEAVY_LASER, SNIPER_LASER;
	}

	public enum BulletType {
		BURST_LASER, PULSE_LASER;//, BEAM_LASER;
	}

	private float range, damage;
	private float reload_time;
	private float this_reload_time;
	private float accuracy;
	private float time_until_next_shot;
	private float current_temp, heat_per_shot;
	public BulletType bullet_type;

	public static Weapon Factory(ManualShipControlModule game, Type type) {
		switch (type) {
		case NONE:
			return null;
		case STD_LASER:
			return new Weapon(game, "Standard Laser", 1f,   160,  .6f, .4f, 1f, 0.5f, BulletType.PULSE_LASER, 0.1f);
		case HEAVY_LASER:
			return new Weapon(game, "Heavy Laser"   , 1.3f, 240,  2f,  .8f, 3f, 0.4f, BulletType.PULSE_LASER, 0.15f);
		case SNIPER_LASER:
			return new Weapon(game, "Sniper Laser"  , 1.1f, 320, 1.2f,  2, 2f, 0.8f, BulletType.PULSE_LASER, 0.2f);
		default:
			throw new RuntimeException("Unknown weapon: " + type);
		}
	}


	private Weapon(ManualShipControlModule game, String name, float weight, float _rng, float dam, float _reload, float power_req, float _acc, BulletType _bullet_type, float _heat_per_shot) {
		super(game, name, weight, power_req);

		range = _rng;
		damage = dam*3;
		reload_time = _reload;
		this.accuracy = _acc;
		bullet_type = _bullet_type;
		heat_per_shot = _heat_per_shot;
	}

	
	public void process(float tpf) {
		time_until_next_shot -= tpf;
		this.current_temp -= tpf * Statics.WEAPON_COOLDOWN;
		if (current_temp < 0) {
			current_temp = 0;
		} else {
			if (ship == module.players_avatar) {
				module.hud.setTempText(this.current_temp);
			}
		}
	}


	public boolean checkIfCanShoot() {
		if (current_temp > 1f) {
			return false;
		}
		boolean can_shoot = time_until_next_shot <= 0;
		if (can_shoot) {
			//module.log("Shoot");
			this.this_reload_time = reload_time;// + NumberFunctions.rndFloat(-1, 1); 
			time_until_next_shot = this_reload_time;
			current_temp += this.heat_per_shot;
			if (ship == module.players_avatar) {
				module.hud.setTempText(this.current_temp);
			}
		} else {
			//module.log("NOT Shoot");
		}
		return can_shoot;
	}


	public float getRange() {
		return this.range;
	}


	public float getDamage() {
		return this.damage * health;
	}


	public float getAccuracy() {
		return this.accuracy * health;
	}

	
	public float getCurrentTemp() {
		return this.current_temp;
	}
}
