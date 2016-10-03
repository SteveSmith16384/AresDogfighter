package com.scs.aresdogfighter.hud;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

public class HealthBar extends Geometry {
	
	private float width, height;

	public HealthBar(AssetManager assetManager, float w, float h) {
		super("HealthBar", new Quad(w, h));
		
		width = w;
		height = h;
		
		Material mat_hb = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture t = assetManager.loadTexture("Textures/healthbar.png");
		mat_hb.setTexture("ColorMap", t);
		this.setMaterial(mat_hb);
		//this.getMesh().scaleTextureCoordinates(new Vector2f(1f, 1f));
		this.setLevel(1f);
	}
	
	
	public void setLevel(float f) {
		Quad quad = (Quad)this.getMesh();
		quad.updateGeometry(width*f, height);
		this.getMesh().scaleTextureCoordinates(new Vector2f(f, 1f));
	}

}
