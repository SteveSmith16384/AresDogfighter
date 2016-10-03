package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.model.BeamLaserModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class MovingLaserBlast extends AbstractBullet {

	private static final float LENGTH = 4f;

	private BeamLaserModel laser;

	protected MovingLaserBlast(ManualShipControlModule game, AssetManager assetManager, GameObject _shooter, float _range, float _damage, Vector3f optional_target) {
		super(game, assetManager, "MovingLaserBlast", _shooter, _range, _damage, optional_target);

		ColorRGBA col = null;
		float length = LENGTH;
		if (shooter == game.players_avatar) {
			col = ColorRGBA.Pink;
		} else {
			col = ColorRGBA.Red;
			length = length/4f;
		}

		Vector3f origin = shooter.getGunNode().getWorldTranslation().clone();
		//origin.addLocal(dir.mult(LENGTH)); // Prevent it starting inside the shooter
		
		//laser = new BeamLaserModel(assetManager, origin, origin.add(dir.multLocal(length)), col);
		laser = BeamLaserModel.Factory(assetManager, origin, origin.add(dir.multLocal(length)), col);

		try {
			if (!Statics.MUTE) {
				laser_audio = new AudioNode(assetManager, "Sound/space laser.wav", false);
				laser_audio.setPositional(false);
				Node n = (Node)this.getNode();
				n.attachChild(laser_audio);
				laser_audio.play();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/*
	@Override
	public void process(float tpf) {
		float this_dist = SPEED * tpf;
		this.dist_travelled += this_dist;
		if (dist_travelled > range) {
			this.removeFromGame(true);
		} else {
			laser.move(dir.mult(this_dist));
			laser.updateGeometricState();
		}
	}
	 */

	@Override
	public Node getNode() {
		return laser;
	}
	/*

	@Override
	public float getWeaponDamage() {
		throw new RuntimeException("Don't access this");
	}

	@Override
	public float getShieldLevel() {
		throw new RuntimeException("Don't access this");
	}

	@Override
	public void damage(GameObject shooter, GameObject actual_object, CaptainsOrder.Section section, float a) {
		throw new RuntimeException("Don't access this");
	}

	 */
}
