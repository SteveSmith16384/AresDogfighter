package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.export.Savable;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.GameObject.ShowMode;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.model.CargoModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

import ssmith.lang.NumberFunctions;

public class Cargo extends GameObject {

	private CargoModel cargo;
	private Vector3f dir;
	private float speed, rot_speed;

	public Cargo(ManualShipControlModule game, AssetManager assetManager, BitmapFont guiFont) {
		super(game, SpaceEntity.Type.CARGO, "Cargo", guiFont, true, true, false, true, ShowMode.SPACE, true);

		cargo = new CargoModel(assetManager);
		cargo.setUserData("gameobject", this);

		dir = new Vector3f(NumberFunctions.rndFloat(-1, 1), NumberFunctions.rndFloat(-1, 1), NumberFunctions.rndFloat(-1, 1));
		dir.normalizeLocal();
		speed = NumberFunctions.rndFloat(1, 5);
		rot_speed = NumberFunctions.rndFloat(.1f, 2f);

		//CollisionShape collShape2 = CollisionShapeFactory.createBoxShape(cargo);
		//super.addToPhysics(collShape2, cargo);
	}


	@Override
	protected ColorRGBA getHUDColour() {
		return ColorRGBA.Cyan;
	}


	@Override
	public void process(float tpf) {
		super.process(tpf);

		this.getNode().rotate( rot_speed * tpf, 0, 0);
		this.getNode().move(dir.mult( speed*tpf));
		module.checkForCollisions(this);
	}


	@Override
	public Node getNode() {
		return cargo;
	}

/*
	@Override
	public float getWeaponDamage() {
		return 0;
	}
*/

	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f pos) {
		//module.showSmallExplosion(pos, Statics.SMALL_EXPLOSION_DURATION);
		this.showSmallExplosion( Statics.SMALL_EXPLOSION_DURATION);

		this.removeFromGame(true, false);
	}


	public void reverseDir() {
		this.dir.multLocal(-1);
	}

	@Override
	public float getSpeed() {
		return 0;
	}



}
