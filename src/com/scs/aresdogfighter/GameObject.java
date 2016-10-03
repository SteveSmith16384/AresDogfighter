package com.scs.aresdogfighter;

import java.io.IOException;
import java.util.Iterator;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.hud.HealthBar;
import com.scs.aresdogfighter.model.SmallExplosionModel;
import com.scs.aresdogfighter.model.StarDust;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

/*
 * GameObject is anything that has a 3D object, like spaceship, debris, planet etc..
 */
public abstract class GameObject implements IProcessable, Savable {

	public enum ShowMode {
		ALWAYS, SPACE, PLANET;
	}

	protected ManualShipControlModule module;
	public String name;
	public SpaceEntity.Type type;
	public boolean can_be_targetted, collides, ai_avoid;
	protected boolean alive = true;

	public float dist_from_player, dist_from_cam;

	public boolean show_in_hud;
	protected BitmapText hudText;
	protected Geometry hud_dot;

	protected SmallExplosionModel expl_model;
	private AudioNode explosion_sound;
	private float show_expl_until;

	//public GhostControl ghost_control;
	public GameObject collided_with;

	public ShowMode show_mode;

	protected boolean show_health_bar;
	protected HealthBar health_bar;
	private float radius = -1;

	protected Node up, down, left, right, front, rear;

	public GameObject(ManualShipControlModule _game, SpaceEntity.Type _type, String _name, BitmapFont guiFont, boolean _can_be_targetted, boolean _collides, boolean _ai_avoid, boolean _show_in_hud, ShowMode _show_mode, boolean _show_health_bar) {
		super();

		module = _game;
		type = _type;
		name = _name;
		//selectable = _selectable;
		can_be_targetted = _can_be_targetted;
		collides = _collides;
		ai_avoid = _ai_avoid;
		show_in_hud = _show_in_hud;
		show_mode = _show_mode;
		show_health_bar = _show_health_bar;

		// Overlay text
		if (show_in_hud && guiFont != null) {
			hudText = new BitmapText(guiFont, false);
			hudText.setSize(guiFont.getCharSet().getRenderedSize());
			hudText.setColor(ColorRGBA.White);
			hudText.setText("to be set");

			int hw = module.getCam().getWidth()/150;
			float hh = module.getCam().getHeight()/150;
			hud_dot = new Geometry("MouseRect", new Quad(hw, hh));
			Material mat = new Material(module.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", getHUDColour()); //new ColorRGBA(1, 0, 0, 0.5f)); // 0.5f is the alpha value
			//mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
			hud_dot.setMaterial(mat);

			if (show_health_bar) {
				health_bar = new HealthBar(module.getAssetManager(), module.getCam().getWidth()/16, module.getCam().getHeight()/60);
				module.tactical_overlay_node.attachChild(health_bar);
			}

		}
	}

	public void postSpatial() {
		up = new Node("up");
		up.setLocalTranslation(0, 2f, 0);
		down = new Node("down");
		down.setLocalTranslation(0, -2f, 0);
		left = new Node("left");
		left.setLocalTranslation(-2f, 0, 0);
		right = new Node("right");
		right.setLocalTranslation(2f, 0, 0);
		front = new Node("front");
		front.setLocalTranslation(0, 0, 2);
		rear = new Node("rear");
		rear.setLocalTranslation(0, 0, -2);

		this.getNode().attachChild(up);
		this.getNode().attachChild(down);
		this.getNode().attachChild(left);
		this.getNode().attachChild(right);
		this.getNode().attachChild(front);
		this.getNode().attachChild(rear);
	}


	@Override
	public void process(float tpf) {
		if (show_expl_until > 0) {
			show_expl_until -= tpf;
			if (show_expl_until <= 0) {
				show_expl_until = 0;
				this.expl_model.stop();
			} else {
				this.expl_model.process();

			}
		}
	}

	// Override if required
	protected ColorRGBA getHUDColour() {
		return ColorRGBA.LightGray; // Default
	}

	public abstract Node getNode(); // Must be node to attach audio etc...


	public float getHealth() {
		// Override if required.
		return 0;
	}


	public boolean isAlive() {
		return alive; 
	}

	public abstract void damage(GameObject shooter, GameObject actual_object, float a, Vector3f point);


	public void addToGame(Node parent) {
		this.module.addObject(this);
		parent.attachChild(this.getNode());
	}


	public void removeFromGame(boolean destroyed, boolean store) {
		//module.debug(name + " removed");

		alive = false;
		this.module.removeObject(this);
		/*if (this.ghost_control != null) {
			try {
				module.bulletAppState.getPhysicsSpace().remove(ghost_control);
			} catch (NullPointerException ex) {
				module.log("Error removing ghost_control from " + this.name);
				ex.printStackTrace();
			}
		}*/
		this.getNode().removeFromParent();
		if (hudText != null) {
			this.hudText.removeFromParent();
		}
		if (this.hud_dot != null) {
			hud_dot.removeFromParent();
		}
		if (show_health_bar) {
			health_bar.removeFromParent();
		}
		if (store) {
			if (this.type != null) {  
				this.module.game.game_data.getCurrentSector().entities.add(new SpaceEntity(this.type, this.getNode().getWorldTranslation().x, this.getNode().getWorldTranslation().z, this.getNode().getWorldTranslation().z));
			}
		}
	}


	public float distance(GameObject o) {
		return distance(o.getNode().getWorldTranslation());
	}


	public float distance(Vector3f pos) {
		float dist = this.getNode().getWorldTranslation().distance(pos);
		return dist;
	}


	public String getName() {
		return name;
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	/*
	public boolean checkForFutureCollision(GameObject go, Ray r) {
		if (r == null) {
			r = new Ray(this.getNode().getWorldTranslation(), this.getNode().getLocalRotation().mult(Vector3f.UNIT_Z).normalizeLocal());
		}
		CollisionResults results = new CollisionResults();
		if (go.collides && go.ai_avoid) {
			//if (go.getSpatial().getWorldBound() != null) {
			//results.clear();
			try {
				go.getNode().collideWith(r, results);
			} catch (UnsupportedCollisionException ex) {
				ex.printStackTrace();
				System.out.println("Spatial: " + go.getNode());
			}
			if (results.size() > 0) {
				//float dist = us.distance(go);
				//if (dist < 10f) {
				return true;
				//}
			}
			//}
		}
		return false;
	}
	 */

	public void updateHUDText() {
		//if (this.dist_from_player < 100f) {
		hudText.setText(name + "\nDist: " + String.format("%d", (int)this.dist_from_player));
		/*} else {
			hudText.setText("");
		}*/
	}


	public void processHUD(Node node, Vector3f screen_pos, FrustumIntersect insideoutside) {
		module.inputManager.setCursorVisible(!Statics.HIDE_MOUSE);

		if (this.show_in_hud && alive) {
			if (this.dist_from_player > module.max_scanner_range) {
				// Hide everything
				this.hudText.removeFromParent();
				this.hud_dot.removeFromParent();
				if (show_health_bar) {
					health_bar.removeFromParent();
				}
			} else {
				// Always show the dot
				if (hud_dot.getParent() == null) {
					node.attachChild(hud_dot);
				}
				if (insideoutside != Camera.FrustumIntersect.Outside && !module.players_avatar.getGameObject().isTargetBehindUs(this)) {
					if (show_health_bar) {
						if (health_bar.getParent() == null) {
							node.attachChild(health_bar);
						}
						health_bar.setLevel(this.getHealth());
						health_bar.setLocalTranslation(screen_pos.x, screen_pos.y, 0f);
					}

					updateHUDText();
					if (hudText.getParent() == null) {
						node.attachChild(hudText);
					}
					//Vector3f textpos = screen_pos.clone();
					hudText.setLocalTranslation(screen_pos.x, screen_pos.y, 0);
				} else {
					this.hudText.removeFromParent();  //hudText.getWorldTranslation()
					if (show_health_bar) {
						health_bar.removeFromParent();
					}
				}

				// Calc position of dot
				if (hud_dot.getParent() != null) {
					Vector3f dot_point = screen_pos.clone();
					if (module.players_avatar.getGameObject().isTargetBehindUs(this)) {
						dot_point.x = dot_point.x * -1;
						dot_point.y = dot_point.y * -1;
					}

					float min_dist = ManualShipControlModule.SMALL_CIRCLE_RAD;
					float max_dist = ManualShipControlModule.BIG_CIRCLE_RAD;

					// Adjust to centre
					dot_point.x -= ManualShipControlModule.CENTRE_X; 
					dot_point.y -= ManualShipControlModule.CENTRE_Y;

					double len = Math.sqrt(Math.pow(dot_point.x, 2) + Math.pow(dot_point.y, 2));
					if (len > max_dist) {
						float new_diff = this.dist_from_player / module.max_scanner_range;
						double frac1 = min_dist / len;
						double frac2 = max_dist / len;
						double frac = ((frac2 - frac1)*new_diff) + frac1; 
						dot_point.x *= frac;
						dot_point.y *= frac;
						// Reposition back
						dot_point.x += ManualShipControlModule.CENTRE_X;
						dot_point.y += ManualShipControlModule.CENTRE_Y;
					}
					hud_dot.setLocalTranslation(dot_point.x, dot_point.y, 0);
				}
			}
		}
	}


	protected void showSmallExplosion(float dur) {
		show_expl_until += dur;
		if (this.expl_model == null) {
			expl_model = new SmallExplosionModel(module.getAssetManager(), module.getRenderManager());
			this.getNode().attachChild(expl_model);
		}

		if (explosion_sound == null) {
			explosion_sound = new AudioNode(module.getAssetManager(), "Sound/explode.wav", false);
			explosion_sound.setPositional(false);
			this.getNode().attachChild(explosion_sound);
		}
		if (!Statics.MUTE) {
			explosion_sound.play();
		}

	}


	protected void addToPhysics(CollisionShape collShape2, Spatial debris) {
		/*ghost_control = new GhostControl(collShape2);
		ghost_control.setSpatial(debris);
		debris.addControl(ghost_control);
		module.bulletAppState.getPhysicsSpace().add(ghost_control);
		 */
	}

	@Override
	public void write(JmeExporter ex) throws IOException {

	}


	@Override
	public void read(JmeImporter im) throws IOException {

	}


	// Override if required
	public void hail() {
		module.log("This doesn't talk");
	}


	// Override if required
	public Node getGunNode() {
		return this.getNode();
	}


	public boolean isTargetBehindUs(GameObject o) {
		try {
			float dist_front = o.distance(front.getWorldTranslation());
			float dist_rear = o.distance(rear.getWorldTranslation());
			return dist_rear < dist_front;
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public abstract float getSpeed();


	public GameObject checkForFutureCollisions(float max_dist) {
		if (max_dist > 0) {
			Ray r = new Ray(this.getNode().getWorldTranslation(), this.getNode().getLocalRotation().mult(Vector3f.UNIT_Z).normalizeLocal());
			//synchronized (module.objects) {
			CollisionResults results = new CollisionResults();
			Iterator<IProcessable> it = module.getGameObjects().iterator();
			while (it.hasNext()) {
				IProcessable o = it.next();
				if (o instanceof GameObject && o != this) {
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
								float dist = this.distance(go);
								if (dist <= max_dist) {
									return go;
								}
							}
						}
					}
				}
			}
			//}
		}
		return null;
	}


	public boolean canSee(GameObject cansee) {
		Ray r = new Ray(this.getNode().getWorldTranslation(), cansee.getNode().getWorldTranslation().subtract(this.getNode().getWorldTranslation()).normalizeLocal());
		//synchronized (module.objects) {
		//if (go.collides) {
		CollisionResults results = new CollisionResults();
		Iterator<IProcessable> it = module.getGameObjects().iterator();
		while (it.hasNext()) {
			IProcessable o = it.next();
			if (o instanceof GameObject && o != this) {
				GameObject go = (GameObject)o;
				if (go.collides) {
					if (go.getNode().getWorldBound() != null) {
						results.clear();
						try {
							go.getNode().collideWith(r, results);
						} catch (UnsupportedCollisionException ex) {
							System.out.println("Spatial: " + go.getNode());
							ex.printStackTrace();
						}
						if (results.size() > 0) {
							float go_dist = this.distance(cansee)-1;
							/*Iterator<CollisionResult> it = results.iterator();
							while (it.hasNext()) {*/
							CollisionResult cr = results.getClosestCollision();
							if (cr.getDistance() < go_dist) { // todo - check
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

/*	public boolean canSee_OLD(GameObject go) {
		Ray r = new Ray(this.getNode().getWorldTranslation(), go.getNode().getWorldTranslation().subtract(this.getNode().getWorldTranslation()).normalizeLocal());
		//synchronized (module.objects) {
		if (go.collides) {
			CollisionResults results = new CollisionResults();
			if (go.getNode().getWorldBound() != null) {
				results.clear();
				try {
					module.rootNode.collideWith(r, results);
				} catch (UnsupportedCollisionException ex) {
					System.out.println("Spatial: " + go.getNode());
					ex.printStackTrace();
				}
				if (results.size() > 0) {
					float go_dist = this.distance(go)-1;
					Iterator<CollisionResult> it = results.iterator();
					while (it.hasNext()) {
						CollisionResult cr = it.next();
						if (cr.getGeometry().getParent() instanceof StarDust == false) { // todo - ignore anything non-GameObject
							//if (cr.getDistance() > go_dist) { // todo - check
							if (cr.getGeometry().getParent().getParent() == go.getNode()) {
								return true;
							} else {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
*/
}
