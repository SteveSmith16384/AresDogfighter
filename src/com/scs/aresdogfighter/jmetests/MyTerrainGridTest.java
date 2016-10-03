package com.scs.aresdogfighter.jmetests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainGrid;
import com.jme3.terrain.geomipmap.TerrainGridLodControl;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.grid.ImageTileLoader;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.Namer;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

public class MyTerrainGridTest extends SimpleApplication {

	private Material mat_terrain;
	private TerrainGrid terrain;

	public static void main(final String[] args) {
		MyTerrainGridTest app = new MyTerrainGridTest();
		app.showSettings = false;
		app.start();
	}

	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		this.flyCam.setMoveSpeed(100f);
		initMaterial();
		initTerrain();
		this.getCamera().setLocation(new Vector3f(0, 200, 0));
		this.getCamera().lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
		this.viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

		setupLight();
	}


	@Override
	public void simpleUpdate(final float tpf) {
	}


	public void initMaterial() {
		this.mat_terrain = new Material(this.assetManager, "Common/MatDefs/Terrain/HeightBasedTerrain.j3md");
		Texture rock = this.assetManager.loadTexture("Textures/Terrain/Rock2/rock.jpg");
		rock.setWrap(WrapMode.Repeat);
		this.mat_terrain.setTexture("region1ColorMap", rock);
		this.mat_terrain.setVector3("region1", new Vector3f(-10, 0, 64));
	}

	private void initTerrain() {
		int img_size = 256;
		ImageTileLoader itl = new ImageTileLoader(assetManager, new Namer() {
			public String getName(int x, int y) {
				return "Textures/heightmap_canals_257_fixed.png";
				//return "Scenes/TerrainMountains/terrain_1_1.png";

			}
		});
		itl.setHeightScale(.01f);

		this.terrain = new TerrainGrid("terrain", 65, (img_size*2)+1, itl);
		this.terrain.setMaterial(mat_terrain);
		this.terrain.setLocalTranslation(0, 0, 0);
		this.terrain.setLocalScale(3f, .5f, 3f);
		this.rootNode.attachChild(this.terrain);

		TerrainLodControl control = new TerrainGridLodControl(this.terrain, getCamera());
		control.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
		this.terrain.addControl(control);
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
		al.setColor(ColorRGBA.White.mult(.25f));//25f));//.mult(2.5f));
		rootNode.addLight(al);
		al = null; // Avoid me accidentally re-using it

		DirectionalLight dirlight = new DirectionalLight();
		dirlight.setColor(ColorRGBA.White.mult(2f));//.mult(2.5f));
		dirlight.setDirection(new Vector3f(0, 1, 0f)); // Relative to the sun in the skybox 
		rootNode.addLight(dirlight);
		dirlight = null;

	}



}