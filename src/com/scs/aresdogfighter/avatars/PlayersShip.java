package com.scs.aresdogfighter.avatars;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.data.CaptainsOrder.OrderType;
import com.scs.aresdogfighter.data.Engine;
import com.scs.aresdogfighter.data.Faction;
import com.scs.aresdogfighter.data.Shield;
import com.scs.aresdogfighter.data.ShipData;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.data.Weapon;
import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class PlayersShip extends Ship1 implements AbstractAvatar {

	private static final float PLAYERS_TURN = .1f;
	private static final float MIN_TURN = .1f;
	private static final float TURN_REDUCTION = 3f;

	public float pitch_ud, roll_lr, turn_lr;
	public volatile boolean start_shooting = false;

	public PlayersShip(ManualShipControlModule game, AssetManager assetManager, BitmapFont guiFont) {
		super(game, SpaceEntity.Type.PLAYERS_SHIP, "Player", assetManager, guiFont, 
				Shield.Factory(game, Shield.Type.SHIELD_1), 
				Engine.Factory(game, Engine.Type.PLAYERS_ENGINE), 
				Weapon.Factory(game, Weapon.Type.STD_LASER), 
				Faction.PLAYER_FRIENDLY, 0f);

		//order.setOrder(OrderType.MANUAL_CONTROL);
		shipdata.setCurrSpeed(0);

	}


	@Override
	public void process(float tpf) {
		/*ManualShipControlModule mod = (ManualShipControlModule)module;
		/*if (mod.landing_on_planet) {
			if (this.getNode().getWorldTranslation().y > Statics.RETURN_TO_SPACE_DIST) {
				mod.returnToSpace();
			}
		}*/

		if (start_shooting) {
			this.shoot(null);
		}

		if (this.shipdata.getCurrentSpeed() < 0) {
			this.shipdata.accelerate(tpf);
		} else if (this.shipdata.getCurrentSpeed() == 0) {
			checkForDocking();
		}


		if (pitch_ud != 0) {
			if (pitch_ud > shipdata.getTurnSpeed(PLAYERS_TURN)) {
				pitch_ud = shipdata.getTurnSpeed(PLAYERS_TURN);
				module.debug("Max turn reached");
			} else if (pitch_ud < -shipdata.getTurnSpeed(PLAYERS_TURN)) {
				pitch_ud = -shipdata.getTurnSpeed(PLAYERS_TURN);
				module.debug("Max turn reached");
			}
			pitch_ud = tendToZero(pitch_ud, TURN_REDUCTION * tpf);
		}
		if (pitch_ud != 0) {
			if (Math.abs(pitch_ud) < MIN_TURN * tpf) { // Need this otherwise turn never more thsn min turn
				pitch_ud = 0;
			} else {
				this.pitchUp(tpf*pitch_ud); // Doesn't matter if up/down since number is negative
			}
			//module.log("pitch_ud="+pitch_ud);
		}

		if (roll_lr != 0) {
			if (roll_lr > shipdata.getTurnSpeed(PLAYERS_TURN)) {
				roll_lr = shipdata.getTurnSpeed(PLAYERS_TURN);
				module.debug("Max turn reached");
			} else if (roll_lr < -shipdata.getTurnSpeed(PLAYERS_TURN)) {
				roll_lr = -shipdata.getTurnSpeed(PLAYERS_TURN);
				module.debug("Max turn reached");
			}
			roll_lr = tendToZero(roll_lr, TURN_REDUCTION * tpf);
		}
		if (roll_lr != 0) {
			if (Math.abs(roll_lr) < MIN_TURN * tpf) { // Need this otherwise turn never more thsn min turn
				roll_lr = 0;
			} else {
				this.rollLeft(tpf*roll_lr);
			}
		}

		if (turn_lr != 0) {
			if (turn_lr > shipdata.getTurnSpeed(PLAYERS_TURN)) {
				turn_lr = shipdata.getTurnSpeed(PLAYERS_TURN);
				module.debug("Max turn reached");
			} else if (turn_lr < -shipdata.getTurnSpeed(PLAYERS_TURN)) {
				turn_lr = -shipdata.getTurnSpeed(PLAYERS_TURN);
				module.debug("Max turn reached");
			}
			turn_lr = tendToZero(turn_lr, TURN_REDUCTION * tpf);
		}
		if (turn_lr != 0) {
			if (Math.abs(turn_lr) < MIN_TURN * tpf) { // Need this otherwise turn never more thsn min turn
				turn_lr = 0;
			} else {
				this.turnRight(tpf*turn_lr);
			}
		}
		super.process(tpf);

		this.shipdata.getSelectedEquipment().process(tpf);
	}


	private float tendToZero(float f, float tpf) {
		float i = f - (f*tpf);
		return i;
	}


	@Override
	public void onAnalog(String name, float value, float tpf) {
		//log("onAnalog:" + tpf);
		if (name.equalsIgnoreCase(ManualShipControlModule.PITCH_UP_MOUSE)) {
			this.pitch_ud += value*Statics.MOUSE_SENSITIVITY;
		} else if (name.equalsIgnoreCase(ManualShipControlModule.PITCH_DOWN_MOUSE)) {
			this.pitch_ud -= value*Statics.MOUSE_SENSITIVITY;

		} else if (name.equalsIgnoreCase(ManualShipControlModule.PITCH_UP_KB)) {
			this.pitch_ud += value*ManualShipControlModule.KEYB_SENS;
			//this.debug("value=" + value);
		} else if (name.equalsIgnoreCase(ManualShipControlModule.PITCH_DOWN_KB)) {
			this.pitch_ud -= value*ManualShipControlModule.KEYB_SENS;
			//this.debug("value=" + value);
		} else if (name.equalsIgnoreCase(ManualShipControlModule.STRAFE_LEFT_MOUSE)) {
			this.turn_lr += value*Statics.MOUSE_SENSITIVITY;
		} else if (name.equalsIgnoreCase(ManualShipControlModule.STRAFE_RIGHT_MOUSE)) {
			this.turn_lr -= value*Statics.MOUSE_SENSITIVITY;

		} else if (name.equalsIgnoreCase(ManualShipControlModule.STRAFE_LEFT_KB)) {
			this.turn_lr += value*ManualShipControlModule.KEYB_SENS;
		} else if (name.equalsIgnoreCase(ManualShipControlModule.STRAFE_RIGHT_KB)) {
			this.turn_lr -= value*ManualShipControlModule.KEYB_SENS;

		} else if (name.equalsIgnoreCase(ManualShipControlModule.ROLL_LEFT)) {
			this.roll_lr += value*ManualShipControlModule.KEYB_SENS;
		} else if (name.equalsIgnoreCase(ManualShipControlModule.ROLL_RIGHT)) {
			this.roll_lr -= value*ManualShipControlModule.KEYB_SENS;
		} else if (name.equalsIgnoreCase(ManualShipControlModule.ACCELERATE)) {
			this.shipdata.accelerate(tpf);
			module.setHyperspaceValues();
		} else if (name.equalsIgnoreCase(ManualShipControlModule.DECELERATE)) {
			if (shipdata.getCurrentSpeed() > 0) {
				shipdata.decelerate(tpf);
				module.setHyperspaceValues();
			}
		}
	}


	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals(ManualShipControlModule.SHOOT)) {
			setStartShooting(isPressed);
			return;
		} else if (name.equalsIgnoreCase(ManualShipControlModule.MISSILE)) {
			//this.players_ship.shipdata.getSelectedEquipment().use(tpf, isPressed);
			//this.hud.setEquipmentText(this.players_ship.shipdata.getSelectedEquipment());
		}


	}


	private void setStartShooting(boolean b) {
		this.start_shooting = b;

	}


	@Override
	public GameObject getGameObject() {
		return this;
	}


	protected void checkForDocking() {
		if (Statics.GAME_TYPE == Statics.GT_ADVENTURE) {
			ManualShipControlModule mod = (ManualShipControlModule)module;
			float dist = this.distance(mod.getSpaceStation()); 
			if (dist < Statics.DOCK_DIST && this.already_docked == false) {
				/*//This is for bringing up the module
				module.game.selectModule(Module.DOCKING, true);
				already_docked = true;*/

				// This is for helping the spacestation
				if (this.shipdata.getNumCargo() > 0 && mod.getSpaceStation().getHealth() < 1) {
					this.shipdata.adjustCargo(-1);
					mod.getSpaceStation().incHealth(0.1f);
				}
			}

		}
	}


}
