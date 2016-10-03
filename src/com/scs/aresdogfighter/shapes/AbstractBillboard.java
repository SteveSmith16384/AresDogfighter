package com.scs.aresdogfighter.shapes;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

public class AbstractBillboard {

	private Geometry wormhole;
	

	public AbstractBillboard(AssetManager assetManager, String tex) {
		super();
		
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

		// Create rectangle of size 10x10
		wormhole = new Geometry("Billboard", new Quad(10f, 10f));

		Texture t = assetManager.loadTexture("Textures/" + tex);
		t.setWrap(WrapMode.Repeat);
		mat.setTexture("DiffuseMap", t);

		wormhole.setMaterial(mat);
		wormhole.setLocalTranslation(0f, 0f, 0f);
		//rootNode.attachChild(wormhole);

		wormhole.setQueueBucket(Bucket.Transparent);

	}

}
