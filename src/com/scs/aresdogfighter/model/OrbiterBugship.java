package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.JMEFunctions;

public class OrbiterBugship extends AbstractModel {

	public OrbiterBugship(AssetManager assetManager) {
		super("OrbiterBugship");

		Spatial ship = JMEFunctions.LoadModel(assetManager, "Models/orbiterbugship/orbiter bugship/orbiter bugship.obj");
		//Spatial ship = assetManager.loadModel("Models/orbiterbugship/orbiter bugship/orbiter bugship.obj");
		ship.move(0f, -1f, 0f);
		ship.rotate(0, 90 * FastMath.DEG_TO_RAD, 0);
		ship.scale(0.01f);
		
		ship.setModelBound(new BoundingBox());
		ship.updateModelBound();

		//ship.setUserData("id", id);
		this.attachChild(ship);

		this.generateLODs();
	}


}
