package com.scs.aresdogfighter.misc;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/** Sample 11 - how to create fire, water, and explosion effects. */
public class HelloEffects extends SimpleApplication {

	public static void main(String[] args) {
		HelloEffects app = new HelloEffects();
		app.start();
	}


	@Override
	public void simpleInitApp() {
		ParticleEmitter fire = 
				new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 60);
		Material mat_red = new Material(assetManager, 
				"Common/MatDefs/Misc/Particle.j3md");
		mat_red.setTexture("Texture", assetManager.loadTexture(
				"Effects/Explosion/flame.png"));
		fire.setMaterial(mat_red);
		fire.setImagesX(2); 
		fire.setImagesY(2); // 2x2 texture animation
		fire.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
		//fire.setStartColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
		fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
		fire.setStartSize(0.2f);
		fire.setEndSize(0.1f);
		fire.setGravity(0, 0, 0);
		fire.setLowLife(.2f);
		fire.setHighLife(.2f);
		fire.setParticlesPerSec(30);
		fire.getParticleInfluencer().setVelocityVariation(0.05f); // Angle
		fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 6, 0));
		rootNode.attachChild(fire);

	}
}