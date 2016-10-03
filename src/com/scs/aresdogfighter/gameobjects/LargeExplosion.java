package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.model.LargeExplosionModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class LargeExplosion extends GameObject {
	
	private LargeExplosionModel model;
	private float show_until;
	
	public LargeExplosion(ManualShipControlModule game, AssetManager assetManager, float duration) {
		super(game, null, "LargeExplosion", null, false, false, false, false, ShowMode.ALWAYS, false);
		
		model = new LargeExplosionModel(assetManager);
		show_until = duration;
	}

	@Override
	public void process(float tpf) {
		super.process(tpf);

		model.process();
		
		show_until -= tpf;
		if (show_until < 0) {
			this.removeFromGame(true, false);
		}
		
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
		throw new RuntimeException("Don't access this");
	}


	@Override
	public float getSpeed() {
		return 0;
	}



}
