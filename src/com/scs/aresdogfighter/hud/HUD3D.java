package com.scs.aresdogfighter.hud;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.model.HUDModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class HUD3D extends Node implements IProcessable {
	
	private ManualShipControlModule module;
	public HUDModel hud_node;
	
	public HUD3D(ManualShipControlModule _module, AssetManager assetManager, BitmapFont guiFont) {
		super("HUD3D");
		
		module = _module;

		hud_node = new HUDModel(assetManager);
		hud_node.scale(.3f);
		hud_node.setLocalTranslation(-1.34f, -.75f, 2);
		//cam_node = new Node("node");
		this.attachChild(hud_node);

	}

	
	@Override
	public void process(float tpf) {
		module.getCam().update();
		this.setLocalTranslation(module.getCam().getLocation());
		this.setLocalRotation(module.getCam().getRotation());
		
	}

	
	public void showFrame(boolean show) {
		if (show && hud_node.getParent() == null) {
			this.attachChild(hud_node);
		} else if (!show && hud_node.getParent() != null) {
			this.hud_node.removeFromParent();
		}
	}
}
