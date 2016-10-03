package com.scs.aresdogfighter.model.robot;

import com.jme3.asset.AssetManager;
import com.scs.aresdogfighter.model.AbstractModel;
import com.scs.aresdogfighter.shapes.MyBox;

public class DefaultRobotModel extends AbstractModel {

	public DefaultRobotModel(AssetManager assetManager) {
		super("DefaultRobotModel");

		MyBox box = new MyBox(1, 1, 1, "DefaultRobotModel", assetManager, "cargo.png", 1, 1);
		//box.setLocalTranslation(0, 0, 0f);//.5f,  -.5f);
		this.attachChild(box);

		
	}

	
}
