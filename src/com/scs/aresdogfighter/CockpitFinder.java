package com.scs.aresdogfighter;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bounding.BoundingBox;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.model.SimpleModel;

public class CockpitFinder extends SimpleApplication {

	public static void main(String[] args){
		CockpitFinder app = new CockpitFinder();
		app.showSettings = false;
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default
		//cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), .1f, 1000);
		cam.setFrustumPerspective(Statics.FOV_ANGLE, settings.getWidth() / settings.getHeight(), .1f, Statics.CAM_VIEW_DIST);

		setupLight();
		
		//Spatial airplaneModel = new FalconT45RescueShip(assetManager, null);
		//Spatial airplaneModel = new Eagle5Transport(assetManager, null);
		//Spatial airplaneModel = new OrbiterBugship(assetManager, null);
		//Spatial airplaneModel = new KameriExplorer(assetManager);
		//Spatial airplaneModel = new FederationInterceptor(assetManager, null);
		//JMEFunctions.loadModel(assetManager, "Models/missile/missile.obj");
		
		//Spatial airplaneModel = new GalacticCruiserCapitalShip(assetManager);
		//Spatial airplaneModel = new FuryStarshuttle(assetManager, null);
		//Spatial airplaneModel = new CargoTransport(assetManager, null);
		//Spatial airplaneModel = new BattleCruiserCapitalShip(assetManager, null);
		//Spatial airplaneModel = new CobraModel(assetManager);
		//Spatial airplaneModel = new MySpaceStationModel(assetManager);
		//Spatial airplaneModel = new UFOModel(assetManager);
		
		//Spatial airplaneModel = SimpleModel.GetSpaceShip(assetManager);//new SimpleModel(assetManager, "Quaternius/Planet3.obj");
		//Spatial airplaneModel = SimpleModel.GetPlanet3(assetManager);
		//Spatial airplaneModel = SimpleModel.GetAsteroid1(assetManager);
		//Spatial airplaneModel = SimpleModel.GetBiggerAsteroid(assetManager);
		//Spatial airplaneModel = SimpleModel.GetLongSpaceship(assetManager);
		//Spatial airplaneModel = SimpleModel.GetMoon(assetManager);
		Spatial airplaneModel = SimpleModel.GetCroissantShip(assetManager);
		
		airplaneModel.setModelBound(new BoundingBox());
		airplaneModel.updateModelBound();

		//airplaneModel.setLocalTranslation(0, 0, 5);
		rootNode.attachChild(airplaneModel);

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));
		
		this.flyCam.setMoveSpeed(3.5f);
		//cam.update();
		
		rootNode.updateGeometricState();

	}


	private void setupLight() {
		// Remove existing lights
		this.rootNode.getWorldLightList().clear();
		LightList list = this.rootNode.getWorldLightList();
		for (Light it : list) {
			this.rootNode.removeLight(it);
		}

		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(2.5f));
		rootNode.addLight(al);

		DirectionalLight dirlight = new DirectionalLight(); // FSR need this for textures to show
		//dirlight.set
		rootNode.addLight(dirlight);

	}


	@Override
	public void simpleUpdate(float tpf) {
		//System.out.println("Pos: " + this.cam.getLocation());
		//this.rootNode.rotate(0,  tpf,  tpf);
	}

	
}
