package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapFont;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.model.HUDModelWithConsole;


public class HUD3DTest3 extends SimpleApplication {
	
	private Node cam_node;

	public static void main(String[] args){
		HUD3DTest3 app = new HUD3DTest3();
		//app.showSettings = false;
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		BitmapFont guiFont_small = assetManager.loadFont("Interface/Fonts/Console.fnt");

        setupLight();
		
		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		Node hud_node = new HUDModelWithConsole(assetManager, guiFont);
		hud_node.scale(.3f);
		//hud_node.setLocalTranslation(-1.5f, -1, 2);
		hud_node.setLocalTranslation(-1.34f, -.75f, 2.5f);
		cam_node = new Node("node");
		this.cam_node.attachChild(hud_node);

	/*	// Console1
		TextArea textarea = new TextArea("textarea", guiFont, 5, "Can you read this?", false);
		textarea.scale(.005f);
		//textarea.setLocalTranslation(-.5f, 0, 3);
		textarea.setLocalTranslation(-.2f, -.4f, 2.4f);
		//textarea.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		textarea.rotate(new Quaternion().fromAngleAxis(-FastMath.PI*1.25f, Vector3f.UNIT_Y));
		cam_node.attachChild(textarea);
		
		// Console2
		TextArea textarea2 = new TextArea("textarea", super.guiFont, 5, "2Can you read this?2", false);
		textarea2.scale(.005f);
		//textarea2.setLocalTranslation(-.5f, 0, 3);
		//textarea2.setLocalTranslation(0.6f, -.4f, 1.4f);
		textarea2.setLocalTranslation(0.7f, -.4f, 1.8f);
		//textarea2.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		textarea2.rotate(new Quaternion().fromAngleAxis(FastMath.PI*1.25f, Vector3f.UNIT_Y));
		cam_node.attachChild(textarea2);
		*/
		this.rootNode.attachChild(cam_node);
	
		rootNode.updateGeometricState();

		this.flyCam.setMoveSpeed(2.5f);
		cam.setLocation(new Vector3f(0, 0, -10f));
		cam.lookAt(rootNode.getLocalTranslation(), Vector3f.UNIT_Y); // Look at something at start
		cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), .01f, 1000);

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

		cam_node.setLocalTranslation(cam.getLocation());
		cam_node.setLocalRotation(cam.getRotation());

	}

}


