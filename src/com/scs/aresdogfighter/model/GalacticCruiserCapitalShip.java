package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.JMEFunctions;

public class GalacticCruiserCapitalShip extends AbstractModel {

	public GalacticCruiserCapitalShip(AssetManager assetManager) {
		super("GalacticCruiserCapitalShip");

		Spatial ship = JMEFunctions.LoadModel(assetManager, "Models/GalacticCruiser/Class II Gallactic Cruiser.obj");

		ship.move(0f, -10f, 0f);
		ship.scale(0.1f);

		ship.setModelBound(new BoundingBox());
		ship.updateModelBound();

		//ship.setUserData("id", id);
		this.attachChild(ship);
		this.generateLODs();

	}


}
