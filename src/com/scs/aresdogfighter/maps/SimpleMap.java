package com.scs.aresdogfighter.maps;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.shapes.MyBox;

public class SimpleMap extends Node implements MapLoader {

	//private static final String TEX = "";
	private static final int SIZE= 7;
	private static final String[] MAPDATA = {
			"wwwwwww", 
			"wffwffw", 
			"fffwfff", 
			"wdwwwfw", 
			"wfffffw", 
			"wfffffw", 
			"wwwwwww"};

	boolean done_l[][];
	boolean done_t[][];
	private AssetManager assetManager;

	public SimpleMap(AssetManager _assetManager) {
		super("SimpleMap");

		assetManager = _assetManager;

		// Create floor and ceiling
		MyBox ceiling = new MyBox(SIZE, Statics.WALL_THICKNESS, SIZE, "Ceiling", assetManager, "lab_floor2.png", SIZE, SIZE );
		ceiling.move(0f ,1f, 0f);
		this.attachChild(ceiling);

		MyBox floor = new MyBox(SIZE, Statics.WALL_THICKNESS, SIZE, "Floor", assetManager, "metalfloor1.jpg", SIZE, SIZE );
		//floor.move((SIZE/2) + 0.5f, 0, (SIZE/2) + 0.5f);
		this.attachChild(floor);

		done_l = new boolean[SIZE][SIZE];
		done_t = new boolean[SIZE][SIZE];

		for (int y=0 ; y<SIZE ; y++) {
			for (int x=0 ; x<SIZE ; x++) {
				String sq = GetCharAt(x, y);
				if (sq.equalsIgnoreCase("w")) {
					if (done_l[x][y] == false) {
						runDown(x, y);
					}
					if (done_t[x][y] == false) {
						runRight(x, y);
					}
				}

			}
		}

		this.updateGeometricState();
	}


	private void runDown(int sx, int sy) {
		float dist = 0;
		int x = sx;
		for (int y=sy ; y<SIZE ; y++) {
			String sq = GetCharAt(x, y);
			if (!sq.equalsIgnoreCase("w") || done_l[x][y]) {
				break;
			}
			done_l[x][y] = true;
			dist++;
		}
		if (dist > 1) {
			MyBox wall = new MyBox(Statics.WALL_THICKNESS, 1f, dist, "Wall", assetManager, "spacewall.png", SIZE, SIZE );
			wall.move(sx, 1f, sy);
			this.attachChild(wall);
		}
	}


	private void runRight(int sx, int sy) {
		float dist = 0;
		int y = sy;
		for (int x=sx ; x<SIZE ; x++) {
			String sq = GetCharAt(x, y);
			if (!sq.equalsIgnoreCase("w") || done_t[x][y]) {
				break;
			}
			done_t[x][y] = true;
			dist++;
		}
		if (dist > 1) {
			MyBox wall = new MyBox(dist, 1f, Statics.WALL_THICKNESS, "Wall", assetManager, "spacewall.png", SIZE, SIZE );
			wall.move(sx, 1f, sy);
			this.attachChild(wall);
		}
	}


	private static String GetCharAt(int x, int y) {
		return MAPDATA[y].substring(x, x+1);

	}

}
