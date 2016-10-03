package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class SmallExplosionTest extends SimpleApplication {

	private float time = 0;
	private int state = 0;
	private Node explosionEffect = new Node("explosionFX");
	private ParticleEmitter flame, flash, spark, roundspark, smoketrail, debris, shockwave;

	private static final int COUNT_FACTOR = 1;
	private static final float COUNT_FACTOR_F = 1f;

	private static final boolean POINT_SPRITE = true;
	private static final Type EMITTER_TYPE = POINT_SPRITE ? Type.Point : Type.Triangle;

	public static void main(String[] args){
		SmallExplosionTest app = new SmallExplosionTest();
		app.start();
	}


	private void createFlame(){
		flame = new ParticleEmitter("Flame", EMITTER_TYPE, 32 * COUNT_FACTOR);
		flame.setSelectRandomImage(true);
		flame.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, (float) (1f / COUNT_FACTOR_F)));
		flame.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
		flame.setStartSize(1.3f);
		flame.setEndSize(2f);
		flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
		flame.setParticlesPerSec(0);
		flame.setGravity(0, -5, 0);
		flame.setLowLife(.4f);
		flame.setHighLife(.5f);
		flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 7, 0));
		flame.getParticleInfluencer().setVelocityVariation(1f);
		flame.setImagesX(2);
		flame.setImagesY(2);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
		mat.setBoolean("PointSprite", POINT_SPRITE);
		flame.setMaterial(mat);
		explosionEffect.attachChild(flame);
	}

	private void createFlash(){
		flash = new ParticleEmitter("Flash", EMITTER_TYPE, 24 * COUNT_FACTOR);
		flash.setSelectRandomImage(true);
		flash.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, (float) (1f / COUNT_FACTOR_F)));
		flash.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
		flash.setStartSize(.1f);
		flash.setEndSize(3.0f);
		flash.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
		flash.setParticlesPerSec(0);
		flash.setGravity(0, 0, 0);
		flash.setLowLife(.2f);
		flash.setHighLife(.2f);
		flash.setInitialVelocity(new Vector3f(0, 5f, 0));
		flash.setVelocityVariation(1);
		flash.setImagesX(2);
		flash.setImagesY(2);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flash.png"));
		mat.setBoolean("PointSprite", POINT_SPRITE);
		flash.setMaterial(mat);
		explosionEffect.attachChild(flash);
	}

	private void createRoundSpark(){
		roundspark = new ParticleEmitter("RoundSpark", EMITTER_TYPE, 20 * COUNT_FACTOR);
		roundspark.setStartColor(new ColorRGBA(1f, 0.29f, 0.34f, (float) (1.0 / COUNT_FACTOR_F)));
		roundspark.setEndColor(new ColorRGBA(0, 0, 0, (float) (0.5f / COUNT_FACTOR_F)));
		roundspark.setStartSize(1.2f);
		roundspark.setEndSize(1.8f);
		roundspark.setShape(new EmitterSphereShape(Vector3f.ZERO, 2f));
		roundspark.setParticlesPerSec(0);
		roundspark.setGravity(0, -.5f, 0);
		roundspark.setLowLife(1.8f);
		roundspark.setHighLife(2f);
		roundspark.setInitialVelocity(new Vector3f(0, 3, 0));
		roundspark.setVelocityVariation(.5f);
		roundspark.setImagesX(1);
		roundspark.setImagesY(1);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/roundspark.png"));
		mat.setBoolean("PointSprite", POINT_SPRITE);
		roundspark.setMaterial(mat);
		explosionEffect.attachChild(roundspark);
	}

	private void createSpark(){
		spark = new ParticleEmitter("Spark", Type.Triangle, 30 * COUNT_FACTOR);
		spark.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, (float) (1.0f / COUNT_FACTOR_F)));
		spark.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
		spark.setStartSize(.5f);
		spark.setEndSize(.5f);
		spark.setFacingVelocity(true);
		spark.setParticlesPerSec(0);
		spark.setGravity(0, 5, 0);
		spark.setLowLife(1.1f);
		spark.setHighLife(1.5f);
		spark.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 20, 0));
		spark.getParticleInfluencer().setVelocityVariation(1);
		spark.setImagesX(1);
		spark.setImagesY(1);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/spark.png"));
		spark.setMaterial(mat);
		explosionEffect.attachChild(spark);
	}

	private void createSmokeTrail(){
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

	private void createDebris(){
		debris = new ParticleEmitter("Debris", Type.Triangle, 15 * COUNT_FACTOR);
		debris.setSelectRandomImage(true);
		debris.setRandomAngle(true);
		debris.setRotateSpeed(FastMath.TWO_PI * 4);
		debris.setStartColor(new ColorRGBA(1f, 0.59f, 0.28f, (float) (1.0f / COUNT_FACTOR_F)));
		debris.setEndColor(new ColorRGBA(.5f, 0.5f, 0.5f, 0f));
		debris.setStartSize(.2f);
		debris.setEndSize(.2f);

		//        debris.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
		debris.setParticlesPerSec(0);
		debris.setGravity(0, 12f, 0);
		debris.setLowLife(1.4f);
		debris.setHighLife(1.5f);
		debris.setInitialVelocity(new Vector3f(0, 15, 0));
		debris.setVelocityVariation(.60f);
		debris.setImagesX(3);
		debris.setImagesY(3);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/Debris.png"));
		debris.setMaterial(mat);
		explosionEffect.attachChild(debris);
	}

	private void createShockwave(){
		shockwave = new ParticleEmitter("Shockwave", Type.Triangle, 1 * COUNT_FACTOR);
		//        shockwave.setRandomAngle(true);
		shockwave.setFaceNormal(Vector3f.UNIT_Y);
		shockwave.setStartColor(new ColorRGBA(.48f, 0.17f, 0.01f, (float) (.8f / COUNT_FACTOR_F)));
		shockwave.setEndColor(new ColorRGBA(.48f, 0.17f, 0.01f, 0f));

		shockwave.setStartSize(0f);
		shockwave.setEndSize(7f);

		shockwave.setParticlesPerSec(0);
		shockwave.setGravity(0, 0, 0);
		shockwave.setLowLife(0.5f);
		shockwave.setHighLife(0.5f);
		shockwave.setInitialVelocity(new Vector3f(0, 0, 0));
		shockwave.setVelocityVariation(0f);
		shockwave.setImagesX(1);
		shockwave.setImagesY(1);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/shockwave.png"));
		shockwave.setMaterial(mat);
		explosionEffect.attachChild(shockwave);
	}

	@Override
	public void simpleInitApp() {
		createFlame();
		createFlash();
		//createSpark();
		//createRoundSpark();
		//createSmokeTrail();
		//createDebris();
		//createShockwave();
		explosionEffect.setLocalScale(0.5f);
		renderManager.preloadScene(explosionEffect);

		cam.setLocation(new Vector3f(0, 3.5135868f, 10));
		cam.setRotation(new Quaternion(1.5714673E-4f, 0.98696727f, -0.16091813f, 9.6381607E-4f));

		rootNode.attachChild(explosionEffect);
	}

	@Override
	public void simpleUpdate(float tpf){  
		/*time += tpf / speed;
		if (time > 1f && state == 0){*/
			if (flash != null) {
				flash.emitAllParticles();
			}
			if (spark != null) {
				spark.emitAllParticles();
			}
			if (smoketrail != null) {
				smoketrail.emitAllParticles();
			}
			if (debris != null) {
				debris.emitAllParticles();
			}
			if (shockwave != null) {
				shockwave.emitAllParticles();
			}
			/*state++;
		}
		if (time > 1f + .05f / speed && state == 1){*/
			if (flame != null) {
				flame.emitAllParticles();
			}
			if (roundspark != null) {
				roundspark.emitAllParticles();
			}
			/*state++;
		}

		// rewind the effect
		if (time > 5 / speed && state == 2){
			state = 0;
			time = 0;

			if (flash != null) {
				flash.killAllParticles();
			}
			if (spark != null) {
				spark.killAllParticles();
			}
			if (smoketrail != null) {
				smoketrail.killAllParticles();
			}
			if (debris != null) {
				debris.killAllParticles();
			}*/
			/*if (flame != null) {
				flame.killAllParticles();
			}*/
			/*if (roundspark != null) {
				roundspark.killAllParticles();
			}
			if (shockwave != null) {
				shockwave.killAllParticles();
			}
		}*/
	}

}