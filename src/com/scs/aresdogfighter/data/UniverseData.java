package com.scs.aresdogfighter.data;

import java.awt.Point;
import java.util.List;

import ssmith.lang.NumberFunctions;
/*
public class UniverseData {
	
	private static final int NUM_SYSTEMS = 2;
	public static final int SIZE = 4;
	
	public final SectorData[][] sector = new SectorData[SIZE][SIZE]; // More details sector data
	public Point start_sec;

	public UniverseData() {
		int remaining = 0;
		while (remaining < NUM_SYSTEMS) {
			int x = NumberFunctions.rnd(0, SIZE-1);
			int y = NumberFunctions.rnd(0, SIZE-1);
			if (sector[x][y] == null) {
				if (remaining == 0) {
					start_sec = new Point(x, y);
				}
				sector[x][y] = new SectorData("Sys_"+(remaining+1), x, y);
				remaining++;
			}
		}
	}
	
	
	public List<SpaceEntity> getSectorData(int x, int y) {
		if (sector[x][y] == null) {
			//sector[x][y] = new SectorData(sector[x][y], x, y);
			throw new RuntimeException("Sector is empty");
		}
		if (sector[x][y].entities == null) {
			sector[x][y].generateSectorEntities();
		}
		return sector[x][y].entities;
	}
	
}
*/