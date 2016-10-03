package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.JMEFunctions;

public class BattleCruiser extends AbstractModel {

	public BattleCruiser(AssetManager assetManager) {
		super(BattleCruiser.class.getSimpleName());

		Spatial ship = JMEFunctions.LoadModel(assetManager, "Models/BattleCruiser/HN t42 Envorian Battlecruiser.obj");

		ship.move(0f, 0f, 0f);
		ship.scale(0.01f);

		ship.setModelBound(new BoundingBox());
		ship.updateModelBound();

		//ship.setUserData("id", id);
		this.attachChild(ship);
		this.generateLODs();

	}


}
