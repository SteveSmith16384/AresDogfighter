package com.scs.aresdogfighter.data;

import java.util.ArrayList;
import java.util.List;

import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.avatars.PlayersShip;
import com.scs.aresdogfighter.equipment.AbstractEquipment;
import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class ShipData implements IProcessable {

	private static final long UPDATE_INTERVAL = 1;

	public Ship1 ship; 
	public Shield shield;
	public Engine engine;
	public Weapon weapon;
	private AbstractEquipment selected_equipment;
	private List<AbstractEquipment> equipment = new ArrayList<AbstractEquipment>();

	private float mass = 0f, extra_mass = 0f;
	private float curr_shield_pcent = 1f, curr_engine_pcent = 1f, curr_weapon_pcent = 1f;
	private float curr_speed;
	private float hull_health = 1f;
	private ManualShipControlModule module;
	public float bounty = 0;
	public int credits = 0;
	private long next_update_time;
	private int num_cargo = 0;

	public ShipData(ManualShipControlModule _module, Ship1 _ship, Shield _shield, Engine _engine, Weapon _weapon, float _extra_mass) {
		super();

		module = _module;
		ship = _ship;
		shield = _shield;
		engine = _engine;
		weapon = _weapon;
		extra_mass = _extra_mass;

		//this.curr_speed = this.engine.getMaxSpeed(); // Just warped in, so max speed!
	}


	public float getAcceleration() {
		return engine.getAcc() * curr_engine_pcent / mass;
	}


	public void addEquipment(AbstractEquipment eq) {
		this.equipment.add(eq);
		if (this.selected_equipment == null) {
			this.selected_equipment = eq;
			if (ship == module.players_avatar) {
				module.hud.setEquipmentText(selected_equipment);
			}
		}
	}


	public void selectNextEquipment() {
		int pos = this.equipment.indexOf(this.selected_equipment);
		pos++;
		if (pos >= this.equipment.size()) {
			pos = 0;
		}
		this.selected_equipment = this.equipment.get(0);

		if (ship == module.players_avatar) {
			module.hud.setEquipmentText(selected_equipment);
		}
	}


	public AbstractEquipment getSelectedEquipment() {
		return this.selected_equipment;
	}


	@Override
	public void process(float tpf) {
		this.bounty -= tpf;
		if (this.bounty < 0) {
			this.bounty = 0;
		}

		// Adjust levels
		/*if (Math.abs(curr_shield_pcent-target_shield_pcent) > .05f) {
			this.curr_shield_pcent += Math.signum(this.target_shield_pcent - this.curr_shield_pcent) * tpf * ADJ_SPEED;
			if (this.ship == module.players_ship) {
				module.hud.our_ship_stats.needs_to_update = true;
			}
		} else {
			curr_shield_pcent = target_shield_pcent;
		}
		if (Math.abs(curr_engine_pcent-target_engine_pcent) > .05f) {
			this.curr_engine_pcent += Math.signum(this.target_engine_pcent - this.curr_engine_pcent) * tpf * ADJ_SPEED;
			if (this.ship == module.players_ship) {
				module.hud.our_ship_stats.needs_to_update = true;
			}
		} else {
			curr_engine_pcent = target_engine_pcent;
		}
		if (Math.abs(curr_weapon_pcent-target_weapon_pcent) > .05f) {
			this.curr_weapon_pcent += Math.signum(this.target_weapon_pcent - this.curr_weapon_pcent) * tpf * ADJ_SPEED;
			if (this.ship == module.players_ship) {
				module.hud.our_ship_stats.needs_to_update = true;
			}
		} else {
			curr_weapon_pcent = target_weapon_pcent;
		}*/

		if (weapon != null) { // Some ships have no weapons
			weapon.process(tpf);
		}
		shield.process(tpf);
		engine.process(tpf);
		for (AbstractEquipment eq : this.equipment) {
			eq.process(tpf);
		}

		/*if (this.curr_speed > this.getMaxSpeed()) {
			this.decelerate();
		}*/

		if (this.ship == module.players_avatar) {
			next_update_time -= tpf;
			if (next_update_time < 0) {
				next_update_time = UPDATE_INTERVAL;

				module.hud.setTempText(this.weapon.getCurrentTemp());
				module.hud.setShieldsText(this.shield.getLevel());
			}
		}
	}


/*	public float getWeaponAccuracy() {
		return this.weapon.getAccuracy();
	}
*/

	public float getWeaponRange() {
		if (weapon != null) {
		return weapon.getRange();// * this.curr_weapon_pcent;
		} else {
			return 0;
		}
	}


	/*
	 * Only use this for AI ships
	 */
	public float getTurnSpeed(float tpf) {
		if (mass <= 0) {
			throw new RuntimeException("Mass is zero!");
		}
		return (this.engine.getTurnSpeed() * curr_engine_pcent * tpf) / 3f;
	}


	public float getMaxSpeed() {
		float max = engine.getMaxSpeed() * (float)Math.max(curr_engine_pcent, 0.1); // min engine pcent so ship can move
		return max;
	}


	public void accelerate(float tpf) {
		float max = this.getMaxSpeed();
		if (this.curr_speed > max) {
			this.decelerate(tpf);
		} else {
			this.curr_speed += this.getAcceleration() * tpf;
		}
		if (this.ship == module.cam_view) {
			module.hud.setSpeedText(curr_speed);
		}
	}


	/*	public void halt() {
		if (this.curr_speed > 0) {
			this.decelerate();
		} else {
			this.accelerate();
		}
	}*/


	public void slowDown(float tpf) {
		float max = this.getMaxSpeed();
		if (this.curr_speed > max/2) {
			this.decelerate(tpf);
		}
	}

	public void decelerate(float tpf) {
		this.curr_speed -= this.getAcceleration()*2*tpf;
		if (curr_speed < 0) {
			curr_speed = 0;
		}
		if (this.ship == module.cam_view) {
			module.hud.setSpeedText(curr_speed);
		}
	}


	public float getCurrentSpeed() {
		return this.curr_speed;
	}


	/*public float getCurrentPowerLevel() {
		return this.power.getCurrentLevel();
	}*/


	/*public void reducePowerLevel(float a) {
		this.power.reducePower(a);
	}*/


	public float getShieldLevel() {
		return this.shield.getLevel();// * curr_shield_pcent;// * health;
	}


	public float getWeaponDamage() {
		return this.weapon.getDamage() * curr_weapon_pcent;// * health;
	}


	public float getCurrShieldPcent() {
		return curr_shield_pcent;
	}


	public float getCurrEnginePcent() {
		return curr_engine_pcent;
	}


	public float getCurrWeaponPcent() {
		return curr_weapon_pcent;
	}
/*

	public AbstractInstallation get(CaptainsOrder.Section section) {
		switch (section) {
		case ENGINES:
			return this.engine;
		case WEAPONS:
			return this.weapon;
		case SHIELD:
			return this.shield;
		default:
			throw new RuntimeException("Unknown section: " + section);
		}
	}

	/*
	public float getTargetShieldsPCent() {
		return this.target_shield_pcent;
	}


	public float getTargetEnginePCent() {
		return this.target_engine_pcent;
	}


	public float getTargetWeaponPCent() {
		return this.target_weapon_pcent;
	}

/*
	public void ensureTargetShieldPCent(float a) {
		if (this.target_shield_pcent < a) {
			this.setTargetShieldPCent(a);
		}
	}


	/*public void setTargetShieldPCent(float a) {
		if (a > 1) {
			throw new IllegalArgumentException("Value > 1f");
		}
		this.target_shield_pcent = a;
		this.last_section_selected = Section.SHIELD;
		setTargets();
	}


	public void ensureTargetEnginePCent(float a) {
		if (this.target_engine_pcent < a) {
			this.setTargetEnginePCent(a);
		}
	}


	public void setTargetEnginePCent(float a) {
		if (a > 1) {
			throw new IllegalArgumentException("Value > 1f");
		}
		this.target_engine_pcent = a;
		this.last_section_selected = Section.ENGINES;
		setTargets();
	}


	public void ensureTargetWeaponPCent(float a) {
		if (this.target_weapon_pcent < a) {
			this.setTargetWeaponPCent(a);
		}
	}


	public void setTargetWeaponPCent(float a) {
		if (a > 1) {
			throw new IllegalArgumentException("Value > 1f");
		}
		this.target_weapon_pcent = a;
		this.last_section_selected = Section.WEAPONS;
		setTargets();
	}


	private void setTargets() {
		while (target_shield_pcent + target_engine_pcent + target_weapon_pcent > 1) {
			float ADJ = 0.01f;
			// Note we dont adjust last selected installation
			if (target_shield_pcent > 0 && this.last_section_selected != Section.SHIELD) {
				target_shield_pcent -= Math.min(target_shield_pcent, ADJ);
			}
			if (target_engine_pcent > 0 && this.last_section_selected != Section.ENGINES) {
				target_engine_pcent -= Math.min(target_engine_pcent, ADJ);
			}
			if (target_weapon_pcent > 0 && this.last_section_selected != Section.WEAPONS) {
				target_weapon_pcent -= Math.min(target_weapon_pcent, ADJ);
			}
		}

	}*/


	public float getHealth() {
		return hull_health; //this.shield.health + this.engine.health + this.weapon.health;
	}


	public float getThreat() {
		return this.getWeaponDamage()  * this.hull_health;
	}


	public void setCurrSpeed(float s) {
		this.curr_speed = s;
		if (this.ship == module.cam_view) {
			module.hud.setSpeedText(curr_speed);
		}
	}


	public void damage(float a) {
		this.hull_health -= a;
		if (hull_health < 0) {
			hull_health = 0;
		}
		if (this.ship == module.players_avatar) {
			module.hud.setHullText(hull_health);
		}

	}


	@Override
	public String toString() {
		return "ShipData:" + ship.getName();
	}


	public void finishLoadout() {
		if (weapon != null) { // Some ships have no weapons
			this.weapon.ship = ship;
		}
		this.shield.ship = ship;
		this.engine.ship = ship;
		for (AbstractEquipment eq : this.equipment) {
			eq.ship = ship;
		}
		this.recalcMass();
	}


	private void recalcMass() {
		mass = shield.weight + engine.weight + (weapon!=null?weapon.weight:0) + extra_mass;
	}

	
	public void adjustCargo(int a) {
		this.num_cargo += a;
		if (this.ship instanceof PlayersShip) {
			//PlayersShip ps = (PlayersShip);
			//ps.
			module.hud.setCargoText(this.num_cargo);
		}
	}

	
	public int getNumCargo() {
		return this.num_cargo;
	}
}
