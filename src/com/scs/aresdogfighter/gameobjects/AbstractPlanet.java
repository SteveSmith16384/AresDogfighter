package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public abstract class AbstractPlanet extends GameObject {

	private Node planet;
	//private float land_dist;
	private float orbit_dist;
	

	public AbstractPlanet(ManualShipControlModule game, SpaceEntity.Type type, String name, float diam, AssetManager assetManager, BitmapFont guiFont, Vector3f pos) {
		super(game, type, name, guiFont, false, true, true, true, ShowMode.SPACE, false);

		planet = new Node(name);
		Spatial p = loadPlanetModel(assetManager, diam);
		p.setLocalTranslation(pos);
		planet.attachChild(p);
		planet.setUserData("gameobject", this);
		
		planet.setModelBound(new BoundingSphere());
		planet.updateModelBound();


		//land_dist = (diam/2) * 1.3f;
		
		orbit_dist = pos.length();

	}


	protected abstract Spatial loadPlanetModel(AssetManager assetManager, float diam);


	@Override
	public void process(float tpf) {
		super.process(tpf);

		//this.getSpatial().rotate(0, 0, tpf*FastMath.DEG_TO_RAD);

		/*if (Statics.LAND_ON_PLANET) {
			// Check how close the player is
			if (this.dist_from_player <= land_dist) {
				ManualShipControlModule mod = (ManualShipControlModule)module;
				mod.landOnPlanet();
				//Not needed - this.removeFromGame(false, true);
			}
		}*/
	}


	@Override
	protected ColorRGBA getHUDColour() {
		return ColorRGBA.Blue;
	}


	@Override
	public Node getNode() {
		return planet;
	}

	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f pos) {
		module.showSmallExplosion(pos, Statics.SMALL_EXPLOSION_DURATION);
	}


	@Override
	public void updateHUDText() {
		hudText.setText(this.name);
	}

	@Override
	public float getSpeed() {
		return 0;
	}



}