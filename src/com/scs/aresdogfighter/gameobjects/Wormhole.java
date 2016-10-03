package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.model.WormholeModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class Wormhole extends GameObject {
	
	private WormholeModel wormhole;

	public Wormhole(ManualShipControlModule game, String name, float diam, AssetManager assetManager, BitmapFont guiFont) {
		super(game, SpaceEntity.Type.WORMHOLE, name, guiFont, false, true, true, true, ShowMode.SPACE, false);
		
		wormhole = new WormholeModel(assetManager);
		wormhole.setUserData("gameobject", this);

	}

	
	@Override
	public void process(float tpf) {
		super.process(tpf);

		getNode().lookAt(module.getCam().getLocation(), Vector3f.UNIT_Y);
		this.getNode().rotate(0, FastMath.DEG_TO_RAD/5, 0);
		
		// Check how close the player is
		if (this.dist_from_player <= 2) {
			ManualShipControlModule mod = (ManualShipControlModule)module;
			mod.flyThroughWormhole();
		}
	}

	
	@Override
	protected ColorRGBA getHUDColour() {
		return ColorRGBA.Magenta;
	}


	@Override
	public Node getNode() {
		return wormhole;
	}
	
/*
	@Override
	public float getWeaponDamage() {
		throw new RuntimeException("Dont access this");
	}
*/
	
	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f pos) {
		//module.showSmallExplosion(pos, Statics.SMALL_EXPLOSION_DURATION);
	}


	@Override
	public float getSpeed() {
		return 0;
	}


}
