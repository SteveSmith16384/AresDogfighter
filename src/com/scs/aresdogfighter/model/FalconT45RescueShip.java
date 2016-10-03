package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.JMEFunctions;

public class FalconT45RescueShip extends AbstractModel {

	public FalconT45RescueShip(AssetManager assetManager) {
		super("FalconT45RescueShip");

		Spatial ship = JMEFunctions.LoadModel(assetManager, "Models/Falcon t45 Rescue ship/Falcon t45 Rescue ship/Falcon t45 Rescue ship flying.obj");
		//Spatial ship = assetManager.loadModel("Models/Falcon t45 Rescue ship/Falcon t45 Rescue ship/Falcon t45 Rescue ship flying.obj");
		ship.move(0f, -1f, 0f);
		//ship.rotate(0, 180 * FastMath.DEG_TO_RAD, 0);
		ship.scale(0.015f);
		
		ship.setModelBound(new BoundingBox());
		ship.updateModelBound();

		//ship.setUserData("id", id);
		
		this.attachChild(ship);
		
		this.generateLODs();

	}


}
