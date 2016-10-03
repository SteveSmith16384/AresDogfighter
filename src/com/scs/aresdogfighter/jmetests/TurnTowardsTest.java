package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.model.FederationInterceptor;

public class TurnTowardsTest extends SimpleApplication {

	private FederationInterceptor ship1, ship2;

	public static void main(String[] args){
		TurnTowardsTest app = new TurnTowardsTest();
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		setupLight();

		// Bloom
		/*FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
		BloomFilter bloom = new BloomFilter();
		bloom.setBlurScale(5f);
		bloom.setBloomIntensity(2f);
		fpp.addFilter(bloom);
		viewPort.addProcessor(fpp);
		 */

		ship1 = new FederationInterceptor(assetManager);
		ship1.setLocalTranslation(5f, 2f, 0f);
		this.rootNode.attachChild(ship1);

		ship2 = new FederationInterceptor(assetManager);
		ship2.setLocalTranslation(-5f, -2f, 0f);
		this.rootNode.attachChild(ship2);

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		rootNode.updateGeometricState();

		this.flyCam.setMoveSpeed(2.5f);
		cam.setLocation(new Vector3f(-15, 0, 0));
		cam.lookAt(ship1.getLocalTranslation(), Vector3f.UNIT_Y); // Look at something at start
		cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), .1f, 1000);

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

		/*PointLight lamp_light = new PointLight();
		lamp_light.setColor(ColorRGBA.Yellow);
		lamp_light.setRadius(2f);
		lamp_light.setPosition(new Vector3f(0f, 0f, 0f));
		rootNode.addLight(lamp_light);*/

		DirectionalLight dirlight = new DirectionalLight();
		rootNode.addLight(dirlight);

	}


	@Override
	public void simpleUpdate(float tpf) {
		//JMEFunctions.TurnTowards(ship1, ship2.getWorldTranslation(), .01f);
		JMEFunctions.TurnTowards_Gentle(ship1, cam.getLocation(), 0.01f);
	}

}
