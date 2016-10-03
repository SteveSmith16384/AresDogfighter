package com.scs.aresdogfighter.gameobjects.robots;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.model.robot.DefaultRobotModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class Robot1 extends GameObject {

	private DefaultRobotModel model;
	
	public Robot1(ManualShipControlModule game, AssetManager assetManager, BitmapFont guiFont) {
		super(game, SpaceEntity.Type.ROBOT, "Robot1", true, guiFont, false, true, true, true, ShowMode.SPACE, true);
		
		model = new DefaultRobotModel(assetManager);
	}

	
	@Override
	public Node getSpatial() {
		return model;
	}
	

	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f point) {
		
	}

}
