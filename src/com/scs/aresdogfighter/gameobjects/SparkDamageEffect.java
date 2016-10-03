package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.GameObject.ShowMode;
import com.scs.aresdogfighter.model.SparkDamageEffectModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class SparkDamageEffect extends GameObject {
	
	private SparkDamageEffectModel model;
	private GameObject ship_adjoining;
	private float show_until;

	public SparkDamageEffect(ManualShipControlModule game, AssetManager assetManager, GameObject _gameobject, float duration) {
		super(game, null, "SparkDamageEffect", null, false, false, false, false, ShowMode.ALWAYS, false);
		
		model = new SparkDamageEffectModel(assetManager);
		ship_adjoining = _gameobject;
		show_until = duration;
	}

	
	@Override
	public void process(float tpf) {
		super.process(tpf);

		model.process();
		
		show_until -= tpf;
		if (show_until < 0) {
			this.removeFromGame(true, false);
			return;
		}
		
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
