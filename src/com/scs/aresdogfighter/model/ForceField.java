package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.aresdogfighter.IProcessable;

/**
 * http://wiki.jmonkeyengine.org/doku.php/jme3:beginner:hello_material
 * http://wiki.jmonkeyengine.org/doku.php/jme3:intermediate:how_to_use_materials
 *
 */
public class ForceField extends Geometry {

	public ForceField(float diam, int zsamples, int radsamples, AssetManager assetManager) {
		super("ForceField");

		Sphere sphere = new Sphere(zsamples, radsamples, diam/2);
		sphere.setBound(new BoundingSphere());
		sphere.updateBound();
		this.setMesh(sphere);

		Texture t = assetManager.loadTexture("Textures/forceshield.png");
		t.setWrap(WrapMode.Repeat);

		sphere.scaleTextureCoordinates(new Vector2f(2f, 2f));

		//Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
		//mat.setTexture("ColorMap", t);
		
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		mat.setTexture("DiffuseMap", t);

		//mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
	    //mat.setColor("GlowColor", ColorRGBA.Green);
		this.setMaterial(mat);                   // set the cube's material
		
		this.setQueueBucket(Bucket.Transparent);
	}

/*
	@Override
	public void process(float tpf) {
		

	}
*/
}
