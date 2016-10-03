package com.scs.aresdogfighter.gameobjects;

import com.jme3.font.BitmapFont;
import com.jme3.math.Vector3f;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

/**
 * For scenic objects that don't collide or do anything except disappear after a while
 *
 */
public abstract class AbstractScenicGameObject extends GameObject {
	
	private float remove_at;
	
	public AbstractScenicGameObject(ManualShipControlModule _game, String _name, BitmapFont guiFont, float duration) {
		super(_game, SpaceEntity.Type.SCENERY, _name, guiFont, false, false, false, false, ShowMode.ALWAYS, false);
		
		remove_at = duration;
	}


	@Override
	public void process(float tpf) {
		super.process(tpf);

		remove_at -= tpf;
		if (remove_at <= 0) {
			this.removeFromGame(true, false);
		}
	}

/*
	@Override
	public float getWeaponDamage() {
		return 0;
	}
	
*/
	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f point) {
		
	}

	@Override
	public float getSpeed() {
		return 0;
	}



}
