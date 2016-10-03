package com.scs.aresdogfighter.shapes;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

public class MyDisk extends Geometry {

	public MyDisk(int sides, float outerRadius, float thickness, String name, AssetManager assetManager, String tex) {
		super(name);

		Ring ring = new Ring(sides, outerRadius, thickness);
		ring.setBound(new BoundingSphere());
		ring.updateBound();
		this.setMesh(ring);

		Texture t = assetManager.loadTexture("Textures/" + tex);
		t.setWrap(WrapMode.Repeat);
		
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		mat.setTexture("DiffuseMap", t);
		mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.FrontAndBack);
		
		this.setMaterial(mat);
	}


}
