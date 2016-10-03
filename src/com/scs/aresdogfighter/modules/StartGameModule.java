package com.scs.aresdogfighter.modules;

import com.jme3.font.BitmapFont;
import com.jme3.input.controls.ActionListener;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.scs.aresdogfighter.AresDogfighter;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.model.PlanetModel;
import com.scs.aresdogfighter.model.SimpleModel;
import com.scs.aresdogfighter.numbermenus.IMenuController;
import com.scs.aresdogfighter.numbermenus.SelectGameTypeMenu;

public class StartGameModule extends AbstractMenuModule implements ActionListener, IMenuController {

	public StartGameModule(AresDogfighter _game, BitmapFont _guiFont_small, BitmapFont _guiFont_large) {
		super(_game, AresDogfighter.Module.START_GAME, _guiFont_small, _guiFont_large);
	}


	@Override
	public void firstSetup() {
		menu = new SelectGameTypeMenu(game, this);

		super.firstSetup();

		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);
		cam.update();

		ship = SimpleModel.GetPlanet3(assetManager);//new PlanetModel(this.assetManager, 20f, true);
		
		ship.setLocalTranslation(0, 0, 10);
		//ship.scale(.4f);
		this.rootNode.attachChild(ship);

		if (Statics.SHOW_LOGO) {
			Picture p = new Picture("p", false);
			//Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
			Texture2D t = (Texture2D)assetManager.loadTexture("Textures/ad_logo.png");
			//mat.setTexture("DiffuseMap", t);
			p.setTexture(assetManager, t, true);
			p.setLocalTranslation(100,  100, 0);
			float w = t.getImage().getWidth();
			if (w > 100-cam.getWidth()) { // Reduce if bigger than the window
				w = cam.getWidth() - 120;
			}
			p.setWidth(w);
			p.setHeight(t.getImage().getHeight());
			this.guiNode.attachChild(p);
		}

	}

	@Override
	public void unload() {
		Statics.SaveProperties();

		super.unload();
	}

}
