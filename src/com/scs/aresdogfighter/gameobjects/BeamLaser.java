package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.model.BeamLaserModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class BeamLaser extends AbstractBullet {

	private static final float DURATION = 3;

	private BeamLaserModel laser;
	private GameObject start, end;
	private float end_time;

	public BeamLaser(ManualShipControlModule game, AssetManager assetManager, Ship1 _start, Ship1 _end, float _range, float _damage) {
		super(game, assetManager, "LaserBlast", _start, _range, _damage, _end.getNode().getWorldTranslation());

		start = _start;
		//end = _end;

		ColorRGBA col = null;
		/*if (start == game.players_ship) {
			col = ColorRGBA.Pink;
		} else {*/
			col = ColorRGBA.Red;
		//}
		
		laser = BeamLaserModel.Factory(assetManager, start.getNode().getWorldTranslation(), end.getNode().getWorldTranslation(), col);//new BeamLaserModel(assetManager, start.getSpatial().getWorldTranslation(), end.getSpatial().getWorldTranslation(), col);
		
		end_time = DURATION;
	}


	@Override
	public void process(float tpf) {
			laser.setPoints(start.getNode().getWorldTranslation(), end.getNode().getWorldTranslation(), true);

		end_time -= tpf;
		if (end_time < 0) {
			this.removeFromGame(true, false);
		}
	}


	@Override
	public Node getNode() {
		return laser;
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


}
