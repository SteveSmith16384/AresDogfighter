package com.scs.aresdogfighter.shapes;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.WireBox;

public class MyWireframe extends Geometry {

	public MyWireframe(BoundingVolume bbox, String name, AssetManager assetManager) {
		super(name);

		WireBox box = new WireBox();
		if (bbox instanceof BoundingBox) {
			box.fromBoundingBox((BoundingBox)bbox);
		} else if (bbox instanceof BoundingSphere) {
			//box..fromBoundingSphere((BoundingSphere)bbox);
		}
		box.setBound(new BoundingBox());
		box.updateBound();
		this.setMesh(box);

		//Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		/*if (tex != null) {
			Texture t = assetManager.loadTexture("Textures/" + tex);
			t.setWrap(WrapMode.Repeat);
			//mat.setTexture("ColorMap", t);
			mat.setTexture("DiffuseMap", t);
			box.scaleTextureCoordinates(new Vector2f(texx, texy));
		}*/
		mat.getAdditionalRenderState().setWireframe(true);

		//Material mat = new Material(assetManager,"Shaders/MyShader.j3md");  // create a simple material
		//mat.setTexture("ColorMap", t);

		//mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
		this.setMaterial(mat);                   // set the cube's material

		//this.move(xlen/2, -ylen/2, zlen/2);
	}


}
