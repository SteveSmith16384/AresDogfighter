package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.scs.aresdogfighter.model.StarDust;

public class StardustTest extends SimpleApplication {

	public static void main(String[] args){
		StardustTest app = new StardustTest();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		// Start the StarDust
		StarDust starDust = new StarDust("StarDust", 1000, 700f, cam, assetManager);
		rootNode.attachChild(starDust);		
	}


}
