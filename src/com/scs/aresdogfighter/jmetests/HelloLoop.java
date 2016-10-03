package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/** Sample 4 - how to trigger repeating actions from the main event loop.
 * In this example, you use the loop to make the player character
 * rotate continuously. */
public class HelloLoop extends SimpleApplication {

	public static void main(String[] args){
		HelloLoop app = new HelloLoop();
		app.start();
	}

	protected Geometry player;

	@Override
	public void simpleInitApp() {
		/** this blue box is our player character */
		Box b = new Box(1, 1, 1);
		player = new Geometry("blue cube", b);
		Material mat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Blue);
		player.setMaterial(mat);
		rootNode.attachChild(player);
	}

	/* Use the main event loop to trigger repeating actions. */
	@Override
	public void simpleUpdate(float tpf) {
		// make the player rotate:
		player.rotate(0, 2*tpf, 0);
		System.out.println("tpf=" + tpf);
	}
}