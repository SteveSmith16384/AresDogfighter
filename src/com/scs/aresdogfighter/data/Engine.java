package com.scs.aresdogfighter.data;

import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class Engine extends AbstractInstallation {

	private float acc_dec;
	private float turn_speed, max_speed;

	public enum Type {
		PLAYERS_ENGINE, STD_ENGINE, CIV_ENGINE, FAST_SPEED_SLOW_TURN, SLOW_SPEED_FAST_TURN, FAST_SPEED_FAST_TURN;
	}

	public static Engine Factory(ManualShipControlModule game, Type type) {
		switch (type) {
		case PLAYERS_ENGINE:
			return new Engine(game, "Players Engine", 1f,  1f,  1.1f,  26f, 3.5f);
		case CIV_ENGINE:
			return new Engine(game, "Civ Engine"   , 1f,  1f,  .3f,  .5f, 0.5f);
		case STD_ENGINE:
			return new Engine(game, "Std Engine"   , 1f,  1f,  1.1f,  1f, 2.5f); // Turn was 2f, speed was 3.5
		case FAST_SPEED_SLOW_TURN: // Easy to shoot
			return new Engine(game, "Thruster"     , 1.5f, 1.5f, 1.1f,  6f, 6f);
		case SLOW_SPEED_FAST_TURN:
			//return new Engine(game, "Plasma Engine", 2f,    2f,  1.1f,  13f, 3f); Hard but hard to find
			return new Engine(game, "Plasma Engine", 2f,   2f,  1.1f,  10f, 7f);
		case FAST_SPEED_FAST_TURN:
			return new Engine(game, "Turbo Engine", 2f,    2f,  1.1f,   13f, 6f);
		default:
			throw new RuntimeException("Unknown type: " + type);
		}
	}

	
	private Engine(ManualShipControlModule game,String name, float weight, float power_req, float _acc, float turn, float _max_speed) {
		super(game, name, weight, power_req);

		this.acc_dec = _acc * 7;
		this.turn_speed = turn * 1f;//0.4f;//0.25f;
		this.max_speed = _max_speed * 2f;
	}

	
	public float getAcc() {
		return this.acc_dec * this.health;
	}

	
	public float getTurnSpeed() {
		return this.turn_speed * this.health;
	}


	public float getMaxSpeed() {
		return this.max_speed * this.health;
	}


	@Override
	public void process(float tpf) {
		// Do nothing (yet?)
		
	}
	
}
