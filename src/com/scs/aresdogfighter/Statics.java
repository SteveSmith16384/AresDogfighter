package com.scs.aresdogfighter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.jme3.math.ColorRGBA;
import com.scs.aresdogfighter.model.SimpleModel;

public class Statics {

	public static final double VERSION = 0.05;
	public static final boolean SCENIC_INTRO = false;
	public static final boolean SHOW_LOGO = false;
	public static final boolean HIDE_MOUSE = false;
	public static final boolean DEBUG_AI = true;
	public static final boolean DEBUG = true;
	public static final boolean STRICT = true;
	public static final boolean SHIP_TRAILS = false;
	public static final boolean AI_SHOOTS = true;
	public static final boolean SHIPS_GET_DAMAGE = true;
	public static final boolean USE_LOD = false;
	//public static final boolean LAND_ON_PLANET = false;
	public static final boolean USE_LOWPOLY = true;

	public static final int MAX_ENEMY_FIGHTERS = 1; // todo - change
	public static final int MAX_ASTEROIDS = 10;
	public static final int EVENT_INTERVAL = 60;//*10000;
	public static final float MAX_SCANNER_RANGE = 600f;
	public static final float DOCK_DIST = 30f;
	public static final float OBJ_CREATION_DIST = 1000f;
	public static final float START_CAM_VIEW_DIST = 25f;
	public static final float ENEMY_CHECK_INT = 5;
	public static final float SCAN_DURATION = 5f;
	public static final float TARGET_DURATION = 2f;
	public static final float WARNING_DISTANCE = 20f;
	public static final float WEAPON_COOLDOWN = 0.08f;//0.06f;
	public static final float SHIELD = 0.04f;
	public static final float RETURN_TO_SPACE_DIST = 150f;
	public static final float CAM_VIEW_DIST = 400;
	
	public static final float WALL_THICKNESS = 0.05f;
	public static final float SMALL_EXPLOSION_DURATION = .5f;
	public static final float BIG_EXPLOSION_DURATION = 5;
		
	//public static final String WEAK_BADDIES_NAME = "Kryxix";
	//public static final String STRONG_BADDIES_NAME = "Syylk";
	public static final String NAME = "Ares: Dogfighter";
	public static final ColorRGBA SKY_COL = new ColorRGBA(.2f, .2f, .9f, 1f);

	// Properties/simple game data
	public static final String PROP_FILENAME = "ares_config.txt";
	public static Properties props = new Properties();
	public static boolean MUTE = false;
	public static int MAX_ENEMIES = 1;
	public static boolean SIMPLE_GRAPHICS = true;
	public static boolean INVERSE_PITCH = false;
	public static int ENEMY_DIFFICULTY = 1;
	public static int NUM_ENEMIES = 1;
	public static boolean MUSIC_DURING_GAME = false;
	public static float MOUSE_SENSITIVITY = 0.9f;
	public static int FOV_ANGLE = 60;
	public static int CREDITS = 0;
	public static int CONFIG_VERSION = 0;
	public static int NUM_WINGMEN = 0;
	public static int GAME_TYPE = 0;

	// Game types
	public static final int GT_DOGFIGHT = 0;
	public static final int GT_ADVENTURE = 1;
	
	static {
		LoadProperties();
	}
	
	
	private static void LoadProperties() {
		try {
			props.load(new FileInputStream(new File(Statics.PROP_FILENAME)));
		} catch (FileNotFoundException e) {
			// Do nothing
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			MUTE = Boolean.parseBoolean(props.getProperty("mute"));
		} catch (Exception ex) {
		}
		try {
			SIMPLE_GRAPHICS = Boolean.parseBoolean(props.getProperty("simple_models"));
		} catch (Exception ex) {
		}
		try {
			MAX_ENEMIES = Integer.parseInt(props.getProperty("max_enemies"));
		} catch (Exception ex) {
		}
		try {
			INVERSE_PITCH = Boolean.parseBoolean(props.getProperty("inverse_pitch"));
		} catch (Exception ex) {
		}
		try {
			ENEMY_DIFFICULTY = Integer.parseInt(props.getProperty("enemy_difficulty"));
		} catch (Exception ex) {
		}
		try {
			NUM_ENEMIES = Integer.parseInt(props.getProperty("num_enemies"));
		} catch (Exception ex) {
		}
		try {
			MUSIC_DURING_GAME = Boolean.parseBoolean(props.getProperty("music_during_game"));
		} catch (Exception ex) {
		}
		try {
			MOUSE_SENSITIVITY = (float)Integer.parseInt(props.getProperty("mouse_sensitivity")) / 100f;
		} catch (Exception ex) {
		}
		try {
			FOV_ANGLE = Integer.parseInt(props.getProperty("fov_angle"));
		} catch (Exception ex) {
		}
		try {
			CREDITS = Integer.parseInt(props.getProperty("credits"));
		} catch (Exception ex) {
		}
		try {
			CONFIG_VERSION = Integer.parseInt(props.getProperty("config_version"));
		} catch (Exception ex) {
		}
		try {
			NUM_WINGMEN = Integer.parseInt(props.getProperty("num_wingmen"));
		} catch (Exception ex) {
		}
		try {
			GAME_TYPE = Integer.parseInt(props.getProperty("game_type"));
		} catch (Exception ex) {
		}


	}
	
	
	public static void SaveProperties() {
		try {
			props.setProperty("mute", ""+MUTE);
			props.setProperty("max_enemies", ""+MAX_ENEMIES);
			props.setProperty("simple_models", ""+SIMPLE_GRAPHICS);
			props.setProperty("inverse_pitch", ""+INVERSE_PITCH);
			props.setProperty("enemy_difficulty", ""+ENEMY_DIFFICULTY);
			props.setProperty("num_enemies", ""+NUM_ENEMIES);
			props.setProperty("music_during_game", ""+MUSIC_DURING_GAME);
			props.setProperty("mouse_sensitivity", ""+MOUSE_SENSITIVITY);
			props.setProperty("fov_angle", ""+FOV_ANGLE);
			props.setProperty("credits", ""+CREDITS);
			props.setProperty("config_version", ""+CONFIG_VERSION);
			props.setProperty("num_wingmen", ""+NUM_WINGMEN);
			props.setProperty("game_type", ""+GAME_TYPE);
			props.store(new FileOutputStream(new File(Statics.PROP_FILENAME)), Statics.NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void Debug(String s) {
		if (Statics.DEBUG || Statics.DEBUG_AI) {
			System.out.println("DEBUG: " + s);
		}
	}


	public static String GetGameTypeAsString() {
		switch (GAME_TYPE) {
		case GT_DOGFIGHT: return "Dogfight";
		case GT_ADVENTURE: return "Adventure";
		default: throw new RuntimeException("Unknown game type: " + GAME_TYPE);
		}
	}
	
	
	private Statics() {
	}
	
	
}
