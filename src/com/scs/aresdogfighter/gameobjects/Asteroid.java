package com.scs.aresdogfighter.gameobjects;

import ssmith.lang.NumberFunctions;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.data.SpaceEntity.Type;
import com.scs.aresdogfighter.model.SimpleModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class Asteroid extends GameObject {

	private Node asteroid;
	private Vector3f dir;
	private float speed, rot_speed;

	public Asteroid(ManualShipControlModule game, AssetManager assetManager, BitmapFont guiFont) {
		super(game, SpaceEntity.Type.ASTEROID, Asteroid.class.getSimpleName(), guiFont, false, true, true, true, ShowMode.SPACE, false);

		asteroid = new Node(this.getClass().getSimpleName());
		asteroid.attachChild(SimpleModel.GetAsteroid1(assetManager));
		asteroid.setUserData("gameobject", this);

		dir = new Vector3f(NumberFunctions.rndFloat(-1, 1), NumberFunctions.rndFloat(-1, 1), NumberFunctions.rndFloat(-1, 1));
		dir.normalizeLocal();

		speed = NumberFunctions.rndFloat(3, 10);
		rot_speed = NumberFunctions.rndFloat(.1f, 1.5f);

		//CollisionShape collShape2 = new SphereCollisionShape(3);
		//super.addToPhysics(collShape2, asteroid);
	}


	@Override
	protected ColorRGBA getHUDColour() {
		return ColorRGBA.DarkGray;
	}


	@Override
	public void process(float tpf) {
		super.process(tpf);
		
		module.num_asteroids++;

		this.getNode().rotate(0, 0, rot_speed * tpf);
		this.getNode().move(dir.mult( speed*tpf));
		//JMEFunctions.MoveForwards(this.getSpatial(), SPEED*tpf);
		module.checkForCollisions(this);
	}


	@Override
	public Node getNode() {
		return asteroid;
	}


	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f pos) {
		module.showBigExplosion(pos, Statics.SMALL_EXPLOSION_DURATION);
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
			// Create cargo for collection
			module.addEntity(Type.CARGO, this.getNode().getWorldTranslation().x, this.getNode().getWorldTranslation().y, this.getNode().getWorldTranslation().z);

			super.removeFromGame(true, false);
		}

	}


	@Override
	public void updateHUDText() {
		if (Statics.DEBUG) {
			super.updateHUDText();
		} else {
			hudText.setText("");
		}
	}

	
	@Override
	public float getSpeed() {
		return speed;
	}


}
