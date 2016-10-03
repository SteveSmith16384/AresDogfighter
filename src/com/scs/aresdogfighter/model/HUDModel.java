package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.shapes.MyCylinder;

/*
 * 
 *     0     3 4  5 6      9
 *     |     | |  | |      |
 *5_   1_____        ______2
 *4_         \3____4/
 *           / 5    \
 *2_   8____/6      7\______9
 *    
 *0_   10________11_______12
 * 
 */
public class HUDModel extends Node {

	private static final float RAD = 0.06f;
	private static final float Z_OFF = -2f;
	private static final String TEX = "spacewall.png";

	public HUDModel(AssetManager assetManager) {
		super("HUDModel");
		
		Node rootNode = this;
		
		MyCylinder cyl1 = new MyCylinder(assetManager, new Vector3f(0, 5, Z_OFF), new Vector3f(3, 5, 0), 4, 4, RAD, TEX);
		rootNode.attachChild(cyl1);
		
		MyCylinder cyl2 = new MyCylinder(assetManager, new Vector3f(6, 5, 0), new Vector3f(9, 5, Z_OFF), 4, 4, RAD, TEX);
		rootNode.attachChild(cyl2);
		
		MyCylinder cyl3 = new MyCylinder(assetManager, new Vector3f(3, 5, 0), new Vector3f(4, 4, 0), 4, 4, RAD, TEX);
		rootNode.attachChild(cyl3);
		
		MyCylinder cyl4 = new MyCylinder(assetManager, new Vector3f(6, 5, 0), new Vector3f(5, 4, 0), 4, 4, RAD, TEX);
		rootNode.attachChild(cyl4);
		
		MyCylinder cyl5 = new MyCylinder(assetManager, new Vector3f(4, 4, 0), new Vector3f(5, 4, 0), 4, 4, RAD, TEX);
		rootNode.attachChild(cyl5);
		
		MyCylinder cyl6 = new MyCylinder(assetManager, new Vector3f(4, 4, 0), new Vector3f(3, 2, 0), 4, 4, RAD, TEX);
		rootNode.attachChild(cyl6);
		
		MyCylinder cyl7 = new MyCylinder(assetManager, new Vector3f(5, 4, 0), new Vector3f(6, 2, 0), 4, 4, RAD, TEX);
		rootNode.attachChild(cyl7);
		
		MyCylinder cyl8 = new MyCylinder(assetManager, new Vector3f(0, 2, Z_OFF), new Vector3f(3, 2, 0), 4, 4, RAD, TEX);
		rootNode.attachChild(cyl8);
		
		MyCylinder cyl9 = new MyCylinder(assetManager, new Vector3f(6, 2, 0), new Vector3f(9, 2, Z_OFF), 4, 4, RAD, TEX);
		rootNode.attachChild(cyl9);
		
		//MyCylinder cyl10 = new MyCylinder(assetManager, new Vector3f(0, 1, Z_OFF), new Vector3f(9, 1, Z_OFF), 4, 4, RAD, TEX);
		//rootNode.attachChild(cyl10);
		
		MyCylinder cyl10 = new MyCylinder(assetManager, new Vector3f(0, 1, Z_OFF), new Vector3f(3, 1, 0), 4, 4, RAD, TEX);
		rootNode.attachChild(cyl10);
		MyCylinder cyl11 = new MyCylinder(assetManager, new Vector3f(3, 1, 0), new Vector3f(6, 1, 0), 4, 4, RAD, TEX);
		rootNode.attachChild(cyl11);
		MyCylinder cyl12 = new MyCylinder(assetManager, new Vector3f(6, 1, 0), new Vector3f(9, 1, Z_OFF), 4, 4, RAD, TEX);
		rootNode.attachChild(cyl12);
	}

}
