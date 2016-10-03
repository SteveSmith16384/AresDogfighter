package com.scs.aresdogfighter.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ssmith.lang.NumberFunctions;
import ssmith.util.GameLoopInterval;

import com.jme3.app.state.ScreenshotAppState;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingSphere;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.scs.aresdogfighter.AresDogfighter;
import com.scs.aresdogfighter.AresDogfighter.Module;
import com.scs.aresdogfighter.CollisionLogic;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.ai.EnemyShipAI;
import com.scs.aresdogfighter.ai.FriendlyShipAI;
import com.scs.aresdogfighter.avatars.AbstractAvatar;
import com.scs.aresdogfighter.avatars.PlayersShip;
import com.scs.aresdogfighter.controllers.ScenicViewController;
import com.scs.aresdogfighter.data.Faction;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.data.SpaceEntity.Type;
import com.scs.aresdogfighter.events.AbstractEvent;
import com.scs.aresdogfighter.gameobjects.Asteroid;
import com.scs.aresdogfighter.gameobjects.Cargo;
import com.scs.aresdogfighter.gameobjects.FriendlyPlanet;
import com.scs.aresdogfighter.gameobjects.HyperspaceEffect;
import com.scs.aresdogfighter.gameobjects.LargeExplosion;
import com.scs.aresdogfighter.gameobjects.Moon;
import com.scs.aresdogfighter.gameobjects.RotatingPlanet;
import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.gameobjects.SmallExplosion;
import com.scs.aresdogfighter.gameobjects.SpaceDebris;
import com.scs.aresdogfighter.gameobjects.SpaceStation;
import com.scs.aresdogfighter.gameobjects.Sun;
import com.scs.aresdogfighter.hud.HUD;
import com.scs.aresdogfighter.hud.HUD3D;
import com.scs.aresdogfighter.model.MySkybox;
import com.scs.aresdogfighter.model.StarDust;

public class ManualShipControlModule extends AbstractModule implements AnalogListener, ActionListener {//, PhysicsCollisionListener {

	public static final float KEYB_SENS = 1f;
	public static float SMALL_CIRCLE_RAD;
	public static float BIG_CIRCLE_RAD;
	public static float CENTRE_X, CENTRE_Y;

	// Controls
	public static final String PITCH_UP_MOUSE = "PitchUpMouse";
	public static final String PITCH_DOWN_MOUSE = "PitchDownMouse";
	public static final String STRAFE_LEFT_MOUSE = "Strafe_LeftMouse";
	public static final String STRAFE_RIGHT_MOUSE = "Strafe_RightMouse";

	public static final String PITCH_UP_KB = "PitchUpKB";
	public static final String PITCH_DOWN_KB = "PitchDownKB";
	public static final String STRAFE_LEFT_KB = "Strafe_LeftKB";
	public static final String STRAFE_RIGHT_KB = "Strafe_RightKB";

	public static final String ROLL_LEFT = "RollLeft";
	public static final String ROLL_RIGHT = "RollRight";

	public static final String ACCELERATE = "Acc";
	public static final String DECELERATE = "Dec";

	public static final String SHOOT = "Shoot";
	public static final String NEXT_EQ = "Next_Eq";
	public static final String MISSILE = "Missile";

	private static final String RESTART = "Restart";
	private static final String TEST = "Test";
	private static final String FRONT_VIEW = "Front_View";
	private static final String REAR_VIEW = "Rear_View";
	private static final String PAUSE = "Pause";
	private static final String TOGGLE_RECORDING = "Record";
	private static final String SCREENSHOT = "Screenshot";
	private static final String MAIN_MENU = "MainMenu";
	private static final String HAIL = "Hail";

	protected ArrayList<IProcessable> objects = new ArrayList<IProcessable>();
	private ArrayList<IProcessable> new_objects = new ArrayList<IProcessable>();
	private ArrayList<IProcessable> to_be_removed = new ArrayList<IProcessable>();

	//public BulletAppState bulletAppState;
	public float max_scanner_range = Statics.MAX_SCANNER_RANGE;
	public Node tactical_overlay_node = new Node("Tactical Overlay");
	public HUD hud;
	public AbstractAvatar players_avatar;

	private GameLoopInterval remove_objs_int = new GameLoopInterval(10);
	private GameObject last_obj_in_sights = null;
	private float time_in_sights;
	private boolean show_front_view = true;
	public GameObject cam_view;
	public SpaceStation spacestation;
	private HyperspaceEffect hyperspace_fx;
	private MySkybox skybox;
	private HUD3D hud3d;
	private boolean change_view = false;
	private boolean game_over = false;
	private VideoRecorderAppState video_recorder;
	private int enemy_ships_left;
	private ScreenshotAppState screenShotState;
	private boolean fly_through_wormhole = false;
	private float time_until_event = 0;
	private FilterPostProcessor fpp;
	private AudioNode music_node, ambient_node;
	private AudioNode game_over_node, new_info_node, siren_node, scanner, come_to_halt, accelerate;
	private DirectionalLight dirlight;

	/*	public boolean landing_on_planet;
	private MyTerrain terrain;
	private Vector3f pre_land_pos;
	private Quaternion pre_land_rot;
	 */
	private ScenicViewController scenic;
	public int num_enemies, num_asteroids;

	public ManualShipControlModule(AresDogfighter _game, BitmapFont _guiFont_small, BitmapFont _guiFont_large) {
		super(_game, AresDogfighter.Module.MANUAL_SHIP_FLYING, _guiFont_small, _guiFont_large);

		SMALL_CIRCLE_RAD = game.getCamera().getHeight() * .3f;
		BIG_CIRCLE_RAD = game.getCamera().getHeight() * .4f;

		CENTRE_X = game.getCamera().getWidth()/2;
		CENTRE_Y = game.getCamera().getHeight()/2;
	}


	@Override
	public void firstSetup() {
		fpp = new FilterPostProcessor(assetManager);
		BloomFilter laser_bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
		//bloom2.setDownSamplingFactor(2f);
		laser_bloom.setBlurScale(5f);
		fpp.addFilter(laser_bloom);

		/*BloomFilter bloom = new BloomFilter();
		bloom.setBlurScale(2f);//5f);
		bloom.setBloomIntensity(1.5f);//2f);
		fpp.addFilter(bloom);*/

		FogFilter fog = new FogFilter(ColorRGBA.White, .2f, Statics.CAM_VIEW_DIST/2);
		fpp.addFilter(fog);

		screenShotState = new ScreenshotAppState();

		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);

		/*bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		//bulletAppState.setDebugEnabled(Statics.DEBUG);
		bulletAppState.setEnabled(true);
		bulletAppState.getPhysicsSpace().addCollisionListener(this);*/

		guiNode.attachChild(tactical_overlay_node);

		StarDust starDust = new StarDust("StarDust", 100, 200f, cam, assetManager);
		rootNode.attachChild(starDust);

		hud = new HUD(this, assetManager, cam.getWidth(), cam.getHeight(), guiFont_small, guiFont_large);
		guiNode.attachChild(hud);
		this.objects.add(hud);

		hud3d = new HUD3D(this, this.getAssetManager(), guiFont_large);
		rootNode.attachChild(hud3d);
		this.objects.add(hud3d);

		if (Statics.SCENIC_INTRO) {
			scenic = new ScenicViewController(cam);
			//this.addObject(scenic);
		}

		loadSpace3DObjects(true);

		this.players_avatar.getNode().setCullHint(CullHint.Always);

		log("Hello Captain Jameson");
		this.getPlayersShip().shipdata.setCurrSpeed(0);

		skybox = new MySkybox(this, this.getAssetManager());
		this.rootNode.attachChild(skybox);
		this.addObject(skybox);

		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);
		cam.update();

		// Load audio nodes
		if (!Statics.MUTE) {
			music_node = new AudioNode(assetManager, "Sound/music_2.wav", true);
			music_node.setLooping(true);
			music_node.setPositional(false);
			this.rootNode.attachChild(music_node);

			ambient_node = new AudioNode(assetManager, "Sound/background_ambience.ogg", true);
			ambient_node.setPositional(false);
			ambient_node.setLooping(true);
			this.rootNode.attachChild(ambient_node);
			ambient_node.play();

			game_over_node = new AudioNode(assetManager, "Sound/gameover_voice.wav", false);
			game_over_node.setPositional(false);
			this.rootNode.attachChild(game_over_node);

			new_info_node = new AudioNode(assetManager, "Sound/newinfo.wav", false);
			new_info_node.setPositional(false);
			this.rootNode.attachChild(new_info_node);

			siren_node = new AudioNode(assetManager, "Sound/alarm.ogg", false);
			siren_node.setPositional(false);
			this.rootNode.attachChild(siren_node);

			scanner = new AudioNode(assetManager, "Sound/scanner.wav", false);
			scanner.setPositional(false);
			this.rootNode.attachChild(scanner);

			come_to_halt = new AudioNode(assetManager, "Sound/qubodup-PowerDrain.ogg", false);
			come_to_halt.setPositional(false);
			this.rootNode.attachChild(come_to_halt);

			accelerate = new AudioNode(assetManager, "Sound/power_drain_backwards.ogg", false);
			accelerate.setPositional(false);
			this.rootNode.attachChild(accelerate);

			if (Statics.MUSIC_DURING_GAME) {
				music_node.play();
			}
		}

		hyperspace_fx = new HyperspaceEffect(this, assetManager, (PlayersShip)this.players_avatar);
		hyperspace_fx.addToGame(this.rootNode);

		enemy_ships_left = Statics.NUM_ENEMIES;
	}


	@Override
	public void reload() {
		setupLight();

		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);
		cam.update();

		viewPort.addProcessor(fpp);

		setupInputs();

		cam.setFrustumPerspective(Statics.FOV_ANGLE, cam.getWidth() / cam.getHeight(), .1f, 1000);

		this.stateManager.attach(screenShotState);

		//stateManager.attach(bulletAppState);

		if (Statics.DEBUG) {
			debug("Game Objects:" + this.objects.size());
			debug("ViewPort:" + this.viewPort.getProcessors().size());
			debug("stateManager: Unknown");// + this.stateManager..getProcessors().size());
		}

		this.viewPort.setBackgroundColor(ColorRGBA.Black);

		hud.updateAllText(this.getPlayersShip().shipdata);
	}


	private void setupLight() {
		// Remove existing lights
		this.rootNode.getWorldLightList().clear(); //this.rootNode.getWorldLightList().size();
		LightList list = this.rootNode.getWorldLightList();
		for (Light it : list) {
			this.rootNode.removeLight(it);
		}

		// We add light so we see the scene
		//if (this.landing_on_planet == false) {
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(2.5f));
		rootNode.addLight(al);
		al = null; // Avoid me accidentally re-using it
		//}

		dirlight = new DirectionalLight();
		dirlight.setColor(ColorRGBA.White.mult(2f));//.mult(2.5f));
		/*if (this.landing_on_planet == false) {
			dirlight.setDirection(new Vector3f(0, 0, 1f)); // Relative to the sun in the skybox 
		} else {
			dirlight.setDirection((new Vector3f(-0.5f, -1f, -0.5f)).normalize());
		}*/		
		rootNode.addLight(dirlight);

		PointLight pointlight = new PointLight();
		pointlight.setColor(ColorRGBA.White.mult(2f));//.mult(2.5f));
		//rootNode.addLight(pointlight);
		pointlight = null;
	}


	@Override
	public void unload() {
		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);
		cam.update();

		viewPort.removeProcessor(fpp);

		this.stateManager.detach(screenShotState);
		//this.stateManager.detach(bulletAppState);

	}


	protected void setupInputs() {
		this.inputManager.clearMappings();
		
		//this.flyCam.registerWithInput(inputManager);

		if (!Statics.INVERSE_PITCH) {
			inputManager.addMapping(PITCH_UP_KB, new KeyTrigger(KeyInput.KEY_UP));
			inputManager.addMapping(PITCH_UP_MOUSE, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
			inputManager.addMapping(PITCH_DOWN_KB, new KeyTrigger(KeyInput.KEY_DOWN));
			inputManager.addMapping(PITCH_DOWN_MOUSE, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		} else {
			inputManager.addMapping(PITCH_DOWN_KB, new KeyTrigger(KeyInput.KEY_UP));
			inputManager.addMapping(PITCH_DOWN_MOUSE, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
			inputManager.addMapping(PITCH_UP_KB, new KeyTrigger(KeyInput.KEY_DOWN));
			inputManager.addMapping(PITCH_UP_MOUSE, new MouseAxisTrigger(MouseInput.AXIS_Y, true));

		}
		inputManager.addMapping(STRAFE_LEFT_KB, new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping(STRAFE_RIGHT_KB, new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping(STRAFE_LEFT_MOUSE, new MouseAxisTrigger(MouseInput.AXIS_X, true));
		inputManager.addMapping(STRAFE_RIGHT_MOUSE, new MouseAxisTrigger(MouseInput.AXIS_X, false));

		inputManager.addMapping(ACCELERATE, new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping(DECELERATE, new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping(ROLL_LEFT, new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping(ROLL_RIGHT, new KeyTrigger(KeyInput.KEY_D));

		inputManager.addMapping(SHOOT, new KeyTrigger(KeyInput.KEY_SPACE), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(NEXT_EQ, new KeyTrigger(KeyInput.KEY_M));
		inputManager.addMapping(MISSILE, new KeyTrigger(KeyInput.KEY_M), new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

		inputManager.addMapping(FRONT_VIEW, new KeyTrigger(KeyInput.KEY_F));
		inputManager.addMapping(REAR_VIEW, new KeyTrigger(KeyInput.KEY_R));

		inputManager.addMapping("1", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping("2", new KeyTrigger(KeyInput.KEY_2));
		inputManager.addMapping("3", new KeyTrigger(KeyInput.KEY_3));
		inputManager.addMapping("4", new KeyTrigger(KeyInput.KEY_4));
		inputManager.addMapping(PAUSE, new KeyTrigger(KeyInput.KEY_5));
		inputManager.addMapping(RESTART, new KeyTrigger(KeyInput.KEY_6));
		inputManager.addMapping(TOGGLE_RECORDING, new KeyTrigger(KeyInput.KEY_7));
		inputManager.addMapping(SCREENSHOT, new KeyTrigger(KeyInput.KEY_8));
		inputManager.addMapping(MAIN_MENU, new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addMapping(HAIL, new KeyTrigger(KeyInput.KEY_H));

		inputManager.addMapping(TEST, new KeyTrigger(KeyInput.KEY_T));

		inputManager.addListener(this, PITCH_UP_KB, PITCH_UP_MOUSE, PITCH_DOWN_KB, PITCH_DOWN_MOUSE, 
				ROLL_LEFT, ROLL_RIGHT, STRAFE_LEFT_KB, STRAFE_LEFT_MOUSE, STRAFE_RIGHT_KB, STRAFE_RIGHT_MOUSE, 
				ACCELERATE, DECELERATE, "1", "2", "3", "4", PAUSE, TOGGLE_RECORDING, SHOOT, NEXT_EQ, MISSILE, TEST, FRONT_VIEW, REAR_VIEW, RESTART,
				MAIN_MENU, SCREENSHOT, HAIL);

	}


	@Override
	public void simpleUpdate(float tpf) {
		objects.removeAll(to_be_removed);
		to_be_removed.clear();
		objects.addAll(this.new_objects);
		this.new_objects.clear();

		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);

		boolean load_remove_objects = remove_objs_int.hasHit(tpf);

		if (cam_view == null) {
			this.viewFrom((GameObject)this.players_avatar);
		}
		if (this.scenic == null) {
			this.cam.setLocation(this.cam_view.getNode().getWorldTranslation());
			this.cam.setRotation(this.cam_view.getNode().getLocalRotation());
			if (!this.show_front_view) {
				cam.lookAt(cam.getLocation().add(cam.getDirection().mult(-1f)), cam.getUp());
				//this.cam.setRotation(this.cam_view.getSpatial().getLocalRotation().opposite());
			}
		} else {
			scenic.process(tpf);
		}
		this.cam.updateViewProjection();// cam.getRotation();

		this.dirlight.setDirection(cam.getDirection());

		hud.showTargetter(false); // Default

		/*if (this.players_ship.order.target != null) {
			if (this.players_ship.order.target.isAlive() == false) {
				this.players_ship.order.target = null;
				hud.showTargetter(false);
			}
		}*/

		num_enemies = 0;
		num_asteroids = 0;

		// Process objects
		synchronized (objects) {
			Iterator<IProcessable> it = objects.iterator();
			while (it.hasNext()) {
				IProcessable o = it.next();

				if (o instanceof GameObject) {
					GameObject go = (GameObject)o;
					go.dist_from_player = go.distance((GameObject)this.players_avatar);
					go.dist_from_cam = go.distance(this.cam.getLocation());
				}

				o.process(tpf);

				if (o instanceof GameObject) {
					GameObject go = (GameObject)o;

					if (go.collided_with != null) {
						Vector3f point = go.getNode().getWorldTranslation(); // too - get proper point
						CollisionLogic.HandleCollision(this, go, go.collided_with, point);
						go.collided_with.collided_with = null; // Clear the other object
						go.collided_with = null;
					}

					Vector3f screen_pos = cam.getScreenCoordinates(go.getNode().getWorldTranslation());
					FrustumIntersect insideoutside = cam.contains(go.getNode().getWorldBound());
					if (go != this.players_avatar) {
						// Within sights?
						if (change_view) {
							if (go instanceof Ship1 && go != this.players_avatar) {
								this.viewFrom((Ship1)go);
							}
						}
						/*if (insideoutside == Camera.FrustumIntersect.Inside) {
							// Targetting
							if (go != this.players_ship.order.target) {
								if (go.can_be_targetted) {
									if (screen_pos.x > cam.getWidth()*.4 && screen_pos.x < cam.getWidth()*.6) { // todo - cache values
										if (screen_pos.y > cam.getHeight()*.4 && screen_pos.y < cam.getHeight()*.6) {
											if (go == last_obj_in_sights || last_obj_in_sights == null) {
												//log("Targetting " + go.name + "...");
												if (scanner != null) {
													scanner.play();
												}
												this.time_in_sights += tpf;
												if (time_in_sights > TIME_TO_TARGET) {
													this.players_ship.order.target = go;
													time_in_sights = 0;
													//log(go.name + " targetted");
												}
											}
										}							
									}
								} else {
									if (last_obj_in_sights == go) {
										last_obj_in_sights = null;
										time_in_sights = 0;
									}
								}
							} else {
								time_in_sights = 0; // Stop us targetting another object
								//hud.target_details.setText("Target: " + go.name +  "  D: " + String.format("%d", (int)go.distance(this.cam.getLocation())));
								hud.showTargetter(true);
								hud.setTargetterPosition(screen_pos.x, screen_pos.y);
							}
						} else {
							if (go == this.players_ship.order.target) {
								this.players_ship.order.target = null;
							}
						}*/

						if (go.show_in_hud) {
							go.processHUD(this.tactical_overlay_node, screen_pos, insideoutside);
						}

						// Warn if close
						if (go.ai_avoid) {
							if (go.dist_from_player < Statics.WARNING_DISTANCE) {
								if (!Statics.MUTE) {
									siren_node.play();
								}
							}
						}

						if (load_remove_objects) {
							/*if (this.landing_on_planet && go.show_mode == ShowMode.SPACE) {
								go.removeFromGame(false, true);
							} else if (this.landing_on_planet == false && go.show_mode == ShowMode.PLANET) {
								go.removeFromGame(false, true);
							} else {*/
							if (go != this.players_avatar) {
								//if (this.landing_on_planet == false || go.show_mode != ShowMode.PLANET) { // Don't unload planet objects
								if (go.distance(this.cam.getLocation()) > Statics.OBJ_CREATION_DIST) {
									go.removeFromGame(false, true);
									//log(go.getName() + " unloaded");
								}
								//}
							}
							//}
						}
					}
				}
			}

			/*if (will_refresh_hud) {
				//this.hud.refreshObjectList(objects);
				will_refresh_hud = false;
			}*/

			// load close objects
			if (load_remove_objects) {
				this.loadSpace3DObjects(false);
			}
		}

		/*if (fly_through_wormhole) {
			if (this.game.game_data.current_mission != null) {
				hud.startWormholeBox();

				// remove all current space objects
				Iterator<IProcessable> it = objects.iterator();
				while (it.hasNext()) {
					IProcessable o = it.next();
					if (o instanceof GameObject) {
						GameObject go = (GameObject)o;
						go.removeFromGame(false, true);
					}
				}

				this.game.game_data.current_sector = this.game.game_data.current_mission.getSectorData();
				this.loadSpace3DObjects();

			}
			fly_through_wormhole = false;
		}*/

		if (scenic == null) { // Only do events when not viewing scenic stuff
			this.time_until_event -= tpf;
			if (time_until_event < 0 && NumberFunctions.rnd(1, 10) == 1) {
				time_until_event = Statics.EVENT_INTERVAL;
				AbstractEvent current_event = AbstractEvent.GetRandomEvent(this);
				debug("Activating event " + current_event.getName());
				current_event.setupEvent();
			}
		} else {
			if (scenic.objects.isEmpty()) {
				scenic = null;
				this.log("Finished scenic controller");
			}
		}
	}


	private void loadSpace3DObjects(boolean start) {
		/*if (this.landing_on_planet) {
			return;
		}*/

		inputManager.setCursorVisible(!Statics.HIDE_MOUSE);

		Vector3f pos = new Vector3f();
		Iterator<SpaceEntity> it = game.game_data.current_sector.entities.iterator();
		while (it.hasNext()) {
			SpaceEntity o = it.next();
			if (this.players_avatar != null) {
				pos = this.players_avatar.getNode().getWorldTranslation();
			}
			float dist = pos.distance(o.pos);
			if (dist < Statics.OBJ_CREATION_DIST || o.type == SpaceEntity.Type.PLAYERS_SHIP) {
				switch (o.type) {
				case DESERT_PLANET:
					RotatingPlanet planet2 = new RotatingPlanet(this, "Ranarama", NumberFunctions.rndFloat(40, 50), this.getAssetManager(), guiFont_small, o.pos);
					planet2.addToGame(this.rootNode);
					//planet2.getSpatial().setLocalTranslation(o.pos);
					it.remove();
					if (start && scenic != null) {
						this.scenic.addObject(planet2);
					}
					break;

				case FRIENDLY_PLANET:
					FriendlyPlanet fplanet = new FriendlyPlanet(this, "Ranarama", this.getAssetManager(), guiFont_small, o.pos);
					fplanet.addToGame(this.rootNode);
					//fplanet.getSpatial().setLocalTranslation(o.pos);
					it.remove();
					if (start && scenic != null) {
						this.scenic.addObject(fplanet);
					}
					break;

				case SUN:
					Sun sun = new Sun(this, this.getAssetManager(), guiFont_small);
					sun.addToGame(this.rootNode);
					sun.getNode().setLocalTranslation(o.pos);
					it.remove();
					if (start && scenic != null) {
						this.scenic.addObject(sun);
					}
					break;

				case MOON:
					Moon moon = new Moon(this, this.getAssetManager(), guiFont_small, o.pos);
					moon.addToGame(this.rootNode);
					moon.getNode().setLocalTranslation(o.pos);
					it.remove();
					if (start && scenic != null) {
						this.scenic.addObject(moon);
					}
					break;

				case PLAYERS_SHIP:
					PlayersShip pship = (PlayersShip)Ship1.Factory(this, assetManager, guiFont_small, o.type);
					pship.addToGame(this.rootNode);
					pship.getNode().setLocalTranslation(o.pos);
					players_avatar = pship;
					it.remove();
					pos = pship.getNode().getWorldTranslation();
					if (start && scenic != null) {
						this.scenic.addObject(pship);
					}
					break;

				case ENEMY_FIGHTER:
					Ship1 ship = Ship1.Factory(this, assetManager, guiFont_small, o.type);
					ship.addToGame(this.rootNode);
					ship.getNode().setLocalTranslation(o.pos);

					ship.ai = new EnemyShipAI(this, ship);
					it.remove();
					if (start && scenic != null) {
						this.scenic.addObject(ship);
					}
					break;

				case FRIENDLY_FIGHTER:
					ship = Ship1.Factory(this, assetManager, guiFont_small, o.type);
					ship.addToGame(this.rootNode);
					ship.getNode().setLocalTranslation(o.pos);

					ship.ai = new FriendlyShipAI(this, ship);
					it.remove();
					if (start && scenic != null) {
						this.scenic.addObject(ship);
					}
					break;

				case POLICE_SHIP:
					/*ship = Ship1.Factory(this, assetManager, guiFont_small, o.type);
					ship.addToGame(this.rootNode);
					ship.getSpatial().setLocalTranslation(o.pos);

					//ship.ai = new PoliceShipAI(this, ship);
					it.remove();*/
					break;

				case CIVILIAN:
					/*ship = Ship1.Factory(this, assetManager, guiFont_small, o.type);
					ship.addToGame(this.rootNode);
					ship.getSpatial().setLocalTranslation(o.pos);

					//ship.ai = new NonFactionShipAI(this, ship);
					it.remove();*/
					break;

				case SPACESTATION:
					spacestation = new SpaceStation(this, "Space Station", this.getAssetManager(), guiFont_small);
					spacestation.addToGame(this.rootNode);
					spacestation.getNode().setLocalTranslation(o.pos);
					it.remove();
					if (start && scenic != null) {
						this.scenic.addObject(spacestation);
					}
					break;

				case ASTEROID:
					Asteroid asteroid = new Asteroid(this, this.getAssetManager(), guiFont_small);
					asteroid.addToGame(this.rootNode);
					asteroid.getNode().setLocalTranslation(o.pos);
					it.remove();
					if (start && scenic != null) {
						this.scenic.addObject(asteroid);
					}
					break;

				case DEBRIS:
					SpaceDebris debris = new SpaceDebris(this, this.getAssetManager(), guiFont_small);
					debris.addToGame(this.rootNode);
					debris.getNode().setLocalTranslation(o.pos);
					it.remove();
					if (start && scenic != null) {
						this.scenic.addObject(debris);
					}
					break;

				case CARGO:
					Cargo cargo = new Cargo(this, this.getAssetManager(), guiFont_small);
					cargo.addToGame(this.rootNode);
					cargo.getNode().setLocalTranslation(o.pos);
					it.remove();
					if (start && scenic != null) {
						this.scenic.addObject(cargo);
					}
					break;

				default:
					throw new RuntimeException("Unknown type: " + o.type);
				}
			}
		}
	}
	/*public void autoShoot(Ship1 shooter, Ship1 target, CaptainsOrder.Section section) {
		LaserBlast laser = new LaserBlast(this, assetManager, shooter, target, Statics.SMALL_EXPLOSION_DURATION);
		laser.addToGame(this.rootNode);
		//this.showSmallExplosion(target.getSpatial().getWorldTranslation(), duration);
		float damage = shooter.getWeaponDamage();
		float protec = target.getShieldLevel();

		boolean success = NumberFunctions.rndFloat(0, 1) < shooter.getWeaponAccuracy();
		if (!success) {
			section = CaptainsOrder.GetRandomSection();
		}

		damage = damage * (1-protec);
		target.damage(shooter, section, damage);

		target.showForceField(assetManager);

		// Who saw it?
		List<Ship1> list = this.getShips(shooter.getSpatial().getWorldTranslation());//, Statics.SEE_SHOTS_RANGE);
		Iterator<Ship1> it = list.iterator();
		while (it.hasNext()) {
			Ship1 ship = it.next();
			if (ship.ai != null) { // Not us
				ship.ai.seenShooting2(shooter, target, damage);
			}
		}

		this.security.shotFired(shooter);
	}
	 */

	/*	public void manualShoot(Ship1 shooter, Vector3f optional_target) {
		if (shooter.checkIfCanShoot()) {
			//log(shooter + " has fired at " + shooter.order.target);
			// Choose bullet type depending on weapon
			AbstractBullet laser = AbstractBullet.Factory(this, this.getAssetManager(), shooter, optional_target);
			//MovingLaserBlast laser = new MovingLaserBlast(this, assetManager, shooter, shooter.getWeaponRange(), shooter.getWeaponDamage(), optional_target);
			//PulseLaserShot laser = new PulseLaserShot(this, assetManager, shooter, shooter.getWeaponRange(), shooter.getWeaponDamage());
			laser.addToGame(this.rootNode);

			this.security.shotFired(shooter);
		} else {
			if (shooter  == this.players_ship) {
				log("Weapon temp=" + this.players_ship.shipdata.weapon.getCurrentTemp());
			}
		}
	}*/


	public List<Ship1> getShips(Vector3f pos) {
		List<Ship1> list = new ArrayList<Ship1>();
		synchronized (this.objects) {
			Iterator<IProcessable> it = objects.iterator();
			while (it.hasNext()) {
				IProcessable o = it.next();
				if (o instanceof Ship1) {
					Ship1 go = (Ship1)o;
					list.add(go);
				}
			}

		}
		return list;
	}


	public List<IProcessable> getGameObjects() {
		List<IProcessable> list = new ArrayList<IProcessable>();
		synchronized (this.objects) {
			Iterator<IProcessable> it = objects.iterator();
			while (it.hasNext()) {
				IProcessable o = it.next();
				if (o instanceof GameObject) {
					GameObject go = (GameObject)o;
					list.add(go);
				}
			}

		}
		return list;
	}


	@Override
	public void log(String s) {
		if (!Statics.MUTE) {
			if (new_info_node != null) {
				new_info_node.play();
			}
		}
		System.out.println(s);
		hud.log(s);
	}


	public int getNumPolice() {
		int num_police = 0;
		Iterator<IProcessable> it = objects.iterator();
		while (it.hasNext()) {
			IProcessable o = it.next();
			if (o instanceof GameObject) {
				GameObject go = (GameObject)o;
				if (go instanceof Ship1) {
					Ship1 ship = (Ship1)go;
					if (ship.faction == Faction.POLICE) {
						num_police++;
					}
				}
			}
		}
		return num_police;
	}


	/*public void launchNPCShip_(SpaceEntity.Type type) {
		Vector3f pos = this.players_avatar.getSpatial().getWorldTranslation();
		float x = NumberFunctions.rndFloat(pos.x - 50, pos.x+50);
		float y = NumberFunctions.rndFloat(pos.y - 50, pos.y+50);
		float z = NumberFunctions.rndFloat(pos.z - 50, pos.z+50);
		game.game_data.current_sector.entities.add(new SpaceEntity(type, x, y, z));
		log(type + " has arrived");
	}
	 */

	public void shipDestroyed(Ship1 target, Ship1 shooter) {
		showBigExplosion(target.getNode().getWorldTranslation(), 2);
		log(target.getName() + " destroyed!");
		if (target == this.players_avatar) {
			gameOver("YOU HAVE BEEN DESTROYED by " + shooter);
		} else {
			if (Statics.GAME_TYPE == Statics.GT_DOGFIGHT) {
				enemy_ships_left--;
				if (enemy_ships_left > 0) {
					log(enemy_ships_left + " enemy ships left");
				} else {
					game_over = true;
					this.hud.showRestart(true);
					log("VICTORY!");
					if (Statics.NUM_ENEMIES == Statics.MAX_ENEMIES) {
						Statics.MAX_ENEMIES++;
						Statics.NUM_ENEMIES = Statics.MAX_ENEMIES;
						Statics.SaveProperties();
						log("You can now battle up to " + Statics.MAX_ENEMIES + " enemy ships!");
					}
					log("Press Escape to continue");
				}
			}
		}

		//Cargo cargo = new Cargo(this, this.assetManager, this.guiFont_small);
		//cargo.addToGame(this.rootNode);
		this.addEntity(Type.CARGO, target.getNode().getWorldBound().getCenter());
	}


	public void gameOver(String reason) {
		game_over = true;
		this.hud3d.removeFromParent();
		this.hud.showRestart(false);
		hyperspace_fx.stop();
		log(reason);//"YOU HAVE BEEN DESTROYED!");
		log("Press Escape to continue");
		if (!Statics.MUTE) {
			if (game_over_node != null) {
				game_over_node.play();
			}
		}

	}
	
	
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		//log("Key " + name + " " + (isPressed?"pressed":"released"));
		if (isPressed) {
			if (name.equalsIgnoreCase(FRONT_VIEW)) { // Show front view
				this.hud.view_name.setText("Front View");
				this.show_front_view = true;
				this.hud.crosshairs.setCullHint(CullHint.Inherit);
				this.hud3d.showFrame(true);
			} else if (name.equalsIgnoreCase(REAR_VIEW)) { // Rear view
				this.hud.view_name.setText("Rear View");
				this.show_front_view = false;
				this.hud.crosshairs.setCullHint(CullHint.Always);
				this.hud3d.showFrame(false);
			} else if (name.equalsIgnoreCase("3")) { // Our ship
				this.viewFrom((GameObject)this.players_avatar);
				change_view = false;
			} else if (name.equalsIgnoreCase("4")) { // enemy ship view
				change_view = true;
				/*if (players_ship.order.target != null && players_ship.order.target instanceof Ship1) {
					cam_view = (Ship1)players_ship.order.target;
					log("Viewing from " + cam_view);
				} else {
					log("No target to view from");
				}*/
			} else if (name.equalsIgnoreCase(PAUSE)) {
				// TODO - UNPAUse - game.togglePause();
			} else if (name.equalsIgnoreCase(NEXT_EQ)) { // todo  -sfx here
				// todo - this.players_ship.shipdata.selectNextEquipment();
				// todo - log(this.players_ship.shipdata.getSelectedEquipment().getName() + " selected");
			} else if (name.equalsIgnoreCase(RESTART)) {// && game_over) {
				game.selectModule(AresDogfighter.Module.MANUAL_SHIP_FLYING, false);
			} else if (name.equalsIgnoreCase(TOGGLE_RECORDING)) {
				if (video_recorder == null) {
					log("RECORDING VIDEO");
					video_recorder = new VideoRecorderAppState();
					stateManager.attach(video_recorder);
					if (Statics.MUTE) {
						log("Warning: sounds are muted");
					}
				} else {
					log("STOPPED RECORDING");
					stateManager.detach(video_recorder);
					video_recorder = null;
				}
			} else if (name.equalsIgnoreCase(SCREENSHOT)) {
				screenShotState.takeScreenshot();
				log("TAKEN SCREENSHOT");
			} else if (name.equalsIgnoreCase(ACCELERATE)) {
				if (Statics.MUTE == false) {
					this.accelerate.play();
				}
			} else if (name.equalsIgnoreCase(DECELERATE)) {
				if (Statics.MUTE == false) {
					this.come_to_halt.play();
				}
				/*} else if (name.equalsIgnoreCase(HAIL)) {
				if (this.players_ship.order.target != null) {
					log("Hailing " + this.players_ship.order.target.name + "...");
					this.players_ship.order.target.hail();
				} else {
					log("No target to hail");
				}*/
			} else if (name.equalsIgnoreCase(TEST)) {
			}
		} else {
			if (name.equalsIgnoreCase(MAIN_MENU)) { // Must be onreleased!
				this.game.selectModule(Module.START_GAME, false);
			}
		}
		this.players_avatar.onAction(name, isPressed, tpf);
	}


	@Override
	public void onAnalog(String name, float value, float tpf) {
		this.players_avatar.onAnalog(name, value, tpf);

	}


	public void setHyperspaceValues() {
		PlayersShip ship = (PlayersShip)players_avatar;
		this.hyperspace_fx.model.setSparkVelocity(ship.shipdata.getCurrentSpeed());
	}


	private void viewFrom(GameObject go) {
		if (this.cam_view == go) {
			return;
		}
		if (this.cam_view != null) {
			Ship1 orig = (Ship1)cam_view;
			orig.getNode().setCullHint(CullHint.Inherit);
		}
		this.cam_view = go;
		log("Viewing from " + cam_view.name);
		change_view = false;
		go.getNode().setCullHint(CullHint.Always);
	}

	/*
	public void landOnPlanet() {
		if (Statics.LAND_ON_PLANET) {
			log("Entering atmosphere");

			landing_on_planet = true;
			setupLight();
			this.hud.startAtmosBox();
			this.viewPort.setBackgroundColor(Statics.SKY_COL);
			this.remove_objs_int.fireNow();

			// Load terrain
			terrain = new MyTerrain(this, this.getAssetManager(), guiFont_small);
			terrain.addToGame(this.rootNode);

			// Position player
			pre_land_pos = this.players_avatar.getNode().getLocalTranslation().clone();
			pre_land_rot = this.players_avatar.getNode().getLocalRotation().clone();
			this.players_avatar.getNode().setLocalTranslation(0, Statics.RETURN_TO_SPACE_DIST * .8f, 0);
			this.players_avatar.getNode().lookAt(terrain.getNode().getWorldTranslation(), Vector3f.UNIT_Y);
		}
	}


	public void returnToSpace() {
		log("Entering atmosphere");
		landing_on_planet = false;
		setupLight();
		this.hud.startAtmosBox();
		this.viewPort.setBackgroundColor(ColorRGBA.Black);
		this.remove_objs_int.fireNow();

		// Position and rotate player
		this.players_avatar.getNode().setLocalTranslation(pre_land_pos);
		this.players_avatar.getNode().setLocalRotation(this.pre_land_rot.inverse());

		try {
			terrain.removeFromGame(false, false);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}
	 */

	public SpaceStation getSpaceStation() {
		return this.spacestation;
	}


	public void flyThroughWormhole() {
		this.fly_through_wormhole = true;
	}


	/*	public void endEvent() {
		debug("Ending current event " + game.game_data.current_event.getName());
		current_event = null;
	}
	 */

	@Override
	public void muteChanged() {
		if (Statics.MUTE) {
			this.music_node.stop();
			ambient_node.stop();
		}
	}


	public void addObject(IProcessable o) {
		this.new_objects.add(o);
		if (o instanceof GameObject) {
			GameObject go = (GameObject)o;
		}
	}


	public void removeObject(IProcessable o) {
		this.to_be_removed.add(o);
		if (o instanceof GameObject) {
			GameObject go = (GameObject)o;
		}
	}


	public Camera getCam() {
		return this.game.getCamera();
	}


	public void debug(String s) {
		if (Statics.DEBUG || Statics.DEBUG_AI) {
			log("DEBUG: " + s);
		}
	}


	public RenderManager getRenderManager() {
		return game.getRenderManager();
	}


	/*@Override
	public void collision(PhysicsCollisionEvent arg0) { // Called after physicsTick(), gets called loads of times
		debug("Collision:" + arg0.getObjectA().getUserObject() + " v " + arg0.getObjectB().getUserObject());
		//been_collision = true;
		Node no1 = (Node)arg0.getObjectA().getUserObject();
		Node no2 = (Node)arg0.getObjectB().getUserObject();
		GameObject o1 = (GameObject)no1.getUserData("gameobject");
		GameObject o2 = (GameObject)no2.getUserData("gameobject");
		o1.collided_with = o2; // If you get NPE here, it means there's no userdata for the model
		o2.collided_with = o1;
	}
	 */

	/*
	 * This is no longer used since we use Physics.  YES IT IS!
	 */
	public boolean checkForCollisions(GameObject us) { // todo - move to gameobject
		boolean result = false;
		synchronized (this.objects) {
			CollisionResults results = new CollisionResults();
			Iterator<IProcessable> it = objects.iterator();
			while (it.hasNext()) {
				IProcessable o = it.next();
				if (o instanceof GameObject && o != us) {
					GameObject go = (GameObject)o;
					if (go.collides) {
						if (go.getNode().getWorldBound() != null) {
							results.clear();
							try {
								/*BoundingSphere us_sphere = (BoundingSphere)us.getNode().getWorldBound();
								float us_rad = us_sphere.getRadius();
								BoundingSphere go_sphere = (BoundingSphere)go.getNode().getWorldBound();
								float go_rad = go_sphere.getRadius();
								boolean res = us_rad+go_rad >= us_sphere.distanceTo(go_sphere.getCenter());*/
								us.getNode().collideWith(go.getNode().getWorldBound(), results);
								if (results.size() > 0) {//res) {
									Vector3f pos = go.getNode().getWorldTranslation(); // todo - get point between them
									result = CollisionLogic.HandleCollision(this, us, go, pos) || result;
								}
							} catch (UnsupportedCollisionException | java.lang.ClassCastException ex) {
								System.out.println("Spatial: " + go.getNode());
								ex.printStackTrace();
							}
						} else {
							if (Statics.DEBUG) {
								log("Warning: " + go + " doesn't have a model bound");
							}
						}
					}
				}
			}
		}
		return result;
	}


	/*public GameObject checkForFutureCollisions(GameObject us, float max_dist) {
		if (max_dist > 0) {
			Ray r = new Ray(us.getNode().getWorldTranslation(), us.getNode().getLocalRotation().mult(Vector3f.UNIT_Z).normalizeLocal());
			synchronized (this.objects) {
				CollisionResults results = new CollisionResults();
				Iterator<IProcessable> it = objects.iterator();
				while (it.hasNext()) {
					IProcessable o = it.next();
					if (o instanceof GameObject && o != us) {
						GameObject go = (GameObject)o;
						if (go.collides && go.ai_avoid) {
							if (go.getNode().getWorldBound() != null) {
								results.clear();
								try {
									r.collideWith(go.getNode().getWorldBound(), results);
									//go.getSpatial().collideWith(r, results);
								} catch (UnsupportedCollisionException ex) {
									System.out.println("Spatial: " + go.getNode());
									ex.printStackTrace();
								}
								if (results.size() > 0) {
									float dist = us.distance(go);
									if (dist <= max_dist) {
										return go;
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}*/


	public void showSmallExplosion(Vector3f pos, float dur) {
		SmallExplosion expl = new SmallExplosion(this, assetManager, dur);
		expl.getNode().setLocalTranslation(pos);
		expl.addToGame(this.rootNode);
	}


	public void showBigExplosion(Vector3f pos, float dur) {
		LargeExplosion expl = new LargeExplosion(this, assetManager, dur);
		expl.getNode().setLocalTranslation(pos);
		expl.addToGame(this.rootNode);
	}

	
	public void addEntity(Type _type, Vector3f v) {
		addEntity(_type, v.x, v.y, v.z);
	}
	
	
	public void addEntity(Type _type, float x, float y, float z) {
		game.game_data.current_sector.entities.add(new SpaceEntity(_type, x, y, z));
		remove_objs_int.fireNow();
	}
	
	
	private PlayersShip getPlayersShip() {
		PlayersShip ps = (PlayersShip)this.players_avatar;
		return ps;
	}
}
