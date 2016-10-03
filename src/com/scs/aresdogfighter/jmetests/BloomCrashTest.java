package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.FogFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.scs.aresdogfighter.Statics;

public class BloomCrashTest extends SimpleApplication implements ActionListener {

	private FilterPostProcessor fpp_laser_bloom, fpp_std_bloom, fogPPS;

	public static void main(String[] args){
    	BloomCrashTest app = new BloomCrashTest();
    	app.showSettings = false;
        app.start();
    }

	
    @Override
    public void simpleInitApp() {
    	// Create filters
		fpp_laser_bloom = new FilterPostProcessor(assetManager);
		BloomFilter bloom2 = new BloomFilter(BloomFilter.GlowMode.Objects);
		bloom2.setBlurScale(5f);
		fpp_laser_bloom.addFilter(bloom2);

		fpp_std_bloom = new FilterPostProcessor(assetManager);
		BloomFilter bloom = new BloomFilter();
		bloom.setBlurScale(2f);//5f);
		bloom.setBloomIntensity(1.5f);//2f);
		fpp_std_bloom.addFilter(bloom);

		fogPPS = new FilterPostProcessor(assetManager);
		FogFilter fog = new FogFilter(ColorRGBA.White, .2f, Statics.CAM_VIEW_DIST);
		fogPPS.addFilter(fog);

		// Add them to the viewport
		viewPort.addProcessor(fpp_laser_bloom);
		viewPort.addProcessor(fpp_std_bloom);
		viewPort.addProcessor(fogPPS);

		Box b = new Box(1, 1, 1); // create cube shape
        Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
        Material mat = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
        geom.setMaterial(mat);                   // set the cube's material
        rootNode.attachChild(geom);              // make the cube appear in the scene
        
		inputManager.addMapping("Crash", new KeyTrigger(KeyInput.KEY_C));
		inputManager.addListener(this, "Crash");

    }
    
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("Crash") && isPressed) {
			System.out.println("Removing filters...");
			
			viewPort.removeProcessor(fpp_laser_bloom);
			viewPort.removeProcessor(fogPPS);
			viewPort.removeProcessor(fpp_std_bloom);

			System.out.println("Finished removing filters");
		}
	}
	
	
}