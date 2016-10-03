package com.scs.aresdogfighter.gameobjects;

import ssmith.lang.NumberFunctions;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.JMEFunctions;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.ai.AbstractShipAI;
import com.scs.aresdogfighter.ai.EnemyShipAI;
import com.scs.aresdogfighter.ai.FriendlyShipAI;
import com.scs.aresdogfighter.ai.PoliceShipAI;
import com.scs.aresdogfighter.avatars.PlayersShip;
import com.scs.aresdogfighter.data.CaptainsOrder;
import com.scs.aresdogfighter.data.CaptainsOrder.OrderType;
import com.scs.aresdogfighter.data.Engine;
import com.scs.aresdogfighter.data.Faction;
import com.scs.aresdogfighter.data.MissileLauncher;
import com.scs.aresdogfighter.data.Shield;
import com.scs.aresdogfighter.data.ShipData;
import com.scs.aresdogfighter.data.SpaceEntity;
import com.scs.aresdogfighter.data.Weapon;
import com.scs.aresdogfighter.model.CargoTransport;
import com.scs.aresdogfighter.model.CobraModel;
import com.scs.aresdogfighter.model.Eagle5Transport;
import com.scs.aresdogfighter.model.FalconT45RescueShip;
import com.scs.aresdogfighter.model.FederationInterceptor;
import com.scs.aresdogfighter.model.ForceField;
import com.scs.aresdogfighter.model.OrbiterBugship;
import com.scs.aresdogfighter.model.SimpleModel;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

/*
 * Check if within range
 * 
 * Scenarios:-
 * Head on collision - Turn until player behind
 * AI being chased - Turn towards player (do nothing)
 * AI Chasing - Slow down
 * 
 * Misc:-
 * Check ship doesn't 'bounce off' zone
 * Check target doesn't do donuts
 * 
 * Problems with AVOID_CRASH:-
 * If the ships just turnsAwayFrom(), they will be perpetually flying away if being chased.
 * 
 */
public class Ship1 extends GameObject {

	private static final float MAX_TURN_TIME = 7;
	private static final float AVOID_CHECK_INT = 2;
	private static final float AVOID_TARGET_DIST = 17f; // multiplied by speed was 7f
	private static float ROT_CLOSE = 0.8f;//15f;
	private static final float SHOOT_ANGLE = .15f;

	// Manouvre types
	private static final int MVR_ROLL_LEFT = 1;
	private static final int MVR_ROLL_RIGHT = 2;
	private static final int MVR_PITCH_UP = 3;
	private static final int MVR_PITCH_DOWN = 4;
	private static final int MAX_MANOUVRES = 4;


	private Node ship_model;
	public ShipData shipdata;
	public final CaptainsOrder order;
	public AbstractShipAI ai;
	public int faction;
	protected Node gun_node;
	private Quaternion rot_q = new Quaternion();
	private float manouvre_until;
	private float next_ship_trail = 0;
	private float time_spent_turning = 0;
	private int curr_manouvre;
	private Vector3f prev_pos;
	protected boolean already_docked = false;

	private GameObject avoid_obj; // used this for planets/spacestation
	private float next_avoid_check = AVOID_CHECK_INT;

	public AudioNode cant_shoot;

	public static Ship1 Factory(ManualShipControlModule game, AssetManager assetManager, BitmapFont guiFont, SpaceEntity.Type type) {
		Ship1 ship = null;
		switch (type) {
		case PLAYERS_SHIP:
			ship = new PlayersShip(game, assetManager, guiFont);
			if (Statics.SIMPLE_GRAPHICS == false && !Statics.USE_LOWPOLY) {
				ship.ship_model = new FalconT45RescueShip(assetManager);
			} else {
				ship.ship_model = SimpleModel.GetSpaceShip(assetManager);// new CobraModel(assetManager);
			}
			ship.shipdata.addEquipment(MissileLauncher.Factory(game));
			break;

		case FRIENDLY_FIGHTER:
			ship = new Ship1(game, type, Faction.GetName(Faction.PLAYER_FRIENDLY) + " Ship", assetManager, guiFont, 
					Shield.Factory(game, Shield.Type.SHIELD_1), 
					Engine.Factory(game, Engine.Type.STD_ENGINE), 
					Weapon.Factory(game, Weapon.Type.STD_LASER), 
					Faction.PLAYER_FRIENDLY, 0f);
			ship.ai = new FriendlyShipAI(game, ship);
			if (Statics.SIMPLE_GRAPHICS == false && !Statics.USE_LOWPOLY) {
				ship.ship_model = new FederationInterceptor(assetManager);
			} else {
				ship.ship_model = SimpleModel.GetMediumShip(assetManager);//new CobraModel(assetManager);
			}
			break;

			/*case NIBLY_CAPITAL:
			ship = new Ship1(game, type, Faction.GetName(Faction.NIBLYS) + " Capital Ship", assetManager, guiFont, 
					Shield.Factory(game, Shield.Type.SHIELD_1), 
					Engine.Factory(game, Engine.Type.FAST_SPEED_SLOW_TURN), 
					Weapon.Factory(game, Weapon.Type.STD_LASER),
					Faction.NIBLYS, 5f);
			ship.ai = new FriendlyShipAI(game, ship);
			if (Statics.SIMPLE_GRAPHICS == false) {
				ship.ship_model = new GalacticCruiserCapitalShip(assetManager);
			} else {
				ship.ship_model = new CobraModel(assetManager);
			}
			break;*/

		case ENEMY_FIGHTER:
			ship = new Ship1(game, type, Faction.GetName(Faction.ENEMY_SHIP) + " Ship", assetManager, guiFont, 
					Shield.Factory(game, Shield.Type.SHIELD_1), 
					Engine.Factory(game, Engine.Type.STD_ENGINE), 
					Weapon.Factory(game, Weapon.Type.STD_LASER), 
					Faction.ENEMY_SHIP, 0f);
			ship.ai = new EnemyShipAI(game, ship);
			if (Statics.SIMPLE_GRAPHICS == false && !Statics.USE_LOWPOLY) {
				ship.ship_model = new Eagle5Transport(assetManager);
			} else {
				//ship.ship_model = new CobraModel(assetManager);
				//ship.ship_model = new UFOModel(assetManager);
				ship.ship_model = SimpleModel.GetCroissantShip(assetManager); //new SpaceshipModel(assetManager);
			}
			break;

		case POLICE_SHIP:
			ship = new Ship1(game, type, "Police Ship", assetManager, guiFont, 
					Shield.Factory(game, Shield.Type.SHIELD_2), 
					Engine.Factory(game, Engine.Type.FAST_SPEED_FAST_TURN), 
					Weapon.Factory(game, Weapon.Type.SNIPER_LASER), 
					Faction.POLICE, 0f);
			ship.ai = new PoliceShipAI(game, ship);
			if (Statics.SIMPLE_GRAPHICS == false && !Statics.USE_LOWPOLY) {
				ship.ship_model = new OrbiterBugship(assetManager);
			} else {
				ship.ship_model = new CobraModel(assetManager);
			}
			break;

			/*case CIVILIAN:
			ship = new Ship1(game, type, "Civilian Ship", assetManager, guiFont, 
					Shield.Factory(game, Shield.Type.SHIELD_1), 
					Engine.Factory(game, Engine.Type.CIV_ENGINE), 
					Weapon.Factory(game, Weapon.Type.NONE), 
					Faction.CIVILIAN, 0f);
			ship.ai = new CivilianShipAI(game, ship);
			if (Statics.SIMPLE_GRAPHICS == false) {
				ship.ship_model = new CargoTransport(assetManager);
			} else {
				ship.ship_model = new CobraModel(assetManager);
			}
			break;*/

		default:
			throw new RuntimeException("Unknown ship type: "+type);

		}

		ship.ship_model.setUserData("gameobject", ship);

		ship.ship_model.attachChild(ship.gun_node);

		ship.postSpatial();

		/*ship.up = new Node("up");
		ship.up.setLocalTranslation(0, 2f, 0);
		ship.down = new Node("down");
		ship.down.setLocalTranslation(0, -2f, 0);
		ship.left = new Node("left");
		ship.left.setLocalTranslation(-2f, 0, 0);
		ship.right = new Node("right");
		ship.right.setLocalTranslation(2f, 0, 0);
		ship.front = new Node("front");
		ship.front.setLocalTranslation(0, 0, 2);
		ship.rear = new Node("rear");
		ship.rear.setLocalTranslation(0, 0, -2);

		ship.ship_model.attachChild(ship.up);
		ship.ship_model.attachChild(ship.down);
		ship.ship_model.attachChild(ship.left);
		ship.ship_model.attachChild(ship.right);
		ship.ship_model.attachChild(ship.front);
		ship.ship_model.attachChild(ship.rear);
		 */
		ship.cant_shoot = new AudioNode(assetManager, "Sound/cant_shoot.wav", false);
		ship.cant_shoot.setPositional(false);
		ship.ship_model.attachChild(ship.cant_shoot);

		/*if (Statics.SHOW_WIREFRAME) {
			//if (ship.ship_model.getWorldBound() instanceof BoundingBox) {
			//BoundingBox bbox = (BoundingBox)ship.ship_model.getWorldBound();
			MyWireframe wf = new MyWireframe(ship.ship_model.getWorldBound(), "bbox", assetManager);
			ship.ship_model.attachChild(wf);
			//}
		}*/

		ship.finishLoadout();

		game.inputManager.setCursorVisible(!Statics.HIDE_MOUSE);

		//CollisionShape collShape2 = new SphereCollisionShape(3f); // Must be circular!
		//CollisionShape collShape2 = CollisionShapeFactory.createDynamicMeshShape(ship.getSpatial());
		//ship.addToPhysics(collShape2, ship.ship_model);
		return ship;
	}


	protected Ship1(ManualShipControlModule game, SpaceEntity.Type type, String name, AssetManager assetManager, BitmapFont guiFont, Shield _shield, Engine _engine, Weapon _weapon, int _faction, float extra_mass) {
		super(game, type, name, guiFont, true, true, true, true, ShowMode.SPACE, true);

		shipdata = new ShipData(game, this, _shield, _engine, _weapon, extra_mass);
		faction = _faction;
		order = new CaptainsOrder(game, shipdata);

		gun_node = new Node("gun");
		if (type == SpaceEntity.Type.PLAYERS_SHIP) {
			gun_node.setLocalTranslation(.3f, -.2f, 0f);
		}
		this.hud_dot.getMaterial().setColor("Color", getHUDColour()); // Set this here as it depends on the faction which has only just been set
	}


	@Override
	protected ColorRGBA getHUDColour() {
		if (this.faction == Faction.ENEMY_SHIP) {
			return ColorRGBA.Red;
		} else {
			return ColorRGBA.Green;

		}
	}


	@Override
	public void process(float tpf) {
		super.process(tpf);

		if (this.faction == Faction.ENEMY_SHIP) {
			module.num_enemies++;
		}

		shipdata.process(tpf);

		if (ai != null) {
			ai.process(tpf);

			next_avoid_check -= tpf;
			if (next_avoid_check < 0) {
				/*if (Statics.DEBUG_AI) {
					module.debug(this + " is checking for future collisions");
				}*/
				avoid_obj = this.checkForFutureCollisions(this.getAvoidDistance());
				if (Statics.DEBUG_AI) {
					if (avoid_obj != null) {
						next_avoid_check = AVOID_CHECK_INT*3; // Longer to keep turning for longer
						//module.debug(this + " is avoiding crashing into " + avoid_obj);
					} else {
						next_avoid_check = AVOID_CHECK_INT;
						//module.debug(this + " is not avoiding anything");
					}
				}
			}
			if (avoid_obj != null) {
				module.debug(this + " is avoiding crashing into " + avoid_obj);
				/*if (avoid_obj == this.order.target) {
					this.order.setType(OrderType.TURN_UNTIL_TARGET_BEHIND);
				} else {*/
				this.turnAway(avoid_obj.getNode().getWorldTranslation(), tpf);
				//}
			} else {
				float dist_from_target = -1;
				if (order.target != null) {
					if (order.target.isAlive() == false || order.target == this) {
						order.target = null;
					} else {
						dist_from_target = ship_model.getWorldTranslation().distance(order.target.getNode().getWorldTranslation());
					}
				}

				boolean target_behind_us = false;
				if (order.target != null) {
					target_behind_us = this.isTargetBehindUs(order.target);// dist_rear < dist_front;
				}

				boolean were_behind_target = false;
				if (order.target != null) {
					were_behind_target = order.target.isTargetBehindUs(this);
				}

				int accelerate = 0; // -1=decel, 1=accel
				float ang_to_target = 0;
				if (order.target != null) {
					ang_to_target = JMEFunctions.GetAngleBetween(this.getNode(), order.target.getNode().getWorldTranslation());

					if (dist_from_target <= this.getAvoidDistance()) {
						if (ang_to_target < 0.2) {
							if (!target_behind_us && !were_behind_target) { // Head-on collision!
								this.order.setType(OrderType.TURN_UNTIL_TARGET_BEHIND);
							} else if (target_behind_us && !were_behind_target) { // Being chased
								// Do nothing - AI will turn towards
							} else if (!target_behind_us && were_behind_target) { // We're chasing
								accelerate = -1;
							}
						}
					}
				}

				switch (order.getType()) {
				case ATTACK:
					if (order.target != null) {
						accelerate = 1;
						this.turnTowards(order.target.getNode().getWorldTranslation(), tpf, 1);
						float wep_rng = this.shipdata.getWeaponRange();
						if (dist_from_target <= wep_rng) {
							//module.log("Ang=" + ang);
							if (this.order.target.getSpeed() <= 0) {
								accelerate = -1; // Might as well stop if the target isn't moving
							}
							if (ang_to_target < SHOOT_ANGLE) {// facing target!
								//module.log("AI Shooting");
								if (Statics.AI_SHOOTS) {
									Vector3f aim_at = order.target.getNode().getWorldTranslation();
									// shoot in front of target
									Vector3f forward = order.target.getNode().getLocalRotation().mult(Vector3f.UNIT_Z);
									aim_at = JMEFunctions.GetCollisionPoint(order.target.getNode().getWorldTranslation(), forward.multLocal(order.target.getSpeed()), this.getNode().getWorldTranslation(), AbstractBullet.SPEED);
									this.shoot(aim_at);
								}
							}
						}
					} else {
						order.setType(CaptainsOrder.OrderType.HALT);
					}
					break;

				case TURN_UNTIL_TARGET_BEHIND:
					if (order.target == null) {
						accelerate = -1;
						order.setType(CaptainsOrder.OrderType.HALT);
					} else {
						accelerate = -1;
						if (target_behind_us == false) {
							// Keep turning
							this.turnAway(order.target.getNode().getWorldTranslation(), tpf);
						} else {
							if (order.target.getSpeed() > 0) {
								startEvasiveManouvre(); // Only need evasive manouvre if dogfighting
							} else {
								order.setType(CaptainsOrder.OrderType.ATTACK);
							}
						}
					}
					break;

				case EVASIVE_MANOUVRE:
					this.manouvre_until -= tpf;
					if (this.manouvre_until <= 0 && (target_behind_us || dist_from_target > getAvoidDistance())) {
						manouvre_until = 0;
						order.setType(CaptainsOrder.OrderType.ATTACK);
					} else {
						switch (this.curr_manouvre) {
						case MVR_ROLL_LEFT:
							this.rollLeft(tpf);
							break;
						case MVR_ROLL_RIGHT:
							this.rollRight(tpf);
							break;
						case MVR_PITCH_UP:
							this.pitchUp(tpf);
							break;
						case MVR_PITCH_DOWN:
							this.pitchDown(tpf);
							break;
						}
						accelerate = 1;
					}
					break;

				case MOVE_TOWARDS:
					if (order.target != null) {
						this.turnTowards(order.target.getNode().getWorldTranslation(), tpf, 1);
						if (dist_from_target < 10f) {
							accelerate = -1;
						} else {
							accelerate = 1;
						}
					} else {
						order.setType(CaptainsOrder.OrderType.HALT);
					}
					break;

				case EVADE:
					if (order.target != null) {
						JMEFunctions.TurnAwayFrom(ship_model, order.target.getNode().getWorldTranslation(), tpf * shipdata.getTurnSpeed(tpf));
						rollLeftRightToTarget(tpf, order.target.getNode().getWorldTranslation(), false);
						if (dist_from_target > Statics.OBJ_CREATION_DIST) {
							this.order.setOrder(OrderType.HALT, null); // Need this to tell AI we're ready for another order
							if (Statics.DEBUG_AI) {
								module.log("Reached destination");
							}
						} else {
							accelerate = 1;
						}
					} else {
						order.setType(CaptainsOrder.OrderType.HALT);
					}
					break;

				case HALT:
					accelerate = -2;
					break;

				/*case MANUAL_CONTROL:
					// Do nothing
					break;*/

				case FLY_AWAY:
					accelerate = 1;
					break;

				default:
					throw new RuntimeException("Unknown command type: " + order.getType());
				}

				module.inputManager.setCursorVisible(!Statics.HIDE_MOUSE);

				if (accelerate == 1 || this.shipdata.getCurrentSpeed() < 0) {
					this.shipdata.accelerate(tpf);
				} else if (accelerate == -1) {
					this.shipdata.slowDown(tpf);
				} else if (accelerate == -2) {
					this.shipdata.decelerate(tpf);
				}
			}
		}

		prev_pos = this.ship_model.getLocalTranslation().clone();
		if (this.shipdata.getCurrentSpeed() != 0) {
			already_docked = false;
			next_ship_trail -= this.shipdata.getCurrentSpeed();
			JMEFunctions.MoveForwards(ship_model, tpf * this.shipdata.getCurrentSpeed());
			ship_model.updateGeometricState();
			if (Statics.SHIP_TRAILS && !Statics.SIMPLE_GRAPHICS && this != module.players_avatar && next_ship_trail <= 0) { // don't create one each time
				next_ship_trail = SmokeTrail.INTERVAL;
				new SmokeTrail(module, module.getAssetManager(), module.guiFont_small, this.getNode().getWorldTranslation(), false).addToGame(module.rootNode);
			}
		}
		if (module.checkForCollisions(this)) {
			// Move back
			if (this.shipdata.getCurrentSpeed() != 0) {
				this.ship_model.setLocalTranslation(prev_pos);
				ship_model.updateGeometricState();
				this.shipdata.setCurrSpeed(0);
			}
		}

		/*		if (this.forcefield != null) {
			this.forcefield.setLocalTranslation(this.getSpatial().getWorldTranslation());
		}*/
	}


	private void startEvasiveManouvre() {
		curr_manouvre = NumberFunctions.rnd(1, MAX_MANOUVRES);
		order.setType(CaptainsOrder.OrderType.EVASIVE_MANOUVRE);
		manouvre_until = 7;
	}


	@Override
	public Node getNode() {
		return ship_model;
	}


	@Override
	public Node getGunNode() {
		return this.gun_node;
	}


	public void damage(GameObject shooter, GameObject actual_object, float amt, Vector3f point) {
		if (shipdata.getHealth() > 0) { // Don't do anything if already destroyed
			if (actual_object instanceof Ship1 == false) { //  Don't show explosion if ships colliding
				this.showSmallExplosion( Statics.SMALL_EXPLOSION_DURATION);
				//new SmokeTrail(module, module.getAssetManager(), module.guiFont_small, point, true).addToGame(module.rootNode);
			}
			float new_amt = amt * this.shipdata.shield.getLevel();//.reduceLevel(amt);
			if (new_amt > 0) {
				if (Statics.SHIPS_GET_DAMAGE) {
					this.shipdata.damage(new_amt);
				}
				/*if (Statics.DEBUG) {
					module.debug(getName() + " damaged " + String.format("%d", (int)(new_amt*100)) + " by " + shooter);
				}*/
			}
			Ship1 ship_shooter = null;
			if (shooter instanceof Ship1) {
				ship_shooter = (Ship1)shooter;
			}
			if (shipdata.getHealth() <= 0) {
				this.removeFromGame(true, false);
				if (ship_shooter != null) {
					//ship_shooter.shipdata.credits += this.shipdata.bounty;
					//this.shipdata.bounty = 0;
					ManualShipControlModule mod = (ManualShipControlModule)module;
					mod.shipDestroyed(this, ship_shooter);
				}
			} else {
				if (ai != null && ship_shooter != null) {
					ai.damagedBy2(ship_shooter, amt); 
				} else if (this == module.players_avatar) {
					module.log("Hit by " + shooter);
					module.hud.startDamageBox();
					//module.hud.our_ship_stats.needs_to_update = true; // To move health bars
				}
			}
		}
	}


	/*	public float getWeaponAccuracy() {
		return this.shipdata.getWeaponAccuracy();
	}
	 */

	public float getWeaponRange() {
		return this.shipdata.getWeaponRange();
	}


	public float getWeaponDamage() {
		return shipdata.getWeaponDamage();
	}


	public boolean checkIfCanShoot() {
		return shipdata.weapon.checkIfCanShoot();
	}


	@Override
	public void updateHUDText() {
		if (this != module.players_avatar) {
			//hudText.setText(name);
			//if (this == module.selected_obj) { // only show all this if its selected object
			//float dist = this.distance(this.module.players_ship);
			hudText.setText(name + "\nDist: " + String.format("%d", (int)this.dist_from_player) + "\n" + 
			((this.avoid_obj != null) ? "avoiding " + avoid_obj : this.order.getStringRepresentation()));// + 
					//"\nHull: " + String.format("%d", (int)(this.shipdata.getHealth()*100)) + "%  Shld: " + String.format("%d", (int)(this.shipdata.shield.getLevel()*100)));
			// + "\nB: " + String.format("%d", (int)this.shipdata.bounty) + (Statics.DEBUG_AI?"\n" + this.order.getStringRepresentation():""));
			/*} else {
				hudText.setText("");
			}*/
		} else {
			hudText.setText("");
		}
	}


	@Override
	public String toString() {
		return getName();
	}


	/*	public boolean useSelectedEquipment() {
		if (this.order.target != null) {
			if (this.shipdata.missile_launcher.checkIfCanShoot()) {
				module.log("Missile launched at " + this.order.target.name);
				Missile missile = new Missile(module, module.getAssetManager(), this, this.order.target, 300f, 1f);
				missile.addToGame(module.rootNode);
				return true;
			} else {
				//module.log("Missile launched not ready");
			}
		} else {
			module.log("No target for Missile");
		}
		return false;

	}
	 */ 

	private void turnAway(Vector3f target, float tpf) {
		turnTowards(target, tpf, -1);
	}


	private void turnTowards(Vector3f target, float tpf, int neg) {
		boolean log = false;//this.toString().contains("Quaz");
		if (log) {
			log = true;
		}
		rollLeftRightToTarget(tpf * neg, target, log);

		// Pitch up/down
		float dist_left = target.distance(left.getWorldTranslation());
		float dist_right = target.distance(right.getWorldTranslation());
		float lr_diff = Math.abs(dist_left-dist_right);
		if (lr_diff < ROT_CLOSE) { // Almost facing it, so pitch up/down
			float dist_front = target.distance(front.getWorldTranslation());
			float dist_rear = target.distance(rear.getWorldTranslation());
			boolean behind_us = dist_rear < dist_front;

			float dist_up = target.distance(up.getWorldTranslation());
			float dist_down = target.distance(down.getWorldTranslation());
			float ud_diff = Math.abs(dist_up-dist_down);
			if (ud_diff > 0.05f || behind_us || (neg == -1 && ud_diff < 0.05f)) {
				float frac = 1;
				if (ud_diff < ROT_CLOSE) {
					frac = 1;//ud_diff / ROT_CLOSE;
				} else {
					time_spent_turning += tpf;
					if (time_spent_turning > MAX_TURN_TIME) {
						time_spent_turning = 0;
						if (order.target.getSpeed() > 0) {
							startEvasiveManouvre();
						} else {
							order.setType(CaptainsOrder.OrderType.ATTACK);
						}
					}
				}
				// Rotate to face it
				if (dist_up > dist_down) {
					/*if (log) {
						System.out.println("Pitch down! " + ud_diff + " " + behind);
					}*/
					this.pitchDown(tpf * frac * neg);
				} else {
					/*if (log) {
						System.out.println("Pitch Up! " + ud_diff + " " + behind);
					}*/
					this.pitchUp(tpf * frac * neg);
				}
			}
		}
	}


	private void rollLeftRightToTarget(float tpf, Vector3f target, boolean log) {
		// Tilt left/right
		float dist_left = target.distance(left.getWorldTranslation());
		float dist_right = target.distance(right.getWorldTranslation());
		float lr_diff = Math.abs(dist_left-dist_right);
		if (lr_diff > 0.05f) {
			float frac = 1;
			if (lr_diff < ROT_CLOSE) {
				frac = lr_diff / ROT_CLOSE;
			}
			if (dist_left > dist_right) {
				if (log) {
					System.out.println("Roll Left! " + lr_diff);
				}
				this.rollLeft(tpf * frac);
			} else {
				if (log) {
					System.out.println("Roll Right! " + lr_diff);
				}
				this.rollRight(tpf * frac);
			}
		} else {
			// No need to turn 
		}
	}


	public void pitchUp(float tpf) {
		this.getNode().rotate(rot_q.fromAngleAxis(-1 * this.shipdata.getTurnSpeed(tpf), Vector3f.UNIT_X));
	}


	public void pitchDown(float tpf) {
		this.getNode().rotate(rot_q.fromAngleAxis(1 * this.shipdata.getTurnSpeed(tpf), Vector3f.UNIT_X));
	}


	public void rollLeft(float tpf) {
		this.getNode().rotate(rot_q.fromAngleAxis(-2 * this.shipdata.getTurnSpeed(tpf), Vector3f.UNIT_Z));
	}


	public void rollRight(float tpf) {
		this.getNode().rotate(rot_q.fromAngleAxis(2 * this.shipdata.getTurnSpeed(tpf), Vector3f.UNIT_Z));
	}


	public void turnLeft(float tpf) {
		this.getNode().rotate(new Quaternion().fromAngleAxis(-1 * this.shipdata.getTurnSpeed(tpf), Vector3f.UNIT_Y));
	}


	public void turnRight(float tpf) {
		this.getNode().rotate(new Quaternion().fromAngleAxis(1 * this.shipdata.getTurnSpeed(tpf), Vector3f.UNIT_Y));
	}


	@Override
	public boolean isAlive() {
		return super.alive && this.shipdata.getHealth() > 0; // Check "alive" in case that target has been unloaded
	}


	private float getAvoidDistance() {
		return AVOID_TARGET_DIST * this.shipdata.getCurrentSpeed();
	}


	protected void finishLoadout() {
		shipdata.finishLoadout();
	}


	public void shoot(Vector3f optional_target) {
		if (checkIfCanShoot()) {
			AbstractBullet laser = AbstractBullet.Factory(module, module.getAssetManager(), this, optional_target);
			laser.addToGame(module.rootNode);
			//module.security.shotFired(this);
		} else {
			if (this == module.players_avatar) {
				if (!Statics.MUTE) {
					this.cant_shoot.play();
				}
			}
		}
	}


	public void bounceBack(boolean reverse_speed) {
		if (prev_pos == null) {
			//  We're brand new and there's no room for us!
			module.debug("Removing " + this + " as there is no room");
			this.removeFromGame(false, false);
		} else {
			this.ship_model.setLocalTranslation(prev_pos);
			if (this.shipdata.getCurrentSpeed() > 0) {
				JMEFunctions.MoveForwards(ship_model, this.shipdata.getCurrentSpeed() * -.5f); // Move us back
				if (reverse_speed) {
					this.shipdata.setCurrSpeed(this.shipdata.getCurrentSpeed() * -1);
				}
			}
		}
	}


	@Override
	public float getHealth() {
		return this.shipdata.getHealth();
	}


	@Override
	public void hail() {
		this.ai.hail();
	}


	@Override
	public float getSpeed() {
		return shipdata.getCurrentSpeed();
	}




}
