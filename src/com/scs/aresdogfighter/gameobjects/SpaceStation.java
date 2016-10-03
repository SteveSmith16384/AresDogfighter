package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.model.MySpaceStationModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class SpaceStation extends GameObject {

	private MySpaceStationModel spacestation;
	public float health = -1f;
	
	public SpaceStation(ManualShipControlModule game, String name, AssetManager assetManager, BitmapFont guiFont) {
		super(game, SpaceEntity.Type.SPACESTATION, name, guiFont, true, true, true, true, ShowMode.SPACE, false);
		
		spacestation = new MySpaceStationModel(assetManager);
		spacestation.setUserData("gameobject", this);

		spacestation.setModelBound(new BoundingBox());
		spacestation.updateModelBound();
		
		//CollisionShape collShape2 = CollisionShapeFactory.createMeshShape(spacestation);
		//super.addToPhysics(collShape2, spacestation);
		
		super.postSpatial();

		this.setHealth(10f);
	}
	

	@Override
	public void process(float tpf) {
		super.process(tpf);

		this.getNode().rotate(0, 0, tpf * health / 10);

		this.module.checkForCollisions(this);
	}

	
	@Override
	protected ColorRGBA getHUDColour() {
		return ColorRGBA.White;
	}


	@Override
	public Node getNode() {
		return spacestation;
	}


	@Override
	public void damage(GameObject shooter, GameObject actual_object, float a, Vector3f pos) {
		module.showSmallExplosion(pos, Statics.SMALL_EXPLOSION_DURATION);
		this.incHealth(-a);
	}

	
	@Override
	public float getSpeed() {
		return 0;
	}


	public float getHealth() {
		return this.health;
	}
	

	public void setHealth(float f) {
		this.health = f;
		module.hud.setSpaceStationHealthText();
	}


	public void incHealth(float f) {
		this.health += f;
		module.hud.setSpaceStationHealthText();
		if (this.health <= 0) {
			module.gameOver("SPACESTATION DESTROYED");
		}
	}

}
