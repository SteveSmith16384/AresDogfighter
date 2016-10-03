package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;
import com.jme3.audio.Listener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.model.FederationInterceptor;

public class SoundTest extends SimpleApplication {

	private static final float MAX_DIST = 10f;
	
	private AudioNode sound;
	private Listener mylistener;
	
	public static void main(String[] args){
		SoundTest app = new SoundTest();
		app.showSettings = false;
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default
		
		cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), .1f, 1000);

		setupLight();
		//airplaneModel = new FalconT45RescueShip(assetManager, null);
		//airplaneModel = new Eagle5Transport(assetManager, null);
		//airplaneModel = new OrbiterBugship(assetManager, null);
		//airplaneModel = new KameriExplorer(assetManager, null);
		Node airplaneModel = new FederationInterceptor(assetManager);
		rootNode.attachChild(airplaneModel);

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		// Sound
		sound = new AudioNode(getAssetManager(), "Sound/background_ambience.ogg", true);
		sound.setPositional(true);
		sound.setRefDistance(5f);
		sound.setMaxDistance(20f);
		sound.setVolume(1);
		sound.setTimeOffset(0);
		//sound.setPitch(1);
		sound.setDirectional(false);
		sound.setReverbEnabled(false);
		sound.setLooping(true);
		sound.setLocalTranslation(0, 0, 0);
		rootNode.attachChild(sound);
		//sound.setLocalTranslation(airplaneModel.getLocalTranslation());
		sound.play();
		
		this.flyCam.setMoveSpeed(3.5f);
		
		mylistener = new Listener();

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
		al.setColor(ColorRGBA.White);//.mult(2.5f));
		rootNode.addLight(al);

		DirectionalLight dirlight = new DirectionalLight();
		rootNode.addLight(dirlight);

	}


	@Override
	public void simpleUpdate(float tpf) {
		System.out.println("Pos: " + this.cam.getLocation());
		
		//sound.updateGeometricState();
		mylistener.setLocation(cam.getLocation());
		mylistener.setRotation(cam.getRotation());
		
		float dist = cam.getLocation().distance(sound.getWorldTranslation());
		sound.setVolume(1 - (dist/MAX_DIST));
		//listener.
	}

	
}
