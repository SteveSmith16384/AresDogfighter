package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.model.FederationInterceptor;

public class RotationTest extends SimpleApplication implements AnalogListener, ActionListener {

	private static final float TURN_SPEED = 5f;

	private float x_ang = 91, z_ang = 1;
	private FederationInterceptor ship1;
	private Node pivot_x, pivot_z;

	public static void main(String[] args){
		RotationTest app = new RotationTest();
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		setupLight();
		
		pivot_x = new Node("PivotX");
		rootNode.attachChild(pivot_x);
		pivot_z = new Node("PivotZ");
		pivot_x.attachChild(pivot_z);
		
		ship1 = new FederationInterceptor(assetManager);
		//ship1.setLocalTranslation(0f, 0f, 0f);
		pivot_z.attachChild(ship1);

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		//rootNode.updateGeometricState();

		this.flyCam.setMoveSpeed(2.5f);
		cam.setLocation(new Vector3f(0, 0, -15));
		cam.lookAt(ship1.getLocalTranslation(), Vector3f.UNIT_Y); // Look at something at start
		cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), .1f, 1000);

		//ship1.lookAt(cam.getLocation(), Vector3f.UNIT_Y);
		//this.rotX();
		//this.rotZ();

		this.inputManager.clearMappings();

		inputManager.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("D", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addListener(this, "W", "A", "S", "D");

		rot();
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
		rot();
	}


	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (isPressed) {
			// Do nothing
		}
	}


	@Override
	public void onAnalog(String name, float value, float tpf) {
		//roll = 0;
		if (name.equalsIgnoreCase("W")) {
			x_ang -= tpf * TURN_SPEED;
			//this.rotX();
		} else if (name.equalsIgnoreCase("S")) {
			//this.players_ship.getSpatial().rotate(-TURN_SPEED*tpf, 0, 0);
			x_ang += tpf * TURN_SPEED;
			//this.rotX();
		} else if (name.equalsIgnoreCase("A")) {
			//this.players_ship.getSpatial().rotate(0, 0, TURN_SPEED*tpf);
			z_ang += tpf * TURN_SPEED;
			//this.rotZ();
			//roll = -1;
		} else if (name.equalsIgnoreCase("D")) {
			//this.players_ship.getSpatial().rotate(0, 0, -TURN_SPEED*tpf);
			z_ang -= tpf * TURN_SPEED;
			//this.rotZ();
			//roll = 1;
		}
		
		rot();
		
	}

	
	private void rot() {
		Quaternion qx = new Quaternion();
		qx.fromAngles(x_ang*FastMath.DEG_TO_RAD, 0, 0);
		Quaternion qz = new Quaternion();
		qz.fromAngles(0, 0, z_ang*FastMath.DEG_TO_RAD);
		//Quaternion q = qx.mult(qz);
		//pivot_x.rotate(q);
		pivot_x.getLocalRotation().multLocal(qx.mult(qz));
		
		System.out.println("pivot_x:" + x_ang + "/" + z_ang);


	}



}
