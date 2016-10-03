package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapFont;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainGridLodControl;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.model.TerrainModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class MyTerrain extends GameObject {

	private TerrainModel terrain_model;

	public MyTerrain(ManualShipControlModule game, AssetManager assetManager, BitmapFont guiFont) {
		super(game, null, "Terrain", guiFont, false, false, true, true, ShowMode.PLANET, false);

		terrain_model = new TerrainModel(assetManager);
		terrain_model.setUserData("gameobject", this);

		// The LOD (level of detail) depends on were the camera is:
		/*if (Statics.USE_TERRAIN_GRID == false) {
			TerrainLodControl control = new TerrainLodControl(terrain_model.terrainquad, game.getCam());
			terrain_model.terrainquad.addControl(control);
		} else {*/
			TerrainLodControl control = new TerrainGridLodControl(this.terrain_model.terrainquad, game.getCam());
			control.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
			this.terrain_model.terrainquad.addControl(control);
		//}

		terrain_model.addControl(new RigidBodyControl(0));
		//module.bulletAppState.getPhysicsSpace().add(terrain_model);

	}


	@Override
	public void removeFromGame(boolean destroyed, boolean store) {
		super.removeFromGame(destroyed, store);

		super.removeFromGame(destroyed, store);
		try {
			//this.module.bulletAppState.getPhysicsSpace().remove(terrain_model);
		} catch (NullPointerException ex) {
			module.log("Error removing terrain");
			ex.printStackTrace();
		}
	}


	@Override
	public void process(float tpf) {
		super.process(tpf);

		//module.debug("Player y=" + module.players_ship.getSpatial().getWorldTranslation().y);

	}


	/*@Override
	protected ColorRGBA getHUDColour() {
		return ColorRGBA.Pink;
	}*/


	@Override
	public Node getNode() {
		return terrain_model;
	}

/*
	@Override
	public float getWeaponDamage() {
		return 0;
	}
*/

	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f pos) {
		module.showSmallExplosion(pos, Statics.SMALL_EXPLOSION_DURATION);
	}

	@Override
	public float getSpeed() {
		return 0;
	}



}
