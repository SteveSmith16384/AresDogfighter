package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;

public class BillboardTest extends SimpleApplication {

	private Geometry wormhole1, wormhole2;
	
	public static void main(String[] args){
		BillboardTest app = new BillboardTest();
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		setupLight();

		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		// Create red transparent material
		//Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		//mat.setColor("Color", new ColorRGBA(1, 0, 0, 0.5f)); // 0.5f is the alpha value

		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

		// Create rectangle of size 10x10
		wormhole1 = new Geometry("Billboard", new Quad(10f, 10f));
		Texture t = assetManager.loadTexture("Textures/laser_bolt.png");//wormhole_alpha.png");
		t.setWrap(WrapMode.Repeat);
		mat.setTexture("DiffuseMap", t);
		wormhole1.setMaterial(mat);
		wormhole1.setLocalTranslation(0f, 0f, 0f);
		rootNode.attachChild(wormhole1);
		wormhole1.setQueueBucket(Bucket.Transparent);


		wormhole2 = new Geometry("Billboard", new Quad(10f, 10f));
		//Texture t = assetManager.loadTexture("Textures/wormhole_alpha.png");
		t.setWrap(WrapMode.Repeat);
		mat.setTexture("DiffuseMap", t);
		wormhole2.setMaterial(mat);
		wormhole2.setLocalTranslation(5f, 10f, 0f);
		rootNode.attachChild(wormhole2);
		wormhole2.setQueueBucket(Bucket.Transparent);
	
		rootNode.updateGeometricState();

		this.flyCam.setMoveSpeed(2.5f);
		cam.setLocation(new Vector3f(0, -10, 0));
		cam.lookAt(wormhole1.getLocalTranslation(), Vector3f.UNIT_Y); // Look at something at start
		cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), .1f, 1000);

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

	}


	@Override
	public void simpleUpdate(float tpf) {
		//inputManager.setCursorVisible(true);

		wormhole1.lookAt(cam.getLocation(), Vector3f.UNIT_Y);
		wormhole2.lookAt(cam.getLocation(), Vector3f.UNIT_Y);

	}

}


