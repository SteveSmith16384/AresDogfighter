package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.GameObject.ShowMode;
import com.scs.aresdogfighter.model.HyperspaceEffectModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class HyperspaceEffect extends GameObject {
	
	public HyperspaceEffectModel model;
	private GameObject ship_adjoining;
	
	public HyperspaceEffect(ManualShipControlModule game, AssetManager assetManager, GameObject _gameobject) {
		super(game, null, "HyperspaceEffect", null, false, false, false, false, ShowMode.ALWAYS, false);
		
		model = new HyperspaceEffectModel(assetManager);
		ship_adjoining = _gameobject;
		
		stop();
	}

	
	public void start() {
		model.start();
	}

	
	public void stop() {
		model.stop();
	}

	
	@Override
	public void process(float tpf) {
		super.process(tpf);

		Vector3f forward = ship_adjoining.getNode().getLocalRotation().mult(Vector3f.UNIT_Z);
		this.model.setLocalTranslation(ship_adjoining.getNode().getWorldTranslation().add(forward.multLocal(5)));
		this.model.lookAt(ship_adjoining.getNode().getWorldTranslation(), Vector3f.UNIT_Y);
	
	}

	
	@Override
	public Node getNode() {
		return model;
	}

	/*
	@Override
	public float getWeaponDamage() {
		throw new RuntimeException("Don't access this");
	}
	
*/
	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f pos) {
		//throw new RuntimeException("Don't access this");
	}


	@Override
	public float getSpeed() {
		return 0;
	}



}
