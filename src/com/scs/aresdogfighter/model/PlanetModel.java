package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.shapes.MyDisk;
import com.scs.aresdogfighter.shapes.MySphere;

public class PlanetModel extends AbstractModel {

	public PlanetModel(AssetManager assetManager, float diam, boolean ring, String tex) {
		super("PlanetModel");

		MySphere planet2 = new MySphere(diam, (int)diam*3, (int)diam*3, "planet", assetManager, tex);
		this.attachChild(planet2);

		if (ring) {
			MyDisk disk = new MyDisk(20, diam * 1.5f, 1f, "Ring", assetManager,  "sand2.png");
			this.attachChild(disk);
		}

	}

}
