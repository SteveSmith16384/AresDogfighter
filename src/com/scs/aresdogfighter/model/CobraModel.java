package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.JMEFunctions;

public class CobraModel extends AbstractModel {

	public CobraModel(AssetManager assetManager) {
		super("CobraModel");

		Spatial ship = JMEFunctions.LoadModel(assetManager, "Models/Cobra/cobra1_redux.obj");

		ship.move(0f, 0f, 0f);
		ship.scale(0.1f);

		ship.setModelBound(new BoundingSphere());
		ship.updateModelBound();

		this.attachChild(ship);

	}


}
