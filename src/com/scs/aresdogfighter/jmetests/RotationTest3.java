package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.scs.aresdogfighter.JMEFunctions;

public class RotationTest3 extends SimpleApplication {

	private Geometry geom;
	private Node pivot_x, pivot_z;

	public static void main(String[] args){
		RotationTest3 app = new RotationTest3();
		app.start();
	}


	@Override
	public void simpleInitApp() {
		AmbientLight al = new AmbientLight();
		rootNode.addLight(al);
		DirectionalLight dirlight = new DirectionalLight();
		rootNode.addLight(dirlight);
		
		pivot_x = new Node("PivotX");
		rootNode.attachChild(pivot_x);
		pivot_z = new Node("PivotZ");
		pivot_x.attachChild(pivot_z);
		
		Box box = new Box(1f, .25f, 3f);
		box.setBound(new BoundingBox());
		box.updateBound();
		geom = new Geometry();
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		geom.setMesh(box);
		geom.setMaterial(mat);
		pivot_z.attachChild(geom);

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		// Rotate by 90 degrees on x-axis
		Quaternion qx = new Quaternion();
		qx.fromAngles(FastMath.DEG_TO_RAD*90, 0, 0); // Point straight up

		Quaternion qz = new Quaternion();
		qz.fromAngles(0, 0, FastMath.DEG_TO_RAD*90); // rotate round by 90 degrees

		Quaternion qx2 = new Quaternion();
		qx2.fromAngles(-FastMath.DEG_TO_RAD*90, 0, 0); // It's now lying on its side

		pivot_x.getLocalRotation().multLocal(qx.mult(qz.mult(qx2)));

	}


	@Override
	public void simpleUpdate(float tpf) {

	}

}
