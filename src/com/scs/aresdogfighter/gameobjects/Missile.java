package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.model.MissileModel2;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class Missile extends GameObject {

	private static final float SPEED = 16f;

	private MissileModel2 missile;
	public Ship1 shooter;
	private GameObject target;
	private float dist_travelled = 0;
	private float range;
	public float damage;
	private float next_ship_trail = 0;

	public Missile(ManualShipControlModule game, AssetManager assetManager, Ship1 _shooter, GameObject _target, float _range, float _damage) {
		super(game, null, "Missile", game.guiFont_small, false, true, false, true, ShowMode.SPACE, false);

		shooter = _shooter;
		target = _target;
		range = _range;
		damage = _damage;

		missile = new MissileModel2(assetManager);
		missile.setLocalTranslation(shooter.getNode().getWorldTranslation().clone());
		missile.setLocalRotation(shooter.getNode().getLocalRotation().clone());
		missile.rotate(0, FastMath.PI/4, 0);

		missile.setUserData("gameobject", this);

		if (!Statics.MUTE) {
			AudioNode sfx = new AudioNode(game.getAssetManager(), "Sound/105016__julien-matthey__jm-fx-fireball-01.wav", false);
			sfx.setPositional(false);
			missile.attachChild(sfx);
			sfx.play();
		}

		CollisionShape collShape2 = new SphereCollisionShape(missile.getWorldBound().getVolume()/FastMath.PI); //CollisionShapeFactory.createMeshShape(ship.getSpatial());
		super.addToPhysics(collShape2, missile);
	}


	@Override
	protected ColorRGBA getHUDColour() {
		return ColorRGBA.Orange;
	}
	

	@Override
	public void process(float tpf) {
		super.process(tpf);

		float this_dist = SPEED * tpf;
		this.dist_travelled += this_dist;
		if (dist_travelled > range) {
			module.showBigExplosion(this.getNode().getWorldTranslation(), Statics.BIG_EXPLOSION_DURATION);
			this.removeFromGame(true, false);
		} else {
			if (target.isAlive()) {
				JMEFunctions.TurnTowards_Gentle(missile, target.getNode().getWorldTranslation(), tpf*2);
			}
			JMEFunctions.MoveForwards(missile, SPEED * tpf);
			next_ship_trail -= SPEED;
			if (Statics.SHIP_TRAILS && next_ship_trail <= 0) { // don't create one each time
				//next_ship_trail = SmokeTrail.INTERVAL;
				//new SmokeTrail(module, module.getAssetManager(), module.guiFont_small, this.getSpatial().getWorldTranslation(), false).addToGame(module.rootNode);
			}
			missile.updateGeometricState();
			module.checkForCollisions(this);
		}
	}


	@Override
	public Node getNode() {
		return missile;
	}

/*
	@Override
	public float getWeaponDamage() {
		throw new RuntimeException("Don't access this");
	}
*/

	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f pos) {
		this.removeFromGame(true, false);
	}


	@Override
	public void updateHUDText() {
		hudText.setText("MISSILE\nDist: " + String.format("%d", (int)(this.distance(target))));
	}


	@Override
	public float getSpeed() {
		return SPEED;
	}



}
