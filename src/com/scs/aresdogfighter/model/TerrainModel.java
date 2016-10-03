package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.terrain.geomipmap.TerrainGrid;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.grid.ImageTileLoader;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.terrain.heightmap.Namer;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.aresdogfighter.Statics;

public class TerrainModel extends AbstractModel {

	public TerrainGrid terrainquad;
	//public TerrainQuad terrainquad;

	public TerrainModel(AssetManager assetManager) {
		super("TerrainModel");

		/** 1. Create terrain material and load four textures into it. */
		Material mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");

		// 1.1) Add ALPHA map (for red-blue-green coded splat textures) 
		mat_terrain.setTexture("Alpha", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));
		//mat_terrain.setTexture("Alpha", assetManager.loadTexture("Textures/alpha01.jpg"));

		// 1.2) Add GRASS texture into the red layer (Tex1). 
		Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
		//Texture grass = assetManager.loadTexture("Textures/sand2.png");
		grass.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex1", grass);
		mat_terrain.setFloat("Tex1Scale", 64f);

		// 1.3) Add DIRT texture into the green layer (Tex2) 
		Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
		dirt.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex2", dirt);
		mat_terrain.setFloat("Tex2Scale", 32f);

		// 1.4) Add ROAD texture into the blue layer (Tex3) 
		Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
		//Texture rock = assetManager.loadTexture("Textures/sand1.png");
		rock.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex3", rock);
		mat_terrain.setFloat("Tex3Scale", 128f);

		// 2. Create the height map 
		AbstractHeightMap heightmap = null;
		Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");
		heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
		heightmap.load();

		/** 3. We have prepared material and heightmap. 
		 * Now we create the actual terrain:
		 * 3.1) Create a TerrainQuad and name it "my terrain".
		 * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
		 * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
		 * 3.4) As LOD step scale we supply Vector3f(1,1,1).
		 * 3.5) We supply the prepared heightmap itself.
		 */

		/*Texture grass = assetManager.loadTexture("Textures/sand2.png");
		grass.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex1", grass);
		mat_terrain.setFloat("Tex1Scale", 64f);
		*/
		/*if (Statics.USE_TERRAIN_GRID == false) {
			terrainquad = new TerrainQuad("my terrain", 65, 513, heightmap.getHeightMap());
		} else {*/
			int img_size = 256;
			this.terrainquad = new TerrainGrid("terrain", 65, (img_size*2)+1, new ImageTileLoader(assetManager, new Namer() {
				public String getName(int x, int y) {
					//return "Interface/Scenes/TerrainMountains/terrain_" + x + "_" + y + ".png";
					return "Textures/heightmap_canals_257_fixed.png";
				}
			}));
		//}


		/** 4. We give the terrain its material, position & scale it, and attach it. */
		terrainquad.setMaterial(mat_terrain);
		terrainquad.setLocalScale(2f, .1f, 2f);

		this.attachChild(terrainquad);

	}


}
