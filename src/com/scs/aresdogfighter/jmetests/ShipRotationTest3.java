package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.model.FederationInterceptor;

/**
 * Ship should tilt and turn towards camera.
 *
 */
public class ShipRotationTest3 extends SimpleApplication {

	private static final float TURN_SPEED = 1f;

	private FederationInterceptor ship1;
	private Node up, down, left, right;

	public static void main(String[] args){
		ShipRotationTest3 app = new ShipRotationTest3();
		app.setPauseOnLostFocus(false);
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		setupLight();

		ship1 = new FederationInterceptor(assetManager);
		//ship1.setLocalTranslation(5f, 2f, 0f);
		this.rootNode.attachChild(ship1);

		up = new Node("up");
		up.setLocalTranslation(0, 2f, 0);
		ship1.attachChild(up);
		down = new Node("down");
		down.setLocalTranslation(0, -2f, 0);
		ship1.attachChild(down);
		left = new Node("left");
		left.setLocalTranslation(-2f, 0, 0);
		ship1.attachChild(left);
		right = new Node("right");
		right.setLocalTranslation(2f, 0, 0);
		ship1.attachChild(right);


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

		DirectionalLight dirlight = new DirectionalLight();
		rootNode.addLight(dirlight);

	}


	@Override
	public void simpleUpdate(float tpf) {
		//inputManager.setCursorVisible(true);

		// Tilt left/right
		float dist_left = cam.getLocation().distance(left.getWorldTranslation());
		float dist_right = cam.getLocation().distance(right.getWorldTranslation());
		float lr_diff = Math.abs(dist_left-dist_right);
		if (lr_diff > 0.1f) {
			if (dist_left > dist_right) {
				//System.out.println("Left!");
				ship1.rotate(new Quaternion().fromAngleAxis(-1 * tpf * TURN_SPEED, Vector3f.UNIT_Z));
			} else {
				//System.out.println("Right!");
				ship1.rotate(new Quaternion().fromAngleAxis(1 * tpf * TURN_SPEED, Vector3f.UNIT_Z));
			}
		} else { // Almost facing it
			// check if facing
			//Vector3f dir_to_target = cam.getLocation().subtract(ship1.getWorldTranslation()).normalizeLocal();
			Quaternion target_q = new Quaternion();
			target_q.lookAt(cam.getLocation(), ship1.getWorldRotation().mult(Vector3f.UNIT_Z));
			target_q.normalizeLocal();
			Quaternion our_q = ship1.getWorldRotation().clone();
			our_q.normalizeLocal();
			float dot = our_q.mult(target_q).toAngleAxis(null);
			System.out.println("Dot=" + dot);
			if (dot < 90.99f) {
				// Rotate to face it
				float dist_up = cam.getLocation().distance(up.getWorldTranslation());
				float dist_down = cam.getLocation().distance(down.getWorldTranslation());
				if (dist_up > dist_down) {
					ship1.rotate(new Quaternion().fromAngleAxis(1 * tpf, Vector3f.UNIT_X));
				} else {
					ship1.rotate(new Quaternion().fromAngleAxis(-1 * tpf, Vector3f.UNIT_X));
				}
			}
		}
	}

}


