package com.scs.aresdogfighter.hud;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.data.ShipData;

public class ShipStatsHUD extends Node implements ClickEvent {

	private int width, height;
	private Geometry rect_shields, rect_engines, rect_weapons;
	private Geometry lines[] = new Geometry[6];
	private int bar_width;
	public ShipData shipdata;
	public boolean needs_to_update = true;
	private HUD hud;
	private boolean get_input;

	public ShipStatsHUD(HUD _hud, int w, int h, AssetManager assetManager, BitmapFont guiFont, ShipData _shipdata, boolean _get_input) {
		super("ShipStatsHUD");

		hud = _hud;
		width = w;
		height = h;
		shipdata = _shipdata;
		get_input = _get_input;

		bar_width = w/3;

		// Background
		Material mat_back = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat_back.setColor("Color", new ColorRGBA(1, 1, 1, .1f)); // 0.5f is the alpha value
		mat_back.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		Geometry rect_back = new Geometry("rect_back", new Quad(w, h));
		rect_back.setMaterial(mat_back);
		this.attachChild(rect_back);


		// Shields
		Material mat_shields = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat_shields.setColor("Color", new ColorRGBA(0, 1, 0, 0.5f)); // 0.5f is the alpha value
		mat_shields.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		rect_shields = new Geometry("rect", new Quad(bar_width, h));
		rect_shields.setMaterial(mat_shields);
		this.attachChild(rect_shields);

		// Engines
		Material mat_engines = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat_engines.setColor("Color", new ColorRGBA(0, 0, 1, 0.5f)); // 0.5f is the alpha value
		mat_engines.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		rect_engines = new Geometry("rect", new Quad(bar_width, h));
		rect_engines.setMaterial(mat_engines);
		rect_engines.setLocalTranslation(bar_width, 0, 0);
		this.attachChild(rect_engines);

		// Weapons
		Material mat_weapons = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat_weapons.setColor("Color", new ColorRGBA(1, 0, 0, 0.5f)); // 0.5f is the alpha value
		mat_weapons.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		rect_weapons = new Geometry("rect", new Quad(bar_width, h));
		rect_weapons.setMaterial(mat_weapons);
		rect_weapons.setLocalTranslation(bar_width*2, 0, 0);
		this.attachChild(rect_weapons);

		/*misc_stats = new BitmapText(guiFont);
		misc_stats.setSize(guiFont.getCharSet().getRenderedSize());
		misc_stats.setColor(ColorRGBA.White);
		misc_stats.setText("Stats\nHere");
		misc_stats.setLocalTranslation(bar_width*3, height, 0);
		this.attachChild(misc_stats);
		 */
		this.setModelBound(new BoundingBox());
		this.updateModelBound();

		Material linemat_targ = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		linemat_targ.setColor("Color", new ColorRGBA(.5f, .5f, .5f, 1f));
		linemat_targ.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

		Material linemat_health = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		linemat_health.setColor("Color", new ColorRGBA(1, 1, 0, 1f));
		linemat_health.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

		for (int i=0 ; i<6 ; i++) {
			Line linegeom = new Line(new Vector3f(i*10, 0, 0), new Vector3f(width/3, height/2, 0));
			linegeom.setLineWidth(2);
			lines[i] = new Geometry("line", linegeom);
			if (i % 2 == 0) { // diff colour for damage lines
				lines[i].setMaterial(linemat_targ);
			} else {
				lines[i].setMaterial(linemat_health);
			}
			lines[i].setCullHint(CullHint.Never);
			this.attachChild(lines[i]);
		}

		BitmapText bmp = JMEFunctions.CreateBitmapText(guiFont, "Sh");
		bmp.setLocalTranslation(0, h-bmp.getLineHeight(), 0);
		this.attachChild(bmp);

		bmp = JMEFunctions.CreateBitmapText(guiFont, "En");
		bmp.setLocalTranslation(bar_width, h-bmp.getLineHeight(), 0);
		this.attachChild(bmp);

		bmp = JMEFunctions.CreateBitmapText(guiFont, "Wp");
		bmp.setLocalTranslation(bar_width*2, h-bmp.getLineHeight(), 0);
		this.attachChild(bmp);

	}


	public void update() {
		//if (needs_to_update) {
		if (shipdata != null) {
			//  Shields
			Quad q = (Quad)rect_shields.getMesh();
			q.updateGeometry(bar_width, height * shipdata.getCurrShieldPcent());

			/*Line line = (Line)lines[0].getMesh();
			line.getStart().x = 0;
			line.getStart().y = shipdata.getTargetShieldsPCent() * height;
			line.getEnd().x = this.bar_width;
			line.getEnd().y = shipdata.getTargetShieldsPCent() * height;
			line.updatePoints(line.getStart(), line.getEnd());*/

			Line line = (Line)lines[1].getMesh();
			line.getStart().x = 0;
			line.getStart().y = shipdata.shield.health * height;
			line.getEnd().x = this.bar_width;
			line.getEnd().y = shipdata.shield.health * height;
			line.updatePoints(line.getStart(), line.getEnd());

			// Engines
			q = (Quad)rect_engines.getMesh();
			q.updateGeometry(bar_width, height * shipdata.getCurrEnginePcent());

			/*line = (Line)lines[2].getMesh();
			line.getStart().x = this.bar_width;
			line.getStart().y = shipdata.getTargetEnginePCent() * height;
			line.getEnd().x = this.bar_width*2;
			line.getEnd().y = shipdata.getTargetEnginePCent() * height;
			line.updatePoints(line.getStart(), line.getEnd());*/

			line = (Line)lines[3].getMesh();
			line.getStart().x = this.bar_width;
			line.getStart().y = shipdata.engine.health * height;
			line.getEnd().x = this.bar_width*2;
			line.getEnd().y = shipdata.engine.health * height;
			line.updatePoints(line.getStart(), line.getEnd());

			//Weapons
			q = (Quad)rect_weapons.getMesh();
			q.updateGeometry(bar_width, height * shipdata.getCurrWeaponPcent());

			/*line = (Line)lines[4].getMesh();
			line.getStart().x = this.bar_width*2;
			line.getStart().y = shipdata.getTargetWeaponPCent() * height;
			line.getEnd().x = this.bar_width*3;
			line.getEnd().y = shipdata.getTargetWeaponPCent() * height;
			line.updatePoints(line.getStart(), line.getEnd());*/

			line = (Line)lines[5].getMesh();
			line.getStart().x = this.bar_width*2;
			line.getStart().y = shipdata.weapon.health * height;
			line.getEnd().x = this.bar_width*3;
			line.getEnd().y = shipdata.weapon.health * height;
			line.updatePoints(line.getStart(), line.getEnd());

			this.updateModelBound();
		}
		//}
	}


	@Override
	public void mouseClicked(float x, float y) {
		if (get_input) {
			//SpaceGame.debug("Click: " + x +"," + y);
			/*int col = (int)(x/(width/3));
			float target = (y / height);
			switch (col) {
			case 0:
				shipdata.setTargetShieldPCent(target);
				//hud.log.addLine("Shields set to " + String.format("%d",(int)(target*100)) + "%");
				break;
			case 1:
				shipdata.setTargetEnginePCent(target);
				//hud.log.addLine("Engines set to " + String.format("%d",(int)(target*100)) + "%");
				break;
			case 2:
				shipdata.setTargetWeaponPCent(target);
				//hud.log.addLine("Weapons set to " + String.format("%d",(int)(target*100)) + "%");
				break;
			default:
				//throw new RuntimeException("Unknown graph:" + col);
			}
			needs_to_update = true;*/
		}
	}


	@Override
	public float getLocalY(float y) {
		return y;
	}

}
