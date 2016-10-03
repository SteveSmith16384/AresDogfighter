package com.scs.aresdogfighter;

import com.jme3.bounding.BoundingSphere;
import com.jme3.math.Vector3f;
import com.scs.aresdogfighter.gameobjects.AbstractBullet;
import com.scs.aresdogfighter.gameobjects.AbstractPlanet;
import com.scs.aresdogfighter.gameobjects.Asteroid;
import com.scs.aresdogfighter.gameobjects.Cargo;
import com.scs.aresdogfighter.gameobjects.Missile;
import com.scs.aresdogfighter.gameobjects.MyTerrain;
import com.scs.aresdogfighter.gameobjects.Ship1;
import com.scs.aresdogfighter.gameobjects.SpaceStation;
import com.scs.aresdogfighter.modules.AbstractModule;

public class CollisionLogic {

	private CollisionLogic() {
	}


	/**
	 * Returns whether the colliders should move back
	 */
	public static boolean HandleCollision(AbstractModule module, GameObject obj1, GameObject obj2, Vector3f point) {
		//System.out.println("Collision:" + obj1 + " vs " + obj2);
		if (obj1 instanceof AbstractBullet && obj2 instanceof AbstractBullet) {
			return false; // Do nothing
		}

		if (obj1 instanceof AbstractBullet) {
			return Laser_GameObject(module, (AbstractBullet)obj1, obj2, point);
		} else if (obj2 instanceof AbstractBullet) {
			return Laser_GameObject(module, (AbstractBullet)obj2, obj1, point);
		}

		if (obj1 instanceof Missile) {
			return Missile_GameObject(module, (Missile)obj1, obj2, point);
		} else if (obj2 instanceof Missile) {
			return Missile_GameObject(module, (Missile)obj2, obj1, point);
		}

		if (obj1 instanceof Asteroid && obj2 instanceof Asteroid) {
			return Debris_Debris(module, (Asteroid)obj1, (Asteroid)obj2, point);
		}

		if (obj1 instanceof Cargo && obj2 instanceof Cargo) {
			return Cargo_Cargo(module, (Cargo)obj1, (Cargo)obj2, point);
		}

		if (obj1 instanceof Ship1 && obj2 instanceof Ship1) {
			Ship1 s1 = (Ship1)obj1;
			s1.bounceBack(true);
			Ship1 s2 = (Ship1)obj2;
			s2.bounceBack(true);
			return false;
		}

		if (obj1 instanceof AbstractPlanet) {
			Planet_Object(module, (AbstractPlanet) obj1, obj2, point);
			return false;
		} else if (obj2 instanceof AbstractPlanet) {
			Planet_Object(module, (AbstractPlanet) obj2, obj1, point);
			return false;
		}

		if (obj1 instanceof Ship1 && obj2 instanceof Cargo) {
			Ship_Cargo(module, (Ship1) obj1, (Cargo)obj2, point);
			return false;
		} else if (obj2 instanceof Ship1 && obj1 instanceof Cargo) {
			Ship_Cargo(module, (Ship1) obj2, (Cargo)obj1, point);
			return false;
		}

		if (obj1 instanceof Ship1 && obj2 instanceof SpaceStation) {
			SpaceStation_Ship(module, (SpaceStation) obj2, (Ship1)obj1, point);
			return false;
		} else if (obj2 instanceof Ship1 && obj1 instanceof SpaceStation) {
			SpaceStation_Ship(module, (SpaceStation) obj1, (Ship1)obj2, point);
			return false;
		}

		/*if (obj1 instanceof Ship1 && obj2 instanceof AbstractPlanet) {
			Planet_Ship(module, (AbstractPlanet) obj2, (Ship1)obj1, point);
			return false;
		} else if (obj2 instanceof Ship1 && obj1 instanceof AbstractPlanet) {
			Planet_Ship(module, (AbstractPlanet) obj1, (Ship1)obj2, point);
			return false;
		}*/

		if (obj1 instanceof Ship1 && obj2 instanceof MyTerrain) {
			MyTerrain_Ship(module, (MyTerrain) obj2, (Ship1)obj1, point);
			return false;
		} else if (obj2 instanceof Ship1 && obj1 instanceof MyTerrain) {
			MyTerrain_Ship(module, (MyTerrain) obj1, (Ship1)obj2, point);
			return false;
		}

		return GameObject_GameObject(module, obj2, obj1, point);
	}


	private static boolean Laser_GameObject(AbstractModule module, AbstractBullet laser, GameObject ship, Vector3f pos) {
		if (laser.shooter != ship) {
			ship.damage(laser.shooter, laser, laser.damage, pos);
			laser.removeFromGame(true, false);
		}
		return false;
	}


	private static boolean Missile_GameObject(AbstractModule module, Missile missile, GameObject ship, Vector3f pos) {
		if (missile.shooter != ship && ship instanceof Missile == false) {
			//module.log("Missile hit " + ship.name);
			ship.damage(missile.shooter, missile, missile.damage, pos);
			missile.removeFromGame(true, false);
		}
		return false;
	}


	private static boolean GameObject_GameObject(AbstractModule module, GameObject go1, GameObject go2, Vector3f pos) {
		//module.log(go1.name + " hit " + go2.name);
		go1.damage(go2, go2, 1f, pos);
		go2.damage(go1, go1, 1f, pos);
		return true;
	}


	private static boolean Debris_Debris(AbstractModule module, Asteroid go1, Asteroid go2, Vector3f pos) {
		//module.log(go1.name + " hit " + go2.name);
		go1.damage(go2, go2, 1f, pos);
		return false;
	}


	private static boolean Cargo_Cargo(AbstractModule module, Cargo go1, Cargo go2, Vector3f pos) {
		//module.log(go1.name + " hit " + go2.name);
		go1.reverseDir();
		go2.reverseDir();
		return true;
	}


	private static boolean Ship_Cargo(AbstractModule module, Ship1 ship, Cargo cargo, Vector3f pos) {
		//module.log(go1.name + " hit " + go2.name);
		ship.shipdata.adjustCargo(1);
		cargo.removeFromGame(false, false);
		return true;
	}


	private static boolean SpaceStation_Ship(AbstractModule module, SpaceStation ss, Ship1 ship, Vector3f pos) {
		ship.bounceBack(true);
		return true;
	}


/*	private static boolean Planet_Ship(AbstractModule module, AbstractPlanet ss, Ship1 ship, Vector3f pos) {
		ship.bounceBack(true);
		return true;
	}
*/
	private static boolean Planet_Object(AbstractModule module, AbstractPlanet ss, GameObject o, Vector3f pos) {
		module.log(ss.name + " hit " + o.name);
		
		// Move the object back until it doesn't hit the planet
		Vector3f diff = o.getNode().getWorldTranslation().subtract(ss.getNode().getWorldTranslation());
		diff.normalizeLocal();
		BoundingSphere planetsphere = (BoundingSphere)ss.getNode().getWorldBound();
		BoundingSphere shipsphere = (BoundingSphere)o.getNode().getWorldBound();
		float dist = planetsphere.distanceTo(shipsphere.getCenter());
		float dist_to_move = planetsphere.getRadius() + shipsphere.getRadius() - dist;
		diff.multLocal(dist_to_move);
		ss.getNode().getLocalTranslation().add(diff);
		
		if (o instanceof Ship1) {
			Ship1 ship = (Ship1)o;
			ship.bounceBack(true);
		}
		return true;
	}

	private static boolean MyTerrain_Ship(AbstractModule module, MyTerrain ss, Ship1 ship, Vector3f pos) {
		ship.bounceBack(false);
		return true;
	}


}
