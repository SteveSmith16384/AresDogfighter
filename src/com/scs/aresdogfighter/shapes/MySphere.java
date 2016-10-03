package com.scs.aresdogfighter.shapes;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.aresdogfighter.IProcessable;

public class MySphere extends Geometry {

	public MySphere(float diam, int zsamples, int radsamples, String name, AssetManager assetManager, String tex) {
		super(name);

		Sphere sphere = new Sphere(zsamples, radsamples, diam/2);
		sphere.setTextureMode(TextureMode.Polar);
		sphere.setBound(new BoundingSphere());
		sphere.updateBound();
		this.setMesh(sphere);

		Texture t = assetManager.loadTexture("Textures/" + tex);
		t.setWrap(WrapMode.Repeat);
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
		mat.setTexture("DiffuseMap", t);
		this.setMaterial(mat);
	}


}
