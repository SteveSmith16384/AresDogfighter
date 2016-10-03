package com.scs.aresdogfighter.model;

import ssmith.lang.NumberFunctions;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.scs.aresdogfighter.shapes.MySphere;

public class SpaceDebrisModel extends AbstractModel {

	public float diam;
	
	public SpaceDebrisModel(AssetManager assetManager) {
		super("SpaceDebrisModel");

		//Spatial ship = JMEFunctions.loadModel(assetManager, "Models/Rock1/Rock1.obj");
		//ship.move(-1f, 1f, 1f);
		//ship.scale(0.01f);
		diam = NumberFunctions.rndFloat(5, 9);
		MySphere ship = new MySphere(diam, 10, 10, "SpaceDebrisModel", assetManager, "moonrock.png");
		
		ship.setModelBound(new BoundingSphere());
		ship.updateModelBound();

		//ship.setUserData("id", id);
		this.attachChild(ship);
		
	}

	
}
