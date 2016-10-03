package com.scs.aresdogfighter;

import java.util.HashMap;
import java.util.prefs.BackingStoreException;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;
import com.scs.aresdogfighter.data.GameData;
import com.scs.aresdogfighter.modules.AbstractModule;
import com.scs.aresdogfighter.modules.DockedModule;
import com.scs.aresdogfighter.modules.ManualShipControlModule;
import com.scs.aresdogfighter.modules.StartGameModule;

public class AresDogfighter extends SimpleApplication {

	public enum Module {
		START_GAME, 
		MANUAL_SHIP_FLYING,
		DOCKING,
	}
	
	public AbstractModule curr_module;
	public GameData game_data = new GameData();
	protected BitmapFont guiFont_small;
	private HashMap<Module, AbstractModule> modules = new HashMap<Module, AbstractModule>();

	
	public static void main(String[] args) {
		AppSettings settings = new AppSettings(true);
		try {
			settings.load(Statics.NAME);
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		settings.setTitle(Statics.NAME + " (v" + Statics.VERSION + ")");
		if (Statics.SHOW_LOGO) {
			settings.setSettingsDialogImage("/ad_logo.png");
		} else {
			settings.setSettingsDialogImage(null);
		}

		AresDogfighter app = new AresDogfighter();
		app.setSettings(settings);
		app.setPauseOnLostFocus(true);
		app.start();

	}


	@Override
	public void simpleInitApp() {
		try {
			this.settings.save(Statics.NAME);
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

		if (!Statics.DEBUG) {
			stateManager.getState(StatsAppState.class).toggleStats(); // Turn off stats
		}

		assetManager.registerLocator("assets/", FileLocator.class); // default

		// For the jar
		assetManager.registerLocator("Models/", FileLocator.class);
		assetManager.registerLocator("Textures/", FileLocator.class);
		assetManager.registerLocator("Sound/", FileLocator.class);
		assetManager.registerLocator("Shaders/", FileLocator.class);

		guiFont_small = assetManager.loadFont("Interface/Fonts/Console.fnt");

		cam.setFrustumPerspective(Statics.FOV_ANGLE, settings.getWidth() / settings.getHeight(), .1f, Statics.CAM_VIEW_DIST);

		this.selectModule(Module.START_GAME, false);

		new PreloadModels(this.assetManager).start();

        this.viewPort.setBackgroundColor(ColorRGBA.Black);

	}


	public void selectModule(Module mod, boolean keep_current) {
		if (this.curr_module != null) {
			this.curr_module.unload();
			if (keep_current) {
				this.modules.put(this.curr_module.module_name, this.curr_module);
			} else {
				this.modules.remove(curr_module.module_name);
			}
		}

		AbstractModule module = null;
		boolean first_setup = false;
		if (this.modules.containsKey(mod) == false) {
			switch (mod) {
			case START_GAME:
				module = new StartGameModule(this, this.guiFont_small, this.guiFont);
				break;
			case MANUAL_SHIP_FLYING:
				module = new ManualShipControlModule(this, this.guiFont_small, this.guiFont);
				break;
			case DOCKING:
				module = new DockedModule(this, this.guiFont_small, guiFont);
				break;
			default:
				throw new RuntimeException("Unknown type:" + mod);
			}
			first_setup = true;
		} else  {
			module = this.modules.get(mod);
		}
		this.curr_module = module;

		this.rootNode.detachAllChildren();
		this.guiNode.detachAllChildren();

		this.inputManager.clearMappings();

		if (first_setup) {
			curr_module.firstSetup();
		}
		
		curr_module.reload();
		this.rootNode.attachChild(curr_module.rootNode);
		this.guiNode.attachChild(curr_module.guiNode);

	}


	@Override
	public void simpleUpdate(float tpf) {
		if (tpf > 1) {
			if (Statics.DEBUG) {
				curr_module.log("Slow tpf (" + tpf + ")");
			}
			tpf = 1;
		}

		curr_module.simpleUpdate(tpf);
	}

}


