package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class KeysTest extends SimpleApplication {

	private static final String CMD_FWD = "fwd";
	private static final String CMD_SHOOT = "shoot";

	public static void main(String[] args){
		KeysTest app = new KeysTest();
		app.start();
	}


	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		// Create red transparent material	    
		Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", new ColorRGBA(1, 0, 0, 0.5f)); // 0.5f is the alpha value

		// Activate the use of the alpha channel
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

		// Create rectangle of size 10x10
		Geometry mouseRect = new Geometry("MouseRect", new Quad(10, 10));
		//mouseRect.move(100, 100, 0);
		mouseRect.setMaterial(mat);
		guiNode.attachChild(mouseRect);

		Geometry rect = new Geometry("rect", new Quad(100, 100));
		rect.move(0, 0, 0);
		rect.setMaterial(mat);
		guiNode.attachChild(rect);

		inputManager.setCursorVisible(true);

		inputManager.addMapping(CMD_FWD, new KeyTrigger(KeyInput.KEY_F));
		inputManager.addMapping(CMD_SHOOT, new KeyTrigger(KeyInput.KEY_X));
		inputManager.addMapping("mouse", new MouseAxisTrigger(MouseInput.AXIS_WHEEL,false));
		inputManager.addMapping("mouse", new MouseAxisTrigger(MouseInput.AXIS_WHEEL,true));
		
		inputManager.addListener(actionListener, CMD_FWD);
		inputManager.addListener(analogListener, CMD_SHOOT);
		inputManager.addListener(analogListener, "mouse");

		guiNode.updateGeometricState();

	}


	public void simpleUpdate(float tpf) {
		inputManager.setCursorVisible(true);

		// Move the rectangle to the cursor position
		Vector2f cursor = inputManager.getCursorPosition();
		guiNode.getChild("MouseRect").setLocalTranslation(cursor.x, cursor.y, 0);
		//System.out.println("pos:" + cursor.x + "," + cursor.y);
	}


	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean keyPressed, float tpf) {
			if (name.equals(CMD_FWD)) {
				System.out.println(name + " pressed (" + keyPressed + ")");
			}
		}
	};

	private AnalogListener analogListener = new AnalogListener() {
		public void onAnalog(String name, float value, float tpf) {
			System.out.println(name + " pressed (" + value + ")");
		}
	};

}
