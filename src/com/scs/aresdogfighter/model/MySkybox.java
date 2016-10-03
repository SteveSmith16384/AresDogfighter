package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class MySkybox extends Node implements IProcessable {

	private static final boolean USE_LIGHTING = false;
	private static final float DIST = 20f;
	private static final float SIZE = 10f;

	private ManualShipControlModule module;

	public MySkybox(ManualShipControlModule _module, AssetManager assetManager) {
		super("MySkybox");

		module = _module;

		Geometry i1 = new Geometry("Billboard", new Quad(SIZE, SIZE));
		Material mat = null;//new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
		if (USE_LIGHTING == false) {
			mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		} else {
			mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
		}
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		Texture t = assetManager.loadTexture("Textures/galaxy1.png");
		if (USE_LIGHTING == false) {
			mat.setTexture("ColorMap", t);
		} else {
			mat.setTexture("DiffuseMap", t);
		}
		i1.setMaterial(mat);
		i1.setLocalTranslation(DIST, 0f, 0f);
		i1.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		this.attachChild(i1);

		i1 = new Geometry("Billboard", new Quad(SIZE, SIZE));
		if (USE_LIGHTING == false) {
			mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		} else {
			mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
		}
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		t = assetManager.loadTexture("Textures/galaxy2.png");
		if (USE_LIGHTING == false) {
			mat.setTexture("ColorMap", t);
		} else {
			mat.setTexture("DiffuseMap", t);
		}
		i1.setMaterial(mat);
		i1.setLocalTranslation(-DIST, 0f, 0f);
		i1.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		this.attachChild(i1);

		i1 = new Geometry("Billboard", new Quad(SIZE, SIZE));
		if (USE_LIGHTING == false) {
			mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		} else {
			mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
		}
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		t = assetManager.loadTexture("Textures/nebula1.png");
		if (USE_LIGHTING == false) {
			mat.setTexture("ColorMap", t);
		} else {
			mat.setTexture("DiffuseMap", t);
		}
		i1.setMaterial(mat);
		i1.setLocalTranslation(0f, DIST, 0f);
		i1.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		this.attachChild(i1);

		i1 = new Geometry("Billboard", new Quad(SIZE, SIZE));
		if (USE_LIGHTING == false) {
			mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		} else {
			mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
		}
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		t = assetManager.loadTexture("Textures/nebula2.png");
		if (USE_LIGHTING == false) {
			mat.setTexture("ColorMap", t);
		} else {
			mat.setTexture("DiffuseMap", t);
		}
		i1.setMaterial(mat);
		i1.setLocalTranslation(0f, -DIST, 0f);
		i1.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		this.attachChild(i1);

		i1 = new Geometry("Billboard", new Quad(SIZE, SIZE));
		if (USE_LIGHTING == false) {
			mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		} else {
			mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
		}
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		t = assetManager.loadTexture("Textures/nebula3.png");
		if (USE_LIGHTING == false) {
			mat.setTexture("ColorMap", t);
		} else {
			mat.setTexture("DiffuseMap", t);
		}
		i1.setMaterial(mat);
		i1.setLocalTranslation(0f, 0, DIST);
		i1.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		this.attachChild(i1);

		i1 = new Geometry("Billboard", new Quad(SIZE, SIZE));
		if (USE_LIGHTING == false) {
			mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		} else {
			mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
		}
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		t = assetManager.loadTexture("Textures/sun.png");
		if (USE_LIGHTING == false) {
			mat.setTexture("ColorMap", t);
		} else {
			mat.setTexture("DiffuseMap", t);
		}
		i1.setMaterial(mat);
		i1.setLocalTranslation(0f, 0, -DIST);
		i1.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		//this.attachChild(i1);  Hide Sun

		this.setQueueBucket(Bucket.Sky);
	}


	@Override
	public void process(float tpf) {
		Vector3f centre = this.module.getCam().getLocation();
		this.setLocalTranslation(centre);
	}

}
