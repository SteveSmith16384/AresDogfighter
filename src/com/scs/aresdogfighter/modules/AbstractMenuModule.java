package com.scs.aresdogfighter.modules;

import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.scs.aresdogfighter.AresDogfighter;
import com.scs.aresdogfighter.AresDogfighter.Module;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.gui.TextArea;
import com.scs.aresdogfighter.numbermenus.AbstractNumberMenu;
import com.scs.aresdogfighter.numbermenus.IMenuController;

public abstract class AbstractMenuModule extends AbstractModule implements ActionListener, IMenuController {

	protected static final float INSETS = 20;

	protected AbstractNumberMenu menu;
	protected TextArea title;
	protected TextArea textarea;
	protected Spatial ship;

	private AudioNode music_node;

	public AbstractMenuModule(AresDogfighter _game, Module module, BitmapFont _guiFont_small, BitmapFont _guiFont_large) {
		super(_game, module, _guiFont_small, _guiFont_large);
	}


	@Override
	public void firstSetup() {
		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);
		cam.update();

		music_node = new AudioNode(assetManager, "Sound/music_2.wav", true);
		music_node.setLooping(false);
		music_node.setPositional(false);
		this.rootNode.attachChild(music_node);

		title = new TextArea("title", super.guiFont_large, -1, menu.getTitle(), false);
		title.setLocalTranslation(INSETS, cam.getHeight()-INSETS, 0);
		guiNode.attachChild(title);

		textarea = new TextArea("textarea", guiFont_large, -1, menu.getOptions(), false);
		textarea.setLocalTranslation(INSETS, cam.getHeight() * .9f, 0);
		guiNode.attachChild(textarea);

		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);
		cam.update();

	}


	protected void setupInputs() {
		this.inputManager.clearMappings();

		inputManager.addMapping("0", new KeyTrigger(KeyInput.KEY_0));
		inputManager.addMapping("1", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping("2", new KeyTrigger(KeyInput.KEY_2));
		inputManager.addMapping("3", new KeyTrigger(KeyInput.KEY_3));
		inputManager.addMapping("4", new KeyTrigger(KeyInput.KEY_4));
		inputManager.addMapping("5", new KeyTrigger(KeyInput.KEY_5));
		inputManager.addMapping("6", new KeyTrigger(KeyInput.KEY_6));
		inputManager.addMapping("7", new KeyTrigger(KeyInput.KEY_7));
		inputManager.addMapping("8", new KeyTrigger(KeyInput.KEY_8));
		inputManager.addMapping("M", new KeyTrigger(KeyInput.KEY_M));
		inputManager.addMapping("Esc", new KeyTrigger(KeyInput.KEY_ESCAPE));

		inputManager.addListener(this, "0", "1", "2", "3", "4", "5", "6", "7", "8", "M", "Esc");

	}


	@Override
	public void reload() {
		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);
		cam.update();

		this.setupInputs();
		this.setupLight();

		cam.setLocation(new Vector3f(0, 0, 0));
		this.cam.lookAt(ship.getWorldTranslation(), Vector3f.UNIT_Y);

		if (!Statics.MUTE) {
			music_node.play();
		}

		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);
		cam.update();

		this.viewPort.setBackgroundColor(ColorRGBA.Black);

	}


	protected void setupLight() {
		// Remove existing lights
		this.rootNode.getWorldLightList().clear(); //this.rootNode.getWorldLightList().size();
		LightList list = this.rootNode.getWorldLightList();
		for (Light it : list) {
			this.rootNode.removeLight(it);
		}

		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(.5f));//25f));//.mult(2.5f));
		rootNode.addLight(al);
		al = null; // Avoid me accidentally re-using it

		DirectionalLight dirlight = new DirectionalLight();
		dirlight.setColor(ColorRGBA.White.mult(2f));//.mult(2.5f));
		dirlight.setDirection(new Vector3f(0, 0, 1f)); // Relative to the sun in the skybox 
		rootNode.addLight(dirlight);
		dirlight = null;

	}


	@Override
	public void unload() {
		if (Statics.MUSIC_DURING_GAME == false) {
			music_node.stop();
		}
	}


	@Override
	public void simpleUpdate(float tpf) {
		ship.rotate(tpf/6, tpf/6, 0); //this.rootNode
		//ship.rotate(0, tpf/6, 0); //this.rootNode
		this.cam.lookAt(ship.getWorldTranslation(), Vector3f.UNIT_Y);
	}


	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (!isPressed) {
			/*if (name.equalsIgnoreCase("Esc")) {
				game.stop(false);
			} else {*/
			menu.optionSelected(name);
			//}
			this.textarea.setText(menu.getOptions()); // reload menu text as it might have changed
		}

	}


	@Override
	public void log(String s) {
		System.out.println(s);
	}


	@Override
	public void muteChanged() {
		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);
		cam.update();

		if (!Statics.MUTE) {
			music_node.play();
		} else {
			music_node.stop();
		}
	}



	@Override
	public void showMenu(AbstractNumberMenu _menu) {
		menu = _menu;
		title.setText(menu.getTitle());
		this.textarea.setText(menu.getOptions());

	}
}
