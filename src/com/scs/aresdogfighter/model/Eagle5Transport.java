package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.JMEFunctions;

public class Eagle5Transport extends AbstractModel {

	public Eagle5Transport(AssetManager assetManager) {
		super("Eagle5Transport");

		Spatial ship = JMEFunctions.LoadModel(assetManager, "Models/eagle 5 transport/eagle 5 transport/eagle 5 transport flying.obj");
		//Spatial ship = assetManager.loadModel("Models/eagle 5 transport/eagle 5 transport/eagle 5 transport flying.obj");
		ship.move(0f, -1f, 0f);
		ship.rotate(0, 90 * FastMath.DEG_TO_RAD, 0); // was 180
		ship.scale(0.02f);
		
		ship.setModelBound(new BoundingBox());
		ship.updateModelBound();

		this.attachChild(ship);
		this.generateLODs();

	}


}
