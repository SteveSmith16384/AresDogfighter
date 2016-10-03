package com.scs.aresdogfighter.shapes;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

public class MyBox extends Geometry {

	public MyBox(float xlen, float ylen, float zlen, String name, AssetManager assetManager, String tex, float texx, float texy) {
		super(name);

		Box box = new Box(xlen/2, ylen/2, zlen/2); // create cube shape
		box.setBound(new BoundingSphere());
		box.updateBound();
		this.setMesh(box);

		//Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		if (tex != null) {
			Texture t = assetManager.loadTexture("Textures/" + tex);
			t.setWrap(WrapMode.Repeat);
			//mat.setTexture("ColorMap", t);
			mat.setTexture("DiffuseMap", t);
			box.scaleTextureCoordinates(new Vector2f(texx, texy));
		}	

		//Material mat = new Material(assetManager,"Shaders/MyShader.j3md");  // create a simple material
		//mat.setTexture("ColorMap", t);

		//mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
		this.setMaterial(mat);                   // set the cube's material

		this.move(xlen/2, -ylen/2, zlen/2);
	}


}
