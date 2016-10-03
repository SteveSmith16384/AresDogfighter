package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.model.FederationInterceptor;
import com.scs.aresdogfighter.model.KameriExplorer;
import com.scs.aresdogfighter.shapes.MyWireframe;

public class CollisionTest extends SimpleApplication implements PhysicsCollisionListener {

	private Node airplaneModel1, airplaneModel2;
	private BulletAppState bulletAppState;
	
	public static void main(String[] args){
		CollisionTest app = new CollisionTest();
		app.showSettings = false;
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default
		cam.setFrustumPerspective(55, settings.getWidth() / settings.getHeight(), .1f, 1000);

		bulletAppState = new BulletAppState(); 
		stateManager.attach(bulletAppState); 
		bulletAppState.setDebugEnabled(true); 

		setupLight();

		airplaneModel1 = new FederationInterceptor(assetManager);
		airplaneModel1.setModelBound(new BoundingBox());
		airplaneModel1.updateModelBound();
		airplaneModel1.setLocalTranslation(-5, 0, 0);
		airplaneModel1.rotate(FastMath.PI/2, 0, 0);
		airplaneModel1.updateGeometricState();
		airplaneModel1.updateModelBound();
		MyWireframe wf = new MyWireframe(airplaneModel1.getWorldBound(), "bbox", assetManager);
		airplaneModel1.attachChild(wf);
		rootNode.attachChild(airplaneModel1);
		airplaneModel1.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().add(airplaneModel1);

		airplaneModel2 = new KameriExplorer(assetManager);//JMEFunctions.loadModel(assetManager, "Models/missile/missile.obj");
		airplaneModel2.setModelBound(new BoundingBox());
		airplaneModel2.updateModelBound();
		airplaneModel2.setLocalTranslation(5, 0, 0);
		MyWireframe wf2 = new MyWireframe(airplaneModel2.getWorldBound(), "bbox", assetManager);
		airplaneModel2.attachChild(wf2);
		rootNode.attachChild(airplaneModel2);
		airplaneModel2.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().add(airplaneModel2);

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));
		
		this.flyCam.setMoveSpeed(3.5f);

		inputManager.setCursorVisible(true);

		// add ourselves as collision listener 
		bulletAppState.getPhysicsSpace().addCollisionListener(this); 
	}


	private void setupLight() {
		// Remove existing lights
		this.rootNode.getWorldLightList().clear(); //this.rootNode.getWorldLightList().size();
		LightList list = this.rootNode.getWorldLightList();
		for (Light it : list) {
			this.rootNode.removeLight(it);
		}

		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White);//.mult(2.5f));
		rootNode.addLight(al);

		DirectionalLight dirlight = new DirectionalLight();
		rootNode.addLight(dirlight);

	}


	@Override
	public void simpleUpdate(float tpf) {
		//System.out.println("Pos: " + this.cam.getLocation());
		airplaneModel1.move(tpf,  0,  0);
		airplaneModel1.updateGeometricState();
		// bulletAppState.getPhysicsSpace()..update(tpf);
		
		CollisionResults results = new CollisionResults();
		airplaneModel1.collideWith(airplaneModel2.getWorldBound(), results);
		if (results.size() > 0) {
			System.out.println("Collided!");
		}
		
	}


	@Override
	public void collision(PhysicsCollisionEvent event) {
		System.out.println("Collision: " + event.getNodeA().getName() + " with " + event.getNodeB().getName());
	}

	
}
