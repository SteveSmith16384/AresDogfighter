package com.scs.aresdogfighter.gameobjects;

import java.util.ArrayList;
import java.util.List;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.CollisionLogic;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.data.Weapon.BulletType;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public abstract class AbstractBullet extends GameObject {

	public static final float SPEED = 18f;//20f;

	public GameObject shooter;
	protected float dist_travelled = 0;
	protected Vector3f dir;
	protected float range;
	public float damage;
	private List<PhysicsRayTestResult> reused_list = new ArrayList<PhysicsRayTestResult>();
	
	protected AudioNode laser_audio;

	public static AbstractBullet Factory(ManualShipControlModule game, AssetManager assetManager, Ship1 shooter, Vector3f optional_target) {
		if (shooter.shipdata.weapon.bullet_type == BulletType.PULSE_LASER) {
			return new MovingLaserBlast(game, assetManager, shooter, shooter.getWeaponRange(), shooter.getWeaponDamage(), optional_target);
		} else if (shooter.shipdata.weapon.bullet_type == BulletType.BURST_LASER) {
			return new PulseLaserShot(game, assetManager, shooter, shooter.getWeaponRange(), shooter.getWeaponDamage(), optional_target);
			/*} else if (shooter.shipdata.weapon.bullet_type == BulletType.BEAM_LASER) {
			return new BeamLaser(game, assetManager, shooter, null, shooter.getWeaponRange(), shooter.getWeaponDamage());*/
		} else {
			throw new RuntimeException("Unknown type: " + shooter.shipdata.weapon.bullet_type);
		}
	}


	protected AbstractBullet(ManualShipControlModule game, AssetManager assetManager, String name, GameObject _shooter, float _range, float _damage, Vector3f optional_target) {
		super(game, null, name, null, false, true, false, false, ShowMode.ALWAYS, false);

		shooter = _shooter;
		range = _range;
		damage= _damage;

		if (optional_target == null) {
			dir = shooter.getNode().getLocalRotation().mult(Vector3f.UNIT_Z);
		} else {
			dir = optional_target.subtract(shooter.getNode().getWorldTranslation()).normalizeLocal();
		}

	}


	@Override
	public void process(float tpf) {
		super.process(tpf);
		
		module.inputManager.setCursorVisible(!Statics.HIDE_MOUSE);

		float this_dist = SPEED * tpf;
		this.dist_travelled += this_dist;
		if (dist_travelled > range) {
			this.removeFromGame(true, false);
		} else {
			Vector3f move = dir.mult(this_dist);
			this.getNode().move(move);
			this.getNode().updateGeometricState();
			
			// Check for collisions as rays
			/*reused_list.clear();
			module.bulletAppState.getPhysicsSpace().rayTest(this.getSpatial().getWorldTranslation(), this.getSpatial().getWorldTranslation().add(move), reused_list);
			if (reused_list.size() > 0) {
				for (PhysicsRayTestResult result : reused_list) {
					Node no1 = (Node)result.getCollisionObject().getUserObject();
					GameObject o1 = (GameObject)no1.getUserData("gameobject");
					module.debug("Bullet has hit " + o1);
					CollisionLogic.HandleCollision(module, this, o1, this.getSpatial().getWorldTranslation());
				}
			}*/
		}
	}

/*
	@Override
	public float getWeaponDamage() {
		throw new RuntimeException("Don't access this");
	}

*/
	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f pos) {
		//throw new RuntimeException("Don't access this");
	}


	@Override
	public float getSpeed() {
		return 0;
	}


}
