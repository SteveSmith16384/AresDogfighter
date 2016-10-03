package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.font.BitmapFont;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class FriendlyPlanet extends AbstractPlanet {

	public FriendlyPlanet(ManualShipControlModule game, String name, AssetManager assetManager, BitmapFont guiFont, Vector3f pos) {
		super(game, SpaceEntity.Type.FRIENDLY_PLANET, name, 20f, assetManager, guiFont, pos);
	}
	

	@Override
	protected Spatial loadPlanetModel(AssetManager assetManager, float diam) {
		Spatial s = JMEFunctions.LoadModel(assetManager, "Models/Quaternius/Planet3.obj");
		BoundingSphere bs = new BoundingSphere();
		s.setModelBound(bs);
		s.updateModelBound();
		s.scale((diam/2)/bs.getRadius());
		return s;
	}


	@Override
	public void process(float tpf) {
		super.process(tpf);

		this.getNode().rotate(0, 0, tpf/500f); // Rotate around sun
	}

}
