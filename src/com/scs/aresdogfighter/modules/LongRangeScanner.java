package com.scs.aresdogfighter.modules;
/*
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.scs.aresdogfighter.AresDogfighter;
import com.scs.aresdogfighter.data.SectorData;
import com.scs.aresdogfighter.gui.TextArea;
import com.scs.aresdogfighter.scannerobjects.AbstractScannerObject;
import com.scs.aresdogfighter.scannerobjects.PlanetScannerObject;

public class LongRangeScanner extends AbstractModule implements AnalogListener, ActionListener {

	private static final float FIXED_Z = 30f;
	private static final String CMD_PICK_TARGET = "pick_target";

	private float screen_height;
	public TextArea log;
	private AbstractScannerObject selected_planet;

	public LongRangeScanner(AresDogfighter _game, BitmapFont _guiFont_small, BitmapFont _guiFont_large, float _screen_height) {
		super(_game, AresDogfighter.Module.LONG_RANGE_SCANNER, _guiFont_small, _guiFont_large);
		
		screen_height = _screen_height;
	}


	@Override
	public void firstSetup() {
		log = new TextArea("log", this.guiFont_small, 6, "", true);
		log.setLocalTranslation(0, screen_height, 0);
		this.guiNode.attachChild(log);
		
		if (this.rootNode.getChildren().size() == 0) {
			for (int y=0 ; y<UniverseData.SIZE ; y++) {
				for (int x=0 ; x<UniverseData.SIZE ; x++) {
					if (game.game_data.universe.sector[x][y] != null) {
						SectorData s = game.game_data.universe.sector[x][y];
						
						PlanetScannerObject p = new PlanetScannerObject(game.getAssetManager());
						p.getSpatial().move(s.x * 5, s.y * 5, 0f);
						this.rootNode.attachChild(p.getSpatial());

					}
				}			
			}
		}

		cam.setLocation(new Vector3f(10f, 10f, FIXED_Z));
		//this.cam.lookAt(new Vector3f(game.game_data.sector_x, game.game_data.sector_y, 0f), Vector3f.UNIT_Y);

		inputManager.addMapping(CMD_PICK_TARGET, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		this.inputManager.addListener(this, CMD_PICK_TARGET);

	}


	@Override
	public void reload() {
		setupLight();

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
		al.setColor(ColorRGBA.White.mult(.1f));//25f));//.mult(2.5f));
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
		
	}


	@Override
	public void simpleUpdate(float tpf) {
		// Always look at map
		this.cam.getDirection().x = 0;
		this.cam.getDirection().y = 0;
		this.cam.getDirection().z = -1;

		this.cam.getLocation().z = FIXED_Z;
	}


	@Override
	public void onAnalog(String name, float value, float tpf) {
		if (name.equals(CMD_PICK_TARGET)) {
			long now = System.currentTimeMillis();
			// Reset results list.
			CollisionResults results = new CollisionResults();
			// Convert screen click to 3d position
			Vector2f click2d = inputManager.getCursorPosition();
			Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
			Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();

			Ray ray = new Ray(click3d, dir);

			// Collect intersections between ray and all nodes in results list.
			rootNode.collideWith(ray, results);
			long took = System.currentTimeMillis() - now;
			//log("Took " + took + " to get collisions");
			for (int i = 0; i < results.size(); i++) {
				Node n = (Node)results.getCollision(i).getGeometry().getParent();
				Integer sid = n.getUserData("id");
				if (sid != null) {
					GameObject obj = AbstractModel.objmap.get(sid);
					if (obj != null && obj.selectable) {
						took = System.currentTimeMillis() - now;
						this.objectSelected(obj);
						break;
					}
				}
			}
		}
	}

	
	private void objectSelected(AbstractScannerObject obj) {
		selected_planet = obj;
		log.addLine(obj + " selected");
	}

	
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
	}


	@Override
	public void log(String s) {
		
	}


	@Override
	public void muteChanged() {
		
	}
}
*/