package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

public class AbstractBillboardModel extends AbstractModel {
	
	public AbstractBillboardModel(AssetManager assetManager, String name, float w, float h, String tex) {
		super(name);
		
		Geometry pulse = new Geometry(name + "_Billboard", new Quad(w, h));
		pulse.setModelBound(new BoundingSphere());
		pulse.updateModelBound();
		this.attachChild(pulse);
		Texture t = assetManager.loadTexture("Textures/" + tex);
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		mat.setTexture("DiffuseMap", t);
		pulse.setMaterial(mat);
		pulse.setQueueBucket(Bucket.Transparent);

	}

}
