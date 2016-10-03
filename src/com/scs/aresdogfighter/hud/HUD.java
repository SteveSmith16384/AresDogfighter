package com.scs.aresdogfighter.hud;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.ui.Picture;
import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.data.ShipData;
import com.scs.aresdogfighter.equipment.AbstractEquipment;
import com.scs.aresdogfighter.gameobjects.SparkDamageEffect;
import com.scs.aresdogfighter.gui.TextArea;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

/*
 * Positioning text = the co-ords of BitmapText are for the top-left of the first line of text, and they go down from there.
 * 
 */
public class HUD extends Node implements IProcessable {

	public TextArea log_ta, options;
	public TextArea view_name;
	private TextArea speed, shields, hull, temp, equipment, cargo, spacestation_health; // temp,shields updated regularly
	private int screen_width, screen_height;
	protected ManualShipControlModule module;
	private Picture targetting_reticule;
	public Picture crosshairs;
	private int crosshairs_w, crosshairs_h;
	private HealthBar health_bar;

	private Geometry damage_box;
	private ColorRGBA dam_box_col = new ColorRGBA(1, 0, 0, 0.0f);
	private boolean process_damage_box;

	public HUD(ManualShipControlModule _module, AssetManager assetManager, int w, int h, BitmapFont font_small, BitmapFont font_large) {
		super("HUD");

		module = _module;
		screen_width = w;
		screen_height = h;
		//ship_data = _ship_data;

		log_ta = new TextArea("log", font_large, 6, "", true);
		log_ta.setLocalTranslation(0, screen_height, 0);
		this.attachChild(log_ta);

		/*options = new TextArea("options", font_large, 6, "", false);
		options.setLocalTranslation(0, screen_height - (log.getLineHeight()*7), 0);
		this.attachChild(options);*/

		/*obj_list_width = screen_width/5;
		object_list = new ObjectList(module, font_large);
		object_list.setLocalTranslation(screen_width-obj_list_width, screen_height, 0);
		//this.attachChild(object_list);*/

		/*ship_stats_width = screen_width/10;
		ship_stats_height = screen_height/4;
		our_ship_stats = new ShipStatsHUD(this, ship_stats_width, ship_stats_height, assetManager, font_small, module.players_ship.shipdata, true);
		our_ship_stats.setLocalTranslation(0, 0, 0);
		//this.attachChild(our_ship_stats);*/

		/*enemy_ship_stats = new ShipStatsHUD(this, ship_stats_width, ship_stats_height, assetManager, font_small, null, false);
		enemy_ship_stats.setLocalTranslation(w-ship_stats_width, 0, 0);
		enemy_ship_stats.setCullHint(CullHint.Always); // DOn't show for now
		this.attachChild(enemy_ship_stats);*/

		speed = new TextArea("speed", font_large, -1, "Speed: 0", false);
		speed.setLocalTranslation(0, speed.getLineHeight()*7, 0);
		this.attachChild(speed);

		shields = new TextArea("shields", font_large, -1, "shields: 0", false);
		shields.setLocalTranslation(0, speed.getLineHeight()*6, 0);
		this.attachChild(shields);

		hull = new TextArea("hull", font_large, -1, "Hull: 0", false);
		hull.setLocalTranslation(0, speed.getLineHeight()*5, 0);
		this.attachChild(hull);

		temp = new TextArea("temp", font_large, -1, "temp: 0", false);
		temp.setLocalTranslation(0, speed.getLineHeight()*4, 0);
		this.attachChild(temp);

		equipment = new TextArea("equipment", font_large, -1, "equipment: 0", false);
		equipment.setLocalTranslation(0, speed.getLineHeight()*3, 0);
		this.attachChild(equipment);

		cargo = new TextArea("cargo", font_large, -1, "cargo: 0", false);
		cargo.setLocalTranslation(0, speed.getLineHeight()*2, 0);
		this.attachChild(cargo);

		spacestation_health = new TextArea("spacestation_health", font_large, -1, "spacestation_health: 0", false);
		spacestation_health.setLocalTranslation(0, speed.getLineHeight()*1, 0);
		this.attachChild(spacestation_health);

		view_name = new TextArea("view_name", font_large, -1, "Front View", false);
		view_name.setLocalTranslation(w/2, h * .9f, 0);
		this.attachChild(view_name);

		/*target_details = new TextArea("target_details", font_large, 1, "Weapon Range: " + module.players_ship.shipdata.weapon.getRange(), false);
		target_details.setLocalTranslation(ship_stats_width, target_details.getLineHeight()*3, 0);
		this.attachChild(target_details);*/

		// Damage box
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", this.dam_box_col);
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		damage_box = new Geometry("damagebox", new Quad(w, h));
		damage_box.move(0, 0, 0);
		damage_box.setMaterial(mat);
		this.attachChild(damage_box);

		// Targetter
		targetting_reticule = new Picture("HUD Picture");
		targetting_reticule.setImage(assetManager, "Textures/circular_recticle.png", true);
		this.crosshairs_w = w/10;
		targetting_reticule.setWidth(crosshairs_w);
		this.crosshairs_h = h/10;
		targetting_reticule.setHeight(crosshairs_h);
		this.attachChild(targetting_reticule);

		// Crosshairs
		crosshairs = new Picture("crosshairs");
		crosshairs.setImage(assetManager, "Textures/triangle_recticle.png", true);
		float cw = w/14;
		float ch = cw; 
		crosshairs.setWidth(cw);
		crosshairs.setHeight(ch);
		crosshairs.setPosition((w-cw)/2, (h-ch)/2);
		this.attachChild(crosshairs);

		// Circle big
		Picture circle_big = new Picture("circle_big");
		circle_big.setImage(assetManager, "Textures/hud_circle.png", true);
		circle_big.setWidth(ManualShipControlModule.BIG_CIRCLE_RAD*2);
		circle_big.setHeight(ManualShipControlModule.BIG_CIRCLE_RAD*2);
		circle_big.setPosition((w-ManualShipControlModule.BIG_CIRCLE_RAD*2)/2, (h-ManualShipControlModule.BIG_CIRCLE_RAD*2)/2);
		this.attachChild(circle_big);

		// Circle small
		Picture circle_small = new Picture("circle_small");
		circle_small.setImage(assetManager, "Textures/hud_circle.png", true);
		circle_small.setWidth(ManualShipControlModule.SMALL_CIRCLE_RAD*2);
		circle_small.setHeight(ManualShipControlModule.SMALL_CIRCLE_RAD*2);
		circle_small.setPosition((w-ManualShipControlModule.SMALL_CIRCLE_RAD*2)/2, (h-ManualShipControlModule.SMALL_CIRCLE_RAD*2)/2);
		this.attachChild(circle_small);

		health_bar = new HealthBar(assetManager, w/3, h/20);
		health_bar.setLocalTranslation(w/2, h * 0.95f, 0f);
		this.attachChild(health_bar);

		this.updateGeometricState();

		this.setModelBound(new BoundingBox());
		this.updateModelBound();

	}


	@Override
	public void process(float tpf) {
		//if (ship_data != null) {
		//our_ship_stats.update();
		//enemy_ship_stats.update();
		//}

		if (process_damage_box) {
			this.dam_box_col.a -= (tpf/2);
			if (dam_box_col.a < 0) {
				dam_box_col.a = 0;
				process_damage_box = false;
			}
		}

		/*StringBuffer str = new StringBuffer();
		str.append("Shields: " + String.format("%d", (int)(module.players_ship.shipdata.shield.getLevel()*100)) + "\n");
		str.append("Hull: " + String.format("%d", (int)(module.players_ship.shipdata.getHealth()*100)) + "\n");
		str.append("Temp: " + String.format("%d", (int)(module.players_ship.shipdata.weapon.getCurrentTemp()*100)) + "\n");
		str.append("Missiles: " + module.players_ship.shipdata.missile_launcher.getNumMissiles() + "\n");
		hud_node.ta_left.setText(str.toString());*/

	}


	public void updateAllText(ShipData data) {
		this.setSpeedText(data.getCurrentSpeed());
		this.setShieldsText(data.shield.getLevel());
		this.setHullText(data.getHealth());
		this.setTempText(data.weapon.getCurrentTemp());
		this.setEquipmentText(data.getSelectedEquipment());
		this.setSpaceStationHealthText();
	}


	public void setSpeedText(float a) {
		this.speed.setText("Speed: " + String.format("%d", (int)(a*100)));
	}


	public void setShieldsText(float a) {
		this.shields.setText("Shields: " + String.format("%d", (int)(a*100)));  //shields.getText();
	}


	public void setHullText(float a) {
		this.hull.setText("Hull: " + String.format("%d", (int)(a*100)) + "%");
	}


	public void setTempText(float a) {
		this.temp.setText("Temp: " + String.format("%d", (int)(a*100)) + "%");
	}


	public void setCargoText(int a) {
		this.cargo.setText("Cargo: " + a);
	}


	public void setSpaceStationHealthText() {
		if (module.spacestation != null) { // Might not have been created yet!
			this.spacestation_health.setText("SpaceStation: " + String.format("%d", (int)(this.module.spacestation.health*100)) + "%");
		}
	}


	public void setEquipmentText(AbstractEquipment eq) {
		if (eq != null) {
			this.equipment.setText(eq.getName()  + ": " + eq.getDisplayValue());
		} else {
			this.equipment.setText("No equipment selected");
		}
	}


	public void log(String s) {
		this.log_ta.addLine(s);
	}


	public void startDamageBox() {
		//SparkDamageEffect spark = new SparkDamageEffect(module, module.getAssetManager(), this.module.players_ship, 1f);
		//spark.addToGame(module.rootNode);

		process_damage_box = true;
		this.dam_box_col.a = .5f;
		this.dam_box_col.r = 1f;
		this.dam_box_col.g = 0f;
		this.dam_box_col.b = 0f;
	}


	public void startSunblindBox(float amt) {
		module.debug("Sunblind:" + amt);
		if (amt > this.dam_box_col.a) {
			process_damage_box = true;
			this.dam_box_col.a = amt;
			this.dam_box_col.r = 1f;
			this.dam_box_col.g = 1f;
			this.dam_box_col.b = 0f;
		}
	}


	public void startAtmosBox() {
		process_damage_box = true;
		this.dam_box_col.a = 1f;
		this.dam_box_col.r = .7f;
		this.dam_box_col.g = .7f;
		this.dam_box_col.b = .7f;
	}


	public void startWormholeBox() {
		process_damage_box = true;
		this.dam_box_col.a = 1f;
		this.dam_box_col.r = .7f;
		this.dam_box_col.g = .0f;
		this.dam_box_col.b = .7f;
	}


	public void setTargetterPosition(float x, float y) {
		targetting_reticule.setPosition(x-(this.crosshairs_w/2), y-(this.crosshairs_h/2));
	}


	public void showTargetter(boolean b) {
		if (b) {
			targetting_reticule.setCullHint(CullHint.Inherit);
		} else {
			targetting_reticule.setCullHint(CullHint.Always);
		}
	}


	public void showRestart(boolean victory) {
		TextArea restart = new TextArea("restart", module.guiFont_large, -1, "", false);
		if (victory) {
			restart.addLine("VICTORY!");
		} else {
			restart.addLine("You have been destroyed");
		}
		restart.addLine("\n\n- Press Escape to restart -");
		restart.setLocalTranslation(module.getCam().getWidth()/2, module.getCam().getHeight() * .5f, 0);
		this.attachChild(restart);

	}

}
