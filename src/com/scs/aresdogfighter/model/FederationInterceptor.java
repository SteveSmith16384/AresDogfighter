package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.JMEFunctions;

public class FederationInterceptor extends AbstractModel {

	public FederationInterceptor(AssetManager assetManager) {
		super("FederationInterceptor");

		Spatial ship = JMEFunctions.LoadModel(assetManager, "Models/FederationInterceptor/Federation Interceptor HN48 flying.obj");

		ship.move(0f, -1f, 0f);
		ship.scale(0.01f);

	    //CollisionShape sceneShape1 = CollisionShapeFactory.createMeshShape(ship);

		ship.setModelBound(new BoundingBox());
		ship.updateModelBound();

		//ship.setUserData("id", id);
		this.attachChild(ship);
		this.generateLODs();

	}


}
