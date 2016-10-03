package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.model.SmokeTrailModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class SmokeTrail extends AbstractScenicGameObject {
	
	public static final float INTERVAL = 0.1f;
	private static final float DURATION = 10f;
	
	private SmokeTrailModel model;

	public SmokeTrail(ManualShipControlModule _game, AssetManager assetManager, BitmapFont guiFont, Vector3f pos, boolean smoke) {
		super(_game, "SmokeTrail", guiFont, DURATION);
		
		model = new SmokeTrailModel(assetManager, smoke);
		model.setLocalTranslation(pos);
	}

	
	@Override
	public void process(float tpf) {
		super.process(tpf);
		model.lookAt(module.getCam().getLocation(), Vector3f.UNIT_Y);
		model.scale(0.95f);
		
	}


	@Override
	public Node getNode() {
		return model;
	}

}
