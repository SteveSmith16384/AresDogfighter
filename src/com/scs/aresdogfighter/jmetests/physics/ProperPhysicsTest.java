package com.scs.aresdogfighter.jmetests.physics;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.model.KameriExplorer;
import com.scs.aresdogfighter.model.MySpaceStationModel;

// http://davidb.github.io/sandbox_wiki_jme/jme3/advanced/physics.html

/*
 * Test flying a ship using cam control and using proper physics for collisions
 */
public class ProperPhysicsTest extends SimpleApplication implements PhysicsCollisionListener, PhysicsTickListener {

	private static final boolean MOUSE_CONTROL = false;

	private Node ship, spacestation;
	private BulletAppState bulletAppState;
	private RigidBodyControl ship_control, ss_control;
	private CollisionShape ship_shape, ss_shape;
	private float no_move_timer;


	public static void main(String[] args){
		ProperPhysicsTest app = new ProperPhysicsTest();
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
		//ship.setLocalTranslation(-3, 5, 0); // misses
		ship.setLocalTranslation(0, 5, 0); // hits
		ship.rotate(0, FastMath.DEG_TO_RAD*180, 0);
		ship.updateGeometricState();
		ship.updateModelBound();
		if (MOUSE_CONTROL) {
			ship.setCullHint(CullHint.Always);
		}
		rootNode.attachChild(ship);

		spacestation = new MySpaceStationModel(assetManager);//JMEFunctions.loadModel(assetManager, "Models/missile/missile.obj");
		spacestation.setModelBound(new BoundingBox());
		spacestation.updateModelBound();
		spacestation.setLocalTranslation(0, 0, 0);
		spacestation.rotate(FastMath.DEG_TO_RAD*90, 0, 0);
		rootNode.attachChild(spacestation);

		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		bulletAppState.setDebugEnabled(true);
		bulletAppState.setEnabled(true);
		bulletAppState.getPhysicsSpace().setGravity(Vector3f.ZERO);
		bulletAppState.getPhysicsSpace().addCollisionListener(this);
		bulletAppState.getPhysicsSpace().addTickListener(this);

		//collShape1 = CollisionShapeFactory.createMeshShape(ship1);
		ship_shape = CollisionShapeFactory.createBoxShape(ship);
		//sceneShape1 = CollisionShapeFactory.createBoxShape(ship1);
		ship_control = new RigidBodyControl(ship_shape, 1);
		ship_control.setSpatial(ship);
		ship_control.setKinematic(false);
		ship.addControl(ship_control);
		bulletAppState.getPhysicsSpace().add(ship_control);

		ss_shape = CollisionShapeFactory.createMeshShape(spacestation);
		//collShape2 = CollisionShapeFactory.createDynamicMeshShape(ship2);
		//sceneShape2 = CollisionShapeFactory.createBoxShape(ship2);
		ss_control = new RigidBodyControl(ss_shape, 1f);
		ss_control.setSpatial(spacestation);
		ss_control.setKinematic(true);
		spacestation.addControl(ss_control);
		bulletAppState.getPhysicsSpace().add(ss_control);

		inputManager.setCursorVisible(true);

		this.getFlyByCamera().setMoveSpeed(5f);
		//this.getCamera().s
		setupLight();

		ship_control.applyCentralForce(new Vector3f(0, 0, -2.8f));

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
		//inputManager.setCursorVisible(true);

		if (MOUSE_CONTROL) {
			cam.setLocation(ship.getWorldTranslation());
			cam.setRotation(ship.getLocalRotation());
		}
		//ship.setLocalRotation(cam.getRotation());

		spacestation.rotate(FastMath.DEG_TO_RAD*tpf*4, 0, 0);
		//bulletAppState.update(tpf);


		/*int c = ship_control.getOverlappingCount();
		if (c > 0) {
			//System.out.println("Overlaps: " + c);
		}*/
	}

	@Override
	public void collision(PhysicsCollisionEvent arg0) {
		System.out.println("Collision:" + arg0.getObjectA().getUserObject() + " v " + arg0.getObjectB().getUserObject());
		//ship_control.s
		no_move_timer = 3f;
		//arg0.getDistance1();
		//move_diff = -1;
	}


	@Override
	public void prePhysicsTick(PhysicsSpace space, float tpf){
		//System.out.println("prePhysicsTick");

		no_move_timer -= tpf;

		//no_move_timer -= tpf;
		if (no_move_timer < 0) {
			Vector3f forward = ship.getLocalRotation().mult(Vector3f.UNIT_Z);
			//Vector3f offset = forward.mult(speed);
			ship_control.setLinearVelocity(forward);
		}

	}


	@Override
	public void physicsTick(PhysicsSpace space, float tpf){
		//System.out.println("physicsTick");
		/*int c = ship_control.get.getOverlappingCount(); // Dp
		if (c > 0) {
			System.out.println("physicsTick: " + c);
		}*/
	}

}
