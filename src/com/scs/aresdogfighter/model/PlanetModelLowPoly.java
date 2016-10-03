package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.scs.aresdogfighter.shapes.MyDisk;
import com.scs.aresdogfighter.shapes.MySphere;

public class PlanetModelLowPoly extends AbstractModel {

	public PlanetModelLowPoly(AssetManager assetManager, float diam, boolean ring, String tex) {
		super("PlanetModelLowPoly");

		MySphere planet2 = new MySphere(diam, (int)diam/4, (int)diam/4, "planet", assetManager, tex);
		this.attachChild(planet2);

/*		if (ring) {
			MyDisk disk = new MyDisk(20, diam * 1.5f, 1f, "Ring", assetManager,  "sand2.png");
			this.attachChild(disk);
		}
*/
	}

}
