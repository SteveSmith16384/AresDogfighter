package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.JMEFunctions;

public class KameriExplorer extends AbstractModel {

	public KameriExplorer(AssetManager assetManager) {
		super("KameriExplorer");

		Spatial ship = JMEFunctions.LoadModel(assetManager, "Models/KameriExplorer/Kameri explorer flying.obj");
		//Spatial ship = assetManager.loadModel("Models/KameriExplorer/Kameri explorer flying.obj");
		ship.rotate(0, 180 * FastMath.DEG_TO_RAD, 0);
		ship.scale(0.01f);
		ship.move(0f, -.5f, 0f);
		
		ship.setModelBound(new BoundingBox());
		ship.updateModelBound();

		//ship.setUserData("id", id);
		this.attachChild(ship);
		this.generateLODs();
	}


}
