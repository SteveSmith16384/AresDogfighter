package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.controllers.CameraController;
import com.scs.aresdogfighter.model.PlanetModel;

public class CameraTest extends SimpleApplication {

	private PlanetModel planet1, planet2;
	private CameraController cam_cont;
	
	public static void main(String[] args){
		CameraTest app = new CameraTest();
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		setupLight();

		planet1 = new PlanetModel(this.getAssetManager(), 30f, true, "sand2.png"); //new MySphere(5f, 30, 30, "planet", this.getAssetManager(), "sand2.png");
		planet1.setLocalTranslation(5f, 0f, 0f);
		this.rootNode.attachChild(planet1);

		planet2 = new PlanetModel(this.getAssetManager(), 30f, true, "sand2.png"); //new MySphere(5f, 30, 30, "planet", this.getAssetManager(), "sand2.png");
		planet2.setLocalTranslation(-5f, 0f, 0f);
		this.rootNode.attachChild(planet2);

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		rootNode.updateGeometricState();

		this.flyCam.setMoveSpeed(2.5f);
		cam.setLocation(new Vector3f(0, 0, 30));
		cam.lookAt(planet2.getLocalTranslation(), Vector3f.UNIT_Y); // Look at something at start
		cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), .1f, 1000);

		cam_cont = new CameraController(cam);
		//cam_cont.setTarget(planet1);
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

	}


	@Override
	public void simpleUpdate(float tpf) {
		//inputManager.setCursorVisible(true);

		planet1.rotate(FastMath.DEG_TO_RAD, 0, 0);

		planet2.rotate(FastMath.DEG_TO_RAD, 0, 0);
		
		cam_cont.process(tpf);

	}

}
