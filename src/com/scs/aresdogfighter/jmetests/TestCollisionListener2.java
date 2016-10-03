package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.scs.aresdogfighter.model.MySpaceStationModel;

import jme3test.bullet.PhysicsTestHelper;

// http://stackoverflow.com/questions/8996995/jmonkeyengine-collision-detection-on-dynamically-loaded-models
public class TestCollisionListener2 extends SimpleApplication implements PhysicsCollisionListener {

	//private BulletAppState bulletAppState; 

	public static void main(String[] args) {
		TestCollisionListener2 app = new TestCollisionListener2();
		app.showSettings = false;
		app.start(); 
	} 


	@Override 
	public void simpleInitApp() { 
		BulletAppState bulletAppState = new BulletAppState(); 
		stateManager.attach(bulletAppState); 
		bulletAppState.setDebugEnabled(true); 

		PhysicsTestHelper.createPhysicsTestWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace()); 
		PhysicsTestHelper.createBallShooter(this, rootNode, bulletAppState.getPhysicsSpace()); 

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));

        Box box = new Box(1f, 3f, 1f);
        Geometry boxGeometry = new Geometry("Box2", box);
        boxGeometry.setMaterial(material);
        boxGeometry.setLocalTranslation(2, 5, -3);
        //RigidBodyControl automatically uses box collision shapes when attached to single geometry with box mesh
        boxGeometry.addControl(new RigidBodyControl(2));
        rootNode.attachChild(boxGeometry);
        bulletAppState.getPhysicsSpace().add(boxGeometry);
        
        Spatial ss = new MySpaceStationModel(assetManager);
        ss.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().add(ss);
        rootNode.attachChild(ss);

		// add ourselves as collision listener 
		bulletAppState.getPhysicsSpace().addCollisionListener(this); 
	} 

	@Override 
	public void simpleUpdate(float tpf) { 

	} 

	@Override 
	public void simpleRender(RenderManager rm) { 
	} 

	@Override 
	public void collision(PhysicsCollisionEvent event) { 
		/*if ("Box".equals(event.getNodeA().getName()) || "Box".equals(event.getNodeB().getName())) { 
			if ("bullet".equals(event.getNodeA().getName()) || "bullet".equals(event.getNodeB().getName())) { 
				fpsText.setText("You hit the box!"); 
			} 
		} */
		System.out.println("Collision: " + event.getNodeA().getName() + " with " + event.getNodeB().getName());
	} 

}
