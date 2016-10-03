package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.font.BitmapFont;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class Moon extends AbstractPlanet {

	public Moon(ManualShipControlModule game, AssetManager assetManager, BitmapFont guiFont, Vector3f pos) {
		super(game, SpaceEntity.Type.MOON, "Moon", 2f, assetManager, guiFont, pos);

	}
	

	@Override
	protected Spatial loadPlanetModel(AssetManager assetManager, float diam) {
		Spatial s = JMEFunctions.LoadModel(assetManager, "Models/Quaternius/Moon.obj");
		BoundingSphere bs = new BoundingSphere();
		s.setModelBound(bs);
		s.updateModelBound();
		s.scale((diam/2)/bs.getRadius());
		//s.updateModelBound();
		return s;
	}


}
