package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.JMEFunctions;

public class TestJet extends SimpleApplication {

	private Node explosionEffect = new Node("explosionFX");
	private ParticleEmitter flame;

	//private static final int COUNT_FACTOR = 1;
	//private static final float COUNT_FACTOR_F = 1f;

	private static final boolean POINT_SPRITE = true;
	private static final Type EMITTER_TYPE = POINT_SPRITE ? Type.Point : Type.Triangle;

	public static void main(String[] args){
		TestJet app = new TestJet();
		app.showSettings = false;
		app.start();
	}


	private void createFlame(){
		flame = new ParticleEmitter("Flame", EMITTER_TYPE, 64);
		flame.setSelectRandomImage(true);
		flame.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, 1f));
		flame.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
		flame.setStartSize(0.1f);
		flame.setEndSize(.01f);
		flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
		flame.setParticlesPerSec(64);
		flame.setGravity(0, 0, 0);
		flame.setLowLife(.4f);
		flame.setHighLife(.5f);
		flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 7, 0));
		flame.getParticleInfluencer().setVelocityVariation(.01f);
		flame.setImagesX(1);
		flame.setImagesY(1);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
		mat.setBoolean("PointSprite", POINT_SPRITE);
		flame.setMaterial(mat);
		explosionEffect.attachChild(flame);
	}

	@Override
	public void simpleInitApp() {
		createFlame();
		//flame.emitAllParticles();
		
		explosionEffect.setLocalScale(0.5f);
		renderManager.preloadScene(explosionEffect);

		cam.setLocation(new Vector3f(0, 3.5135868f, 10));
		cam.setRotation(new Quaternion(1.5714673E-4f, 0.98696727f, -0.16091813f, 9.6381607E-4f));

		rootNode.attachChild(explosionEffect);

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));
		this.flyCam.setMoveSpeed(3.5f);

	}

	@Override
	public void simpleUpdate(float tpf){
		flame.emitAllParticles();
	}

}