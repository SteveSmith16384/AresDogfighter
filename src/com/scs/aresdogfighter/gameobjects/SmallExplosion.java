package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.model.SmallExplosionModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class SmallExplosion extends GameObject {

	private SmallExplosionModel model;
	private float show_until;

	private AudioNode explosion_sound;

	public SmallExplosion(ManualShipControlModule game, AssetManager assetManager, float duration) {
		super(game, null, "SmallExplosion", null, false, false, false, false, ShowMode.ALWAYS, false);

		model = new SmallExplosionModel(assetManager, game.getRenderManager());
		show_until = duration;

		if (!Statics.MUTE) {
			explosion_sound = new AudioNode(assetManager, "Sound/explode.wav", false);
			explosion_sound.setPositional(false);
			this.getNode().attachChild(explosion_sound);
			explosion_sound.play();
		}
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
	
	
	@Override
	public float getSpeed() {
		return 0;
	}



	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f pos) {
		//throw new RuntimeException("Don't access this");
	}


}
