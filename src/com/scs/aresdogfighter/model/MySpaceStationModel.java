package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.aresdogfighter.shapes.Ring;
import com.scs.aresdogfighter.shapes.RingEdge;

public class MySpaceStationModel extends Node {

	private static final int RING_SIDES = 30;
	private static final float RING_RAD = 10f;
	private static final float RING_THICKNESS = 2f;
	private static final int NUM_SPOKES = 3;

	public MySpaceStationModel(AssetManager assetManager) {
		super("MySpaceStationModel");

		Texture t = assetManager.loadTexture("Textures/windows.gif");//spacewall.png");
		t.setWrap(WrapMode.Repeat);
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		mat.setTexture("DiffuseMap", t);
		this.setMaterial(mat);

		Ring r1 = new Ring(RING_SIDES, RING_RAD, RING_THICKNESS);
		Geometry gr1 = new Geometry("SSSideRing1");
		gr1.setMesh(r1);
		gr1.setMaterial(mat);
		gr1.move(0, 0, RING_THICKNESS/2);
		this.attachChild(gr1);
		
		Ring r2 = new Ring(RING_SIDES, RING_RAD, RING_THICKNESS);
		Geometry gr2 = new Geometry("SSSideRing2");
		gr2.setMesh(r2);
		gr2.setMaterial(mat);
		gr2.rotate(0, FastMath.DEG_TO_RAD*180, 0);
		gr2.move(0, 0, -RING_THICKNESS/2);
		this.attachChild(gr2);

		// Inner ring
		RingEdge edgemesh_inner = new RingEdge(RING_SIDES, RING_RAD-RING_THICKNESS, RING_THICKNESS, false);
		Geometry edge_inner = new Geometry("SSInnerRing");
		edge_inner.setMesh(edgemesh_inner);
		edge_inner.setMaterial(mat);
		edge_inner.move(0, 0, -RING_THICKNESS/2);
		this.attachChild(edge_inner);

		// Outer ring
		RingEdge edgemesh_outer = new RingEdge(RING_SIDES, RING_RAD, RING_THICKNESS, true);
		Geometry edge_outer = new Geometry("SSOuterRing");
		edge_outer.setMesh(edgemesh_outer);
		edge_outer.setMaterial(mat);
		edge_outer.move(0, 0, -RING_THICKNESS/2);
		//mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		this.attachChild(edge_outer);

		// Centre
		Cylinder centre = new Cylinder(6, 6, RING_THICKNESS*2, RING_THICKNESS*2, true);
		Geometry gcentre = new Geometry("SSCentre");
		gcentre.setMesh(centre);
		gcentre.setMaterial(mat);
		this.attachChild(gcentre);

		// Spokes
		Texture spoke_tex = assetManager.loadTexture("Textures/spoke.jpg");
		spoke_tex.setWrap(WrapMode.Repeat);
		Material spoke_mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		spoke_mat.setTexture("DiffuseMap", spoke_tex);
		this.setMaterial(spoke_mat);

		float ang = 0;
		for (int i=0 ; i<NUM_SPOKES ; i++) {
			Cylinder cyl = new Cylinder(2, 6, RING_THICKNESS/2, (RING_RAD*2)-RING_THICKNESS, false);
			cyl.scaleTextureCoordinates(new Vector2f(10f, 10f));
			Geometry g = new Geometry("SSSpoke_" + i);
			g.setMesh(cyl);
			g.setMaterial(spoke_mat);
			g.rotate(FastMath.DEG_TO_RAD * 90, 0, 0);
			
			cyl.scaleTextureCoordinates(new Vector2f(.1f, .1f));

			g.rotate(0, ang, 0f);
			ang += FastMath.PI/NUM_SPOKES;
			this.attachChild(g);
		}
	}

}
