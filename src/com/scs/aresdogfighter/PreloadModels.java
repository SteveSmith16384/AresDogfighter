package com.scs.aresdogfighter;

import com.jme3.asset.AssetManager;
import com.scs.aresdogfighter.model.CobraModel;
import com.scs.aresdogfighter.model.Eagle5Transport;
import com.scs.aresdogfighter.model.FederationInterceptor;
import com.scs.aresdogfighter.model.KameriExplorer;
import com.scs.aresdogfighter.model.MissileModel2;
import com.scs.aresdogfighter.model.OrbiterBugship;
import com.scs.aresdogfighter.model.SimpleModel;

public class PreloadModels extends Thread {

	private AssetManager assetManager;
	private static boolean already_loaded = false;

	public PreloadModels(AssetManager _assetManager) {
		super("PreloadModels");

		this.setDaemon(true);

		assetManager = _assetManager;
	}


	public void run() {
		if (!already_loaded) {
			if (Statics.USE_LOWPOLY) {
				new CobraModel(assetManager);
				SimpleModel.GetAsteroid1(assetManager);
				SimpleModel.GetAsteroid2(assetManager);
				SimpleModel.GetAsteroid3(assetManager);
				SimpleModel.GetBigAsteroid(assetManager);
				SimpleModel.GetBiggerAsteroid(assetManager);
				SimpleModel.GetPlanet3(assetManager);
				SimpleModel.GetCroissantShip(assetManager);
			} else {
				new KameriExplorer(assetManager);
				new Eagle5Transport(assetManager);
				new FederationInterceptor(assetManager);
				new OrbiterBugship(assetManager);
				new MissileModel2(assetManager);
			}
			already_loaded = true;
		}
	}

}
