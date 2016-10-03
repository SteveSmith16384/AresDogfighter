package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;

public class HyperspaceEffectModel extends AbstractModel {

	//private static final int COUNT_FACTOR = 1;
	private static final float COUNT_FACTOR_F = 1f;

	private ParticleEmitter spark;
	private Node explosionEffect = new Node("explosionFX");

	public HyperspaceEffectModel(AssetManager assetManager) {
		super("HyperspaceEffectModel");

		this.createSpark(assetManager);
		//spark.emitAllParticles();

		explosionEffect.scale(.2f);
		explosionEffect.updateModelBound();
		this.attachChild(explosionEffect);

	}


	public void start() {
		spark.emitAllParticles();
	}


	public void stop() {
		spark.killAllParticles();
	}


	private void createSpark(AssetManager assetManager) {
		spark = new ParticleEmitter("Spark", Type.Triangle, 10);
		//spark.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, (float) (1.0f / COUNT_FACTOR_F)));
		//spark.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
		spark.setStartColor(new ColorRGBA(1f, 1, 1f, (float) (1.0f / COUNT_FACTOR_F)));
		spark.setEndColor(new ColorRGBA(1f, 1f, 1f, 0f));
		spark.setStartSize(.2f);
		spark.setEndSize(.2f);
		spark.setFacingVelocity(true);
		spark.setParticlesPerSec(1);
		spark.setGravity(0, 0, 0);
		spark.setLowLife(2f);
		spark.setHighLife(2f);
		spark.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 15, 0));
		spark.getParticleInfluencer().setVelocityVariation(1);
		spark.setImagesX(1);
		spark.setImagesY(1);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/spark.png"));
		spark.setMaterial(mat);
		explosionEffect.attachChild(spark);
	}


	public void setSparkVelocity(float f) {
		if (f <= 0) {
			stop();
		} else {
			start();
			spark.getParticleInfluencer().setInitialVelocity(new Vector3f(0, f*2, 0));
			spark.setParticlesPerSec(f);
		}
	}

}
