package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class JMECollisionTest extends SimpleApplication implements PhysicsCollisionListener {

	private Geometry ship1, ship2;
	private BulletAppState bulletAppState;
	private RigidBodyControl rbc1, rbc2;
	private CollisionShape sceneShape1, sceneShape2;

	public static void main(String[] args){
		JMECollisionTest app = new JMECollisionTest();
		app.showSettings = false;
		app.start();
	}


	@Override
	public void simpleInitApp() {
	    assetManager.registerLocator("assets/", FileLocator.class); // default
	    
	    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	    mat.setColor("Color", ColorRGBA.Red);

	    Box b1 = new Box(1f, 1f, 1f);
		ship1 = new Geometry();
		ship1.setMaterial(mat);
		ship1.setMesh(b1);
		ship1.setModelBound(new BoundingBox());
		ship1.updateModelBound();
		ship1.setLocalTranslation(0, 0, 0);
		ship1.updateGeometricState();
		ship1.updateModelBound();
		rootNode.attachChild(ship1);

	    Box b2 = new Box(1f, 1f, 1f);
		ship2 = new Geometry();
		ship2.setMaterial(mat);
		ship2.setMesh(b2);
		ship2.setModelBound(new BoundingBox());
		ship2.updateModelBound();
		ship2.setLocalTranslation(.5f, .5f, .5f);
		rootNode.attachChild(ship2);

		bulletAppState = new BulletAppState();
	    stateManager.attach(bulletAppState);
	    bulletAppState.setDebugEnabled(true);
	    bulletAppState.setEnabled(true);
	    bulletAppState.getPhysicsSpace().addCollisionListener(this);
	    
	    sceneShape1 = CollisionShapeFactory.createDynamicMeshShape(ship1);
	    rbc1 = new RigidBodyControl(sceneShape1, 1);
	    rbc1.setSpatial(ship1);
	    rbc1.setKinematic(true);
	    ship1.addControl(rbc1);
	    bulletAppState.getPhysicsSpace().add(rbc1);
	    
	    sceneShape2 = CollisionShapeFactory.createDynamicMeshShape(ship2);
	    rbc2 = new RigidBodyControl(sceneShape2, 1);
	    rbc2.setSpatial(ship2);
	    rbc2.setKinematic(true);
	    ship2.addControl(rbc2);
	    bulletAppState.getPhysicsSpace().add(rbc2);
	    
		inputManager.setCursorVisible(true);

	}

	@Override
	public void simpleUpdate(float tpf) {
		bulletAppState.update(tpf);
		rbc1.update(tpf);
		rbc2.update(tpf);
		bulletAppState.getPhysicsSpace().distributeEvents();
	}

	@Override
	public void collision(PhysicsCollisionEvent arg0) {
		System.out.println(arg0);
	}


}
