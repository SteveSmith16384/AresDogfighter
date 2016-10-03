package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bounding.BoundingSphere;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;

// http://tf3dm.com/3d-model/esa-tardis-figr-station-mk3-95100.html
public class SpaceStationTest extends SimpleApplication {

	public static void main(String[] args){
		SpaceStationTest app = new SpaceStationTest();
		app.start();
	}


	@Override
	public void simpleInitApp() {
		inputManager.setCursorVisible(true);

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

		Spatial ship = assetManager.loadModel("Models/TARDIS-FIGR_mkIII_station/TARDIS-FIGR_mkIII_station.obj");
		ship.scale(0.01f);

		ship.setModelBound(new BoundingSphere());
		ship.updateModelBound();
		this.rootNode.attachChild(ship);

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		this.flyCam.setMoveSpeed(2.5f);
		this.cam.setLocation(ship.getWorldTranslation().add(0, 0, -10f)); // Point us at a ship but move back
		this.cam.lookAt(ship.getWorldTranslation(), Vector3f.UNIT_Y);
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
		//ship1.getNode().rotate(0, FastMath.DEG_TO_RAD, 0);
		//cam.lookAt(this.ship1.getNode().getWorldTranslation(), Statics.V_UP);
		//cam.update();

	}	


}

