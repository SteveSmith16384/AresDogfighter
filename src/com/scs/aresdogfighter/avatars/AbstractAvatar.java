package com.scs.aresdogfighter.avatars;

import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;

public interface AbstractAvatar {

	GameObject getGameObject();
	
	Node getNode();
	
	void onAnalog(String name, float value, float tpf);

	void onAction(String name, boolean isPressed, float tpf);
	
	//void setStartShooting(boolean b);
}
