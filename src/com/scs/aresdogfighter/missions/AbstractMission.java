package com.scs.aresdogfighter.missions;

import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.data.SectorData;

public abstract class AbstractMission implements IProcessable {
	
	public enum Type {
		ESCORT,
		TRANSPORT;
	}
	
	public boolean actually_started = false;

	
	public abstract SectorData getSectorData();
}
