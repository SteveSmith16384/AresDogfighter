package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.model.BeamLaserModel;
import com.scs.aresdogfighter.shapes.MyBox;

public class LaserTest extends SimpleApplication {
	
	public static void main(String[] args){
		LaserTest app = new LaserTest();
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		setupLight();

		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);        
	    fpp.addFilter(bloom);
	    viewPort.addProcessor(fpp);
		
		MyBox box1 = new MyBox(1f, 1f, 1f, "TestBox", assetManager, "spacewall.png", 1f, 1f);
		box1.move(0, 0, 0);
		this.rootNode.attachChild(box1);
		
		MyBox box2 = new MyBox(1f, 1f, 1f, "TestBox", assetManager, "spacewall.png", 1f, 1f);
		box2.move(5, 0, 0);
		this.rootNode.attachChild(box2);
		
		BeamLaserModel ff = BeamLaserModel.Factory(assetManager, box1.getWorldTranslation(), box2.getWorldTranslation(), ColorRGBA.Pink);//new BeamLaserModel(assetManager, box1.getWorldTranslation(), box2.getWorldTranslation(), ColorRGBA.Pink);
		//ff.lookAt(box2.getWorldTranslation(), Statics.V_UP);
		//ff.move(3.5f, 0, 0);
		this.rootNode.attachChild(ff);

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		rootNode.updateGeometricState();

		this.flyCam.setMoveSpeed(3.5f);
		cam.setLocation(new Vector3f(0, 0, 10));
		cam.lookAt(box1.getLocalTranslation(), Vector3f.UNIT_Y); // Look at something at start
		cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), .1f, 1000);
		cam.update();
		
	}


	private void setupLight() {
		// Remove existing lights
		this.rootNode.getWorldLightList().clear(); //this.rootNode.getWorldLightList().size();
		LightList list = this.rootNode.getWorldLightList();
		for (Light it : list) {
			this.rootNode.removeLight(it);
		}

		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White);
		rootNode.addLight(al);

	}


	@Override
	public void simpleUpdate(float tpf) {
		//inputManager.setCursorVisible(true);


	}

}

