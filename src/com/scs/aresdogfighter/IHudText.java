package com.scs.aresdogfighter;

import com.jme3.font.BitmapText;

public interface IHudText {

	void updateHUDText();
	
	BitmapText getHudText(); // Need the by object in case they want to change colour or something

}
