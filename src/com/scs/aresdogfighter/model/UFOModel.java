package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.JMEFunctions;

public class UFOModel extends AbstractModel {

	public UFOModel(AssetManager assetManager) {
		super("UFOModel");

		Spatial ship = JMEFunctions.loadModel(assetManager, "Models/UFO/UFO.obj");
		//Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		//mat.setTexture("ColorMap", t);
		//ship.setMaterial(mat);

		ship.setModelBound(new BoundingSphere());
		ship.updateModelBound();

		//ship.setUserData("id", id);
		this.attachChild(ship);
		this.generateLODs();

	}


}
