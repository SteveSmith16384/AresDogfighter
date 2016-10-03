package com.scs.aresdogfighter.hud;


public interface ClickEvent {

	void mouseClicked(float x, float y);
	
	float getLocalY(float y); // since BitmapText origin is top-left, quads are bottom-left
}
