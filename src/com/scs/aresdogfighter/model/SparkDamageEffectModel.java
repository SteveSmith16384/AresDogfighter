package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;

public class SparkDamageEffectModel extends AbstractModel {

	private static final int COUNT_FACTOR = 1;
	private static final float COUNT_FACTOR_F = 1f;

	private ParticleEmitter smoketrail;
	private Node explosionEffect = new Node("explosionFX");

	public SparkDamageEffectModel(AssetManager assetManager) {
		super("SparkDamageEffectModel");

		this.createSmokeTrail(assetManager);
		
		explosionEffect.scale(.2f);
		explosionEffect.updateModelBound();
		this.attachChild(explosionEffect);
	}

	
	public void process() {
		smoketrail.emitAllParticles();
	}

	
    private void createSmokeTrail(AssetManager assetManager) {
        smoketrail = new ParticleEmitter("SmokeTrail", Type.Triangle, 22 * COUNT_FACTOR);
        smoketrail.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, (float) (1.0f / COUNT_FACTOR_F)));
        smoketrail.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
        smoketrail.setStartSize(.2f);
        smoketrail.setEndSize(1f);

//        smoketrail.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        smoketrail.setFacingVelocity(true);
        smoketrail.setParticlesPerSec(0);
        smoketrail.setGravity(0, 1, 0);
        smoketrail.setLowLife(.4f);
        smoketrail.setHighLife(.5f);
        smoketrail.setInitialVelocity(new Vector3f(0, 12, 0));
        smoketrail.setVelocityVariation(1);
        smoketrail.setImagesX(1);
        smoketrail.setImagesY(3);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/smoketrail.png"));
        smoketrail.setMaterial(mat);
        explosionEffect.attachChild(smoketrail);
    }


}
