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
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.model.KameriExplorer;
import com.scs.aresdogfighter.model.MySpaceStationModel;

// http://davidb.github.io/sandbox_wiki_jme/jme3/advanced/physics.html

/*
 * This class is just for registering a collision using ghostcontrol.
 */
public class PhysicsTest extends SimpleApplication implements PhysicsCollisionListener, PhysicsTickListener {

	private Node ship1, ship2;
	private BulletAppState bulletAppState;
	private GhostControl gc1, gc2;
	private CollisionShape collShape1, collShape2;
	private int move_diff = 1;

	
	public static void main(String[] args){
		PhysicsTest app = new PhysicsTest();
		app.showSettings = false;
		app.start();
	}


	@Override
	public void simpleInitApp() {
	    assetManager.registerLocator("assets/", FileLocator.class); // default
	    
		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		ship1 = new KameriExplorer(assetManager);
		ship1.setModelBound(new BoundingBox());
		ship1.updateModelBound();
		//ship1.setLocalTranslation(-3, 5, 0); // misses
		ship1.setLocalTranslation(0, 5, 0);
		ship1.updateGeometricState();
		ship1.updateModelBound();
		rootNode.attachChild(ship1);

		ship2 = new MySpaceStationModel(assetManager);//JMEFunctions.loadModel(assetManager, "Models/missile/missile.obj");
		ship2.setModelBound(new BoundingBox());
		ship2.updateModelBound();
		ship2.setLocalTranslation(0, 0, 0);
		rootNode.attachChild(ship2);

		bulletAppState = new BulletAppState();
	    stateManager.attach(bulletAppState);
	    bulletAppState.setDebugEnabled(true);
	    bulletAppState.setEnabled(true);
	    bulletAppState.getPhysicsSpace().addCollisionListener(this);
	    bulletAppState.getPhysicsSpace().addTickListener(this);
	    
	    //collShape1 = CollisionShapeFactory.createMeshShape(ship1);
	    collShape1 = new SphereCollisionShape(1.1f);//collShape1 = CollisionShapeFactory.createDynamicMeshShape(ship1);
	    //sceneShape1 = CollisionShapeFactory.createBoxShape(ship1);
	    gc1 = new GhostControl(collShape1);
	    gc1.setSpatial(ship1);
	    ship1.addControl(gc1);
	    bulletAppState.getPhysicsSpace().add(gc1);
	    
	    collShape2 = CollisionShapeFactory.createMeshShape(ship2);
	    //collShape2 = CollisionShapeFactory.createDynamicMeshShape(ship2);
	    //sceneShape2 = CollisionShapeFactory.createBoxShape(ship2);
	    gc2 = new GhostControl(collShape2);
	    gc2.setSpatial(ship2);
	    ship2.addControl(gc2);
	    bulletAppState.getPhysicsSpace().add(gc2);
	    
		inputManager.setCursorVisible(true);
		
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
		//inputManager.setCursorVisible(true);
		
		//ship1.move(tpf*move_diff, 0, 0);
		ship2.rotate(FastMath.DEG_TO_RAD*tpf*4, 0, 0);
		bulletAppState.update(tpf);
		
		int c = gc1.getOverlappingCount();
		if (c > 0) {
			//System.out.println("Overlaps: " + c);
		}
	}

	@Override
	public void collision(PhysicsCollisionEvent arg0) {
		System.out.println("Collision:" + arg0);
		//arg0.getDistance1();
		move_diff = -1;
	}

	
	@Override
	public void prePhysicsTick(PhysicsSpace space, float tpf){
	  // apply state changes ...
	}
	
	
	@Override
	public void physicsTick(PhysicsSpace space, float tpf){
		/*int c = gc1.getOverlappingCount(); // Dp
		if (c > 0) {
			System.out.println("physicsTick: " + c);
		}*/
	}

}
