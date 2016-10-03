package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.JMEFunctions;

public class SimpleModel extends AbstractModel {

	public static SimpleModel GetAsteroid1(AssetManager assetManager) {
		return new SimpleModel(assetManager, "Quaternius/Asteroid1.obj");
	}
	
	public static SimpleModel GetAsteroid2(AssetManager assetManager) {
		return new SimpleModel(assetManager, "Quaternius/Asteroid2.obj");
	}
	
	public static SimpleModel GetAsteroid3(AssetManager assetManager) {
		return new SimpleModel(assetManager, "Quaternius/Asteroid3.obj");
	}
	
	public static SimpleModel GetBigAsteroid(AssetManager assetManager) {
		return new SimpleModel(assetManager, "Quaternius/BigAsteroid.obj");
	}
	
	public static SimpleModel GetBiggerAsteroid(AssetManager assetManager) {
		return new SimpleModel(assetManager, "Quaternius/BiggerAsteroid.obj");
	}
	
	public static SimpleModel GetMoon(AssetManager assetManager) {
		return new SimpleModel(assetManager, "Quaternius/Moon.obj");
	}
	
	public static SimpleModel GetPlanet3(AssetManager assetManager) {
		return new SimpleModel(assetManager, "Quaternius/Planet3.obj");
	}
	
	public static SimpleModel GetSpaceShip(AssetManager assetManager) {
		return new SimpleModel(assetManager, "Quaternius/SpaceShip.obj");
	}

	// SHips
	
	public static SimpleModel GetLongSpaceship(AssetManager assetManager) {
		return new SimpleModel(assetManager, "Quaternius/LongSpaceship.obj");
	}
	
	public static Node GetCroissantShip(AssetManager assetManager) {
		SimpleModel sm = new SimpleModel(assetManager, "Quaternius/CroissantShip.obj");
		sm.getChild(0).rotate(0, FastMath.DEG_TO_RAD*270, 0);
		return  sm;
	}
	
	public static SimpleModel GetMediumShip(AssetManager assetManager) {
		return new SimpleModel(assetManager, "Quaternius/MediumShip.obj");
	}
	
	public static SimpleModel GetBigSpaceship(AssetManager assetManager) {
		return new SimpleModel(assetManager, "Quaternius/BigSpaceship.obj");
	}
	
	private SimpleModel(AssetManager assetManager, String sobj) {
		super("SimpleModel");

		Spatial ship = JMEFunctions.LoadModel(assetManager, "Models/" + sobj);

		ship.setModelBound(new BoundingSphere());
		ship.updateModelBound();

		this.attachChild(ship);

	}


}
