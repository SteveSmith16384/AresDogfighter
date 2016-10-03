package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.model.AbstractModel;
import com.scs.aresdogfighter.model.PlanetModelLowPoly;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class Sun extends AbstractPlanet {

	public Sun(ManualShipControlModule game, AssetManager assetManager, BitmapFont guiFont) {
		super(game, SpaceEntity.Type.SUN, "Sun", 40f, assetManager, guiFont, Vector3f.ZERO);

	}


	@Override
	protected AbstractModel loadPlanetModel(AssetManager assetManager, float diam) {
		return new PlanetModelLowPoly(assetManager, diam, false, "sun.jpg");
	}


	@Override
	protected ColorRGBA getHUDColour() {
		return ColorRGBA.Yellow;
	}


	@Override
	public void processHUD(Node node, Vector3f screen_pos, FrustumIntersect insideoutside) {
		super.processHUD(node, screen_pos, insideoutside);  //this

		if (insideoutside == FrustumIntersect.Inside) {
			if (!module.players_avatar.getGameObject().isTargetBehindUs(this)) {
				// Adjust to centre
				Vector3f new_pos = screen_pos.clone();
				new_pos.x -= ManualShipControlModule.CENTRE_X; 
				new_pos.y -= ManualShipControlModule.CENTRE_Y;

				float max_dist = module.getCam().getWidth() / 4;

				double len = Math.sqrt(Math.pow(new_pos.x, 2) + Math.pow(new_pos.y, 2));
				if (len < max_dist) {
					if (module.players_avatar.getGameObject().canSee(this)) {
						module.hud.startSunblindBox((float)((max_dist-len)/max_dist)); // todo - test
					}
				}
			}
		}

	}

}
