package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.JMEFunctions;

public class MissileModel2 extends AbstractModel {

	public MissileModel2(AssetManager assetManager) {
		super("MissileModel2");

		Spatial ship = JMEFunctions.LoadModel(assetManager, "Models/missile/missile.obj");

		ship.move(0f, -1f, 0f);
		ship.setLocalRotation(ship.getLocalRotation().opposite());
		ship.scale(0.001f);

		ship.setModelBound(new BoundingSphere());
		ship.updateModelBound();

		//ship.setUserData("id", id);
		this.attachChild(ship);
		this.generateLODs();

	}


}
