package com.scs.aresdogfighter.gameobjects;

import ssmith.lang.NumberFunctions;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.export.Savable;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.model.SpaceDebrisModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

/*
 * SpaceDebris is bits of ship.  It has no in-game value.
 * 
 */
public class SpaceDebris extends GameObject implements Savable {

	private SpaceDebrisModel debris;
	private Vector3f dir;
	private float speed, rot_speed;

	public SpaceDebris(ManualShipControlModule game, AssetManager assetManager, BitmapFont guiFont) {
		super(game, SpaceEntity.Type.DEBRIS, "Debris", guiFont, false, true, true, true, ShowMode.SPACE, true);

		debris = new SpaceDebrisModel(assetManager);
		debris.setUserData("gameobject", this);

		dir = new Vector3f(NumberFunctions.rndFloat(-1, 1), NumberFunctions.rndFloat(-1, 1), NumberFunctions.rndFloat(-1, 1));
		dir.normalizeLocal();

		speed = NumberFunctions.rndFloat(1, 5);
		rot_speed = NumberFunctions.rndFloat(.1f, 1.5f);

		CollisionShape collShape2 = new SphereCollisionShape(debris.diam/2);
		super.addToPhysics(collShape2, debris);
	}


	@Override
	protected ColorRGBA getHUDColour() {
		return ColorRGBA.DarkGray;
	}


	@Override
	public void process(float tpf) {
		super.process(tpf);

		this.getNode().rotate(0, 0, rot_speed * tpf);
		this.getNode().move(dir.mult( speed*tpf));
		//JMEFunctions.MoveForwards(this.getSpatial(), SPEED*tpf);
		module.checkForCollisions(this);
	}


	@Override
	public Node getNode() {
		return debris;
	}


	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f pos) {
		//module.showSmallExplosion(pos, Statics.SMALL_EXPLOSION_DURATION);
		this.showSmallExplosion( Statics.SMALL_EXPLOSION_DURATION);
		this.removeFromGame(true, false);
	}


	@Override
	public void removeFromGame(boolean destroyed, boolean store) {
		if (destroyed == false) {
			// Dont remove, just turn around and point us at the player
			Vector3f new_dir = this.module.players_avatar.getNode().getWorldTranslation().subtract(this.getNode().getWorldTranslation()).normalizeLocal();
			this.dir.set(new_dir);

			this.getNode().move(dir.mult(speed));
			module.checkForCollisions(this);
		} else {
			super.removeFromGame(destroyed, false);
		}

	}


	@Override
	public float getSpeed() {
		return 0;
	}



}
