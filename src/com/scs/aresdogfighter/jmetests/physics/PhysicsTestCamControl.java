package com.scs.aresdogfighter.jmetests.physics;

import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.model.KameriExplorer;
import com.scs.aresdogfighter.model.MySpaceStationModel;

/*
 * Test of flying a ship using cam control and manually moving back on a collision.
 */
public class PhysicsTestCamControl extends SimpleApplication implements PhysicsCollisionListener, PhysicsTickListener {

	private static final float MAX_SPEED = 0.5f;
	private static final float ACC = 0.05f;

	private Node ship, spacestation;
	//private BulletAppState bulletAppState;
	//private GhostControl gc1, gc2;
	//private CollisionShape collShape1, collShape2;
	private Vector3f prev_pos;
	private float speed;
	private boolean been_collision = false;
	//private Vector3f prev_safe_space, potential_safe_space;

	public static void main(String[] args){
		PhysicsTestCamControl app = new PhysicsTestCamControl();
		app.showSettings = false;
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		ship = new KameriExplorer(assetManager);
		ship.setModelBound(new BoundingBox());
		ship.updateModelBound();
		ship.setLocalTranslation(-3, 5, 0);
		ship.updateGeometricState();
		ship.updateModelBound();
		ship.setCullHint(CullHint.Always);
		rootNode.attachChild(ship);

		spacestation = new MySpaceStationModel(assetManager);//JMEFunctions.loadModel(assetManager, "Models/missile/missile.obj");
		spacestation.setModelBound(new BoundingBox());
		spacestation.updateModelBound();
		spacestation.setLocalTranslation(0, 0, 0);
		rootNode.attachChild(spacestation);

		BulletAppState bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		//bulletAppState.setDebugEnabled(true);
		bulletAppState.setEnabled(true);
		bulletAppState.getPhysicsSpace().addCollisionListener(this);
		bulletAppState.getPhysicsSpace().addTickListener(this);

		//collShape1 = CollisionShapeFactory.createMeshShape(ship1);
		CollisionShape collShape1 = new SphereCollisionShape(1.1f);//CollisionShapeFactory..createDynamicMeshShape(ship);
		//sceneShape1 = CollisionShapeFactory.createBoxShape(ship1);
		GhostControl gc1 = new GhostControl(collShape1);
		gc1.setSpatial(ship);
		ship.addControl(gc1);
		bulletAppState.getPhysicsSpace().add(gc1);

		CollisionShape collShape2 = CollisionShapeFactory.createMeshShape(spacestation);
		//collShape2 = CollisionShapeFactory.createDynamicMeshShape(ship2);
		//sceneShape2 = CollisionShapeFactory.createBoxShape(ship2);
		GhostControl gc2 = new GhostControl(collShape2);
		gc2.setSpatial(spacestation);
		spacestation.addControl(gc2);
		bulletAppState.getPhysicsSpace().add(gc2);

		inputManager.setCursorVisible(true);

		cam.setFrustumPerspective(Statics.FOV_ANGLE, settings.getWidth() / settings.getHeight(), .01f, 1000);

		this.getFlyByCamera().setMoveSpeed(5f);

		setupLight();

		//VideoRecorderAppState video_recorder = new VideoRecorderAppState();
		//stateManager.attach(video_recorder);
	}


	private void setupLight() {
		// Remove existing lights
		this.rootNode.getWorldLightList().clear();
		LightList list = this.rootNode.getWorldLightList();
		for (Light it : list) {
			this.rootNode.removeLight(it);
		}

		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White);
		rootNode.addLight(al);

	}


	@Override
	public void simpleUpdate(float tpf) {
		//System.out.println("simpleUpdate");
		//inputManager.setCursorVisible(true);

		if (been_collision == false) {
			System.out.println("Moving normally");
			prev_pos = this.ship.getWorldTranslation().clone();
			ship.setLocalRotation(cam.getRotation());
			// Always move fwd
			if (speed < MAX_SPEED) {
				speed += ACC;
			}

		} else {
			System.out.println("Responding to collision");
			//ship.setLocalTranslation(this.prev_pos);
			if (speed > 0) {
				speed = speed * -5;
			}
		}
		JMEFunctions.MoveForwards(ship, tpf*speed);
		cam.setLocation(ship.getWorldTranslation());

		System.out.println("Pos: " + ship.getWorldTranslation());
		//System.out.println("Pos: " + ship.getWorldTranslation().distance(prev_pos));

		//been_collision = false;
	}


	@Override
	public void collision(PhysicsCollisionEvent arg0) { // Called after physicsTick(), gets called loads of times
		//System.out.println("Collision:" + arg0.getObjectA().getUserObject() + " v " + arg0.getObjectB().getUserObject());
		been_collision = true;
	}


	@Override
	public void prePhysicsTick(PhysicsSpace space, float tpf) {
		//System.out.println(" PreTick");
		been_collision = false;
	}


	@Override
	public void physicsTick(PhysicsSpace space, float tpf) { // We're about to check for collisions
		//System.out.println("  physicsTick");
	}

}
