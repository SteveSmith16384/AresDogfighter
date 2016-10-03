package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;
import com.jme3.texture.Texture;

public class MissileModel extends Geometry {
	
	private Cylinder cyl;
	
	public MissileModel(AssetManager assetManager) {
		super("MissileModel");
		
		cyl = new Cylinder(10, 10, .2f, 2f, true);
		cyl.setBound(new BoundingSphere());
		cyl.updateBound();
		this.setMesh(cyl);
		
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		Texture t = assetManager.loadTexture("Textures/spacewall.png");
		mat.setTexture("DiffuseMap", t);
		//mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
	    this.setMaterial(mat);
	}
	

}
