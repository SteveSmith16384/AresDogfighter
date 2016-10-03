package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bounding.BoundingBox;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.model.FederationInterceptor;

public class RotationTest_Airplane extends SimpleApplication implements AnalogListener, ActionListener {

	//private FederationInterceptor airplaneModel;
	private Geometry airplaneModel;
	private Node positionNode;

	public static void main(String[] args){
		RotationTest_Airplane app = new RotationTest_Airplane();
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		setupLight();

		positionNode = new Node("positionNode");
		rootNode.attachChild(positionNode);

		//airplaneModel = new FederationInterceptor(assetManager, null);
		Box box = new Box(1f, .25f, 3f);
		box.setBound(new BoundingBox());
		box.updateBound();
		airplaneModel = new Geometry();
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		airplaneModel.setMesh(box);
		airplaneModel.setMaterial(mat);

		positionNode.attachChild(airplaneModel);

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		this.flyCam.setMoveSpeed(2.5f);
		cam.setLocation(new Vector3f(0, 0, -15));
		cam.lookAt(airplaneModel.getLocalTranslation(), Vector3f.UNIT_Y); // Look at something at start
		cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), .1f, 1000);

		this.inputManager.clearMappings();

		inputManager.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("D", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addListener(this, "W", "A", "S", "D");

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
		
	}


	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (isPressed) {
			// Do nothing
		}
	}


	@Override
	public void onAnalog(String name, float value, float tpf) {
		if (name.equalsIgnoreCase("W")) {
			//x_ang -= tpf * TURN_SPEED;
			airplaneModel.rotate(new Quaternion().fromAngleAxis(1 * tpf, Vector3f.UNIT_X));
		} else if (name.equalsIgnoreCase("S")) {
			airplaneModel.rotate(new Quaternion().fromAngleAxis(-1 * tpf, Vector3f.UNIT_X));
			//x_ang += tpf * TURN_SPEED;
		} else if (name.equalsIgnoreCase("A")) {
			//z_ang += tpf * TURN_SPEED;
			airplaneModel.rotate(new Quaternion().fromAngleAxis(-3 * tpf, Vector3f.UNIT_Z));
		} else if (name.equalsIgnoreCase("D")) {
			airplaneModel.rotate(new Quaternion().fromAngleAxis(3 * tpf, Vector3f.UNIT_Z));
			//z_ang -= tpf * TURN_SPEED;
		}


	}


}
