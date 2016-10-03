package com.scs.aresdogfighter.ai;

import com.scs.aresdogfighter.gameobjects.Ship1;

public class AIMessage {
	
	public enum Type {
		SEEN_SHOOTING, SHOT_BY;
	}

	public Type type;
	public Ship1 shooter, target;
	public float amt;
	
	public AIMessage(Type _type, Ship1 _shooter, float _amt) {
		super();
		
		type = _type;
		shooter = _shooter;
		amt = _amt;
	}

	public AIMessage(Type _type, Ship1 _shooter, Ship1 _target, float _amt) {
		super();
		
		type = _type;
		shooter = _shooter;
		target = _target;
		amt = _amt;
	}

	
	@Override
	public String toString() {
		return type + " - " + shooter + " / " + target;
	}
}
