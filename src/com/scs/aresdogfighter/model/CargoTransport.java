package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.JMEFunctions;

public class CargoTransport extends AbstractModel {

	public CargoTransport(AssetManager assetManager) {
		super("CargoTransport");

		Spatial ship = JMEFunctions.LoadModel(assetManager, "Models/CargoTransport/cargo transport 3.obj");

		ship.move(0f, 0f, 0f);
		ship.scale(0.01f);

		ship.setModelBound(new BoundingSphere());
		ship.updateModelBound();

		//ship.setUserData("id", id);
		this.attachChild(ship);

	}


}
