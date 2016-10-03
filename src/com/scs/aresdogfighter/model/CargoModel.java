package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.scs.aresdogfighter.shapes.MyBox;

public class CargoModel extends AbstractModel {

	public CargoModel(AssetManager assetManager) {
		super("CargoModel");

		MyBox box = new MyBox(1, 1, 1, "Cargo", assetManager, "cargo.png", 1, 1);
		//box.setLocalTranslation(0, 0, 0f);//.5f,  -.5f);
		this.attachChild(box);

		
	}

	
}
