package com.scs.aresdogfighter.modules;

import com.jme3.font.BitmapFont;
import com.jme3.input.controls.ActionListener;
import com.scs.aresdogfighter.AresDogfighter;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.model.MySpaceStationModel;
import com.scs.aresdogfighter.numbermenus.IMenuController;
import com.scs.aresdogfighter.numbermenus.SpaceStationMenu;

public class DockedModule extends AbstractMenuModule implements ActionListener, IMenuController {

	public DockedModule(AresDogfighter _game, BitmapFont _guiFont_small, BitmapFont _guiFont_large) {
		super(_game, AresDogfighter.Module.DOCKING, _guiFont_small, _guiFont_large);
		
	}


	@Override
	public void firstSetup() {
		menu = new SpaceStationMenu(game, this);

		super.firstSetup();
		
		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);
		cam.update();

		ship = new MySpaceStationModel(this.assetManager);
		ship.setLocalTranslation(0, 0, 10);
		ship.scale(.3f);
		this.rootNode.attachChild(ship);

	}

}
