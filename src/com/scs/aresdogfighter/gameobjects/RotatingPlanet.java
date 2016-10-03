package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.GameObject.ShowMode;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.model.AbstractModel;
import com.scs.aresdogfighter.model.PlanetModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class RotatingPlanet extends AbstractPlanet {

	public RotatingPlanet(ManualShipControlModule game, String name, float diam, AssetManager assetManager, BitmapFont guiFont, Vector3f pos) {
		super(game, SpaceEntity.Type.DESERT_PLANET, name, diam, assetManager, guiFont, pos);

	}

	
	@Override
	protected AbstractModel loadPlanetModel(AssetManager assetManager, float diam) {
		return new PlanetModel(assetManager, diam, true, "sand2.png");
	}



}
