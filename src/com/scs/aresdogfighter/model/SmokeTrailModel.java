package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;

public class SmokeTrailModel extends AbstractBillboardModel {

	public SmokeTrailModel(AssetManager assetManager, boolean smoke) {
		super(assetManager, "SmokeTrailModel", 1, 1, smoke?"smoke.png":"flame.png");
	}

}
