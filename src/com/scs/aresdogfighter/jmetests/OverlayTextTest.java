package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.shapes.MySphere;

public class OverlayTextTest extends SimpleApplication {

	private MySphere planet2;
	private BitmapText hudText;
	
	public static void main(String[] args){
		OverlayTextTest app = new OverlayTextTest();
		app.start();
	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		setupLight();

		// Bloom
		/*FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
		BloomFilter bloom = new BloomFilter();
		bloom.setBlurScale(5f);
		bloom.setBloomIntensity(2f);
		fpp.addFilter(bloom);
		viewPort.addProcessor(fpp);
*/

		planet2 = new MySphere(5f, 30, 30, "planet", this.getAssetManager(), "sand2.png");
		planet2.setLocalTranslation(0f, 0f, 0f);
		this.rootNode.attachChild(planet2);

		hudText = new BitmapText(guiFont, false);          
		hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
		hudText.setColor(ColorRGBA.Blue);                             // font color
		hudText.setText("You can write any string here");             // the text
		hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
		guiNode.attachChild(hudText);
		
		this.rootNode.attachChild(JMEFunctions.GetGrid(assetManager, 10));

		this.flyCam.setMoveSpeed(2.5f);
		cam.setLocation(new Vector3f(0, -10, 0));
		cam.lookAt(planet2.getLocalTranslation(), Vector3f.UNIT_Y); // Look at something at start
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

		planet2.rotate(FastMath.DEG_TO_RAD, 0, 0);
		
		Vector3f pos = cam.getScreenCoordinates(planet2.getWorldTranslation());
		hudText.setLocalTranslation(pos.x, pos.y, 0);


	}

}


