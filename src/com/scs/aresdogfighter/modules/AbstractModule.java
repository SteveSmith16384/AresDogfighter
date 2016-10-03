package com.scs.aresdogfighter.modules;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.AresDogfighter;

public abstract class AbstractModule {
	
	public AresDogfighter game;
	public Node rootNode = new Node("sub-rootNode");
	public Node guiNode = new Node("sub-guiNode");
	protected AssetManager assetManager;
	public InputManager inputManager;
	protected Camera cam;
	protected FlyByCamera flyCam;
	protected ViewPort viewPort;
	public BitmapFont guiFont_small, guiFont_large;
	protected AppStateManager stateManager;
	public AresDogfighter.Module module_name;
	
	public AbstractModule(AresDogfighter _game, AresDogfighter.Module _module_name, BitmapFont _guiFont_small, BitmapFont _guiFont_large) {
		super();
		
		game = _game;
		module_name = _module_name;
		guiFont_small = _guiFont_small;
		guiFont_large = _guiFont_large;

		this.assetManager = _game.getAssetManager();
		this.inputManager = _game.getInputManager();
		viewPort = _game.getViewPort();
		this.stateManager = _game.getStateManager();
		cam = _game.getCamera();
		flyCam = game.getFlyByCamera();
		
		
	}
	
	
	protected InputManager getInputManager() {
		return game.getInputManager();
	}
	
	
	public AssetManager getAssetManager() {
		return game.getAssetManager();
	}
	
	public abstract void firstSetup();

	public abstract void unload();

	public abstract void reload();

	public abstract void simpleUpdate(float tpf);

	public abstract void log(String s);

	public abstract void muteChanged(); 
	
}
