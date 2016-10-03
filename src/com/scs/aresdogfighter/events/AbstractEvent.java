package com.scs.aresdogfighter.events;

import ssmith.lang.NumberFunctions;

import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public abstract class AbstractEvent {//implements IProcessable {
	
	private static final float LONG_INTERVAL_SECS = 5;
	
/*	public enum Type {
		//DISTRESS_CALL,
		METEOR_STORM;
	}
*/
	
	protected ManualShipControlModule module;
	private float time_till_next_process = 0;
	
	public static AbstractEvent GetRandomEvent(ManualShipControlModule m) {
		int z = 1; //todo - re-add NumberFunctions.rnd(1, 2);
		switch (z) {
		case 1: return new EnemyShipArrivedEvent(m);
		case 2: return new MeteorStormEvent(m);
		default: throw new RuntimeException("No such event: " + z);
		}
	}
	
	
	protected AbstractEvent(ManualShipControlModule _module) {
		super();
		
		module = _module;
	}

	
	public abstract String getName();

	public abstract void setupEvent();
	
	//public abstract void processLongInterval();

	/*public void process(float tpf) {
		time_till_next_process -= tpf;
		if (time_till_next_process <= 0) {
			this.processLongInterval();
			time_till_next_process = LONG_INTERVAL_SECS;
		}
	}
*/

}
