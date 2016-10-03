package com.scs.aresdogfighter.model;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.scs.aresdogfighter.gui.TextArea;

public class HUDModelWithConsole extends HUDModel {
	
	public TextArea ta_left, ta_right;

	public HUDModelWithConsole(AssetManager assetManager, BitmapFont guiFont) {
		super(assetManager);
		
		// Console1
		ta_right = new TextArea("textarea", guiFont, 5, "Can you read this?\nLine 2\nLine3", false);
		ta_right.scale(.009f);
		//textarea.setLocalTranslation(-.2f, -.4f, 2.4f);
		ta_right.setLocalTranslation(3.5f, 1.0f, -1.0f);
		ta_right.rotate(new Quaternion().fromAngleAxis(-FastMath.PI*1.25f, Vector3f.UNIT_Y));
		this.attachChild(ta_right);
		
		// Console2
		ta_left = new TextArea("textarea", guiFont, 5, "Enemy locked on\nLine 2\nLine 3", false);
		ta_left.scale(.009f);
		//textarea2.setLocalTranslation(0.7f, -.4f, 1.8f);
		ta_left.setLocalTranslation(6.9f, 1f, -1f);
		ta_left.rotate(new Quaternion().fromAngleAxis(FastMath.PI*1.25f, Vector3f.UNIT_Y));
		this.attachChild(ta_left);

	}

}
