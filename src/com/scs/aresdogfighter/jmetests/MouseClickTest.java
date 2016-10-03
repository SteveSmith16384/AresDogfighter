package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.shapes.MySphere;

public class MouseClickTest extends SimpleApplication {

	MySphere planet2;

	public static void main(String[] args){
		MouseClickTest app = new MouseClickTest();
		app.start();
	}


	private AnalogListener analogListener = new AnalogListener() {
		public void onAnalog(String name, float intensity, float tpf) {
			if (name.equals("pick target")) {
				// Reset results list.
				CollisionResults results = new CollisionResults();
				// Convert screen click to 3d position
				Vector2f click2d = inputManager.getCursorPosition();
				Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
				Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
				// Aim the ray from the clicked spot forwards.
				Ray ray = new Ray(click3d, dir);
				// Collect intersections between ray and all nodes in results list.
				rootNode.collideWith(ray, results);
				// (Print the results so we see what is going on:)
				for (int i = 0; i < results.size(); i++) {
					// (For each, we know distance, impact point, geometry.)
					float dist = results.getCollision(i).getDistance();
					Vector3f pt = results.getCollision(i).getContactPoint();
					String target = results.getCollision(i).getGeometry().getName();
					System.out.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
				}
				// Use the results -- we rotate the selected geometry.
				/*if (results.size() > 0) {
					// The closest result is the target that the player picked:
					Geometry target = results.getClosestCollision().getGeometry();
					// Here comes the action:
					if (target.getName().equals("Red Box")) {
						target.rotate(0, -intensity, 0);
					} else if (target.getName().equals("Blue Box")) {
						target.rotate(0, intensity, 0);
					}
				}*/
			}
		}
	};


	@Override
	public void simpleInitApp() {
		inputManager.setCursorVisible(true);

		inputManager.addMapping("pick target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		this.inputManager.addListener(analogListener, "pick target");

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

		planet2 = new MySphere(5f, 30, 30, "planet", this.getAssetManager(), "sand2.png");
		planet2.setLocalTranslation(0f, 0f, 0f);
		this.rootNode.attachChild(planet2);

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		rootNode.updateGeometricState();

		this.flyCam.setMoveSpeed(2.5f);
		cam.setLocation(new Vector3f(0, -10, 0));
		cam.lookAt(planet2.getLocalTranslation(), Vector3f.UNIT_Y); // Look at something at start
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

	}


	@Override
	public void simpleUpdate(float tpf) {
		inputManager.setCursorVisible(true);

	}	


}


