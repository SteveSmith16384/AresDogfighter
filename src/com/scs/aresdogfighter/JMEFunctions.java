package com.scs.aresdogfighter;

import java.io.File;
import java.io.IOException;

import jme3tools.optimize.LodGenerator;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LodControl;
import com.jme3.scene.debug.Grid;

public class JMEFunctions {

	public static void main(String args[]) {
		TestCollisionPoint();
	}


	private JMEFunctions() {
	}


	public static Spatial LoadModel(AssetManager assetManager, String path) {
		boolean LOAD_JME_VERSION = true;

		Spatial ship = null;
		String j30_path = path.substring(path.lastIndexOf("/")+1) + ".j3o";
		try {
			if (LOAD_JME_VERSION) {
				String filename = "Models/" + j30_path;
				System.out.println("Loading " + filename);
				//if (new File("assets/" + filename).exists() || new File("" + filename).exists()) { // todo - better
				ship = assetManager.loadModel(filename);
				//}
			}
		} catch (AssetNotFoundException | IllegalArgumentException ex) {
			// Do nothing
		}
		if (ship == null) {
			System.err.println("WARNING!! Loading original model! " + path);
			ship = assetManager.loadModel(path);
			if (Statics.USE_LOD) {
				AddLODs((Node)ship);
			}
			File file = new File("assets/Models/" + j30_path);
			BinaryExporter exporter = BinaryExporter.getInstance();
			try {
				exporter.save(ship, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ship;
	}


	private static void AddLODs(Node n) {
		long start = System.currentTimeMillis();
		for (Spatial s : n.getChildren()) {
			if (s instanceof Geometry) {
				Geometry g = (Geometry)s;
				try {
					LodGenerator lod = new LodGenerator(g);
					lod.bakeLods(LodGenerator.TriangleReductionMethod.COLLAPSE_COST, 0.5f);
					Statics.Debug("Generated LODs for " + n.getName());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		long took = System.currentTimeMillis() - start;
		Statics.Debug("Finished generating LODs for " + n.getName() + ".  Took " + (took/1000) + " secs");
	}


	public static Geometry GetGrid(AssetManager assetManager, int size){
		Geometry g = new Geometry("wireframe grid", new Grid(size, size, 1f) );
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setWireframe(true);
		mat.setColor("Color", ColorRGBA.White);
		g.setMaterial(mat);
		//g.center().move(pos);
		//rootNode.attachChild(g);
		g.move(-size/2, 0, -size/2);
		return g;
	}


	public static void TurnTowards_Gentle(Spatial spatial, Vector3f target, float pcent) {
		if (pcent <= 0) {
			return; //throw new RuntimeException("Invalid pcent: " + pcent);
		}
		if (pcent > 1) {
			pcent = 1;
		}
		Vector3f dir_to_target = target.subtract(spatial.getWorldTranslation()).normalizeLocal();
		Quaternion target_q = new Quaternion();
		target_q.lookAt(dir_to_target, Vector3f.UNIT_Y);
		Quaternion our_q = spatial.getWorldRotation();
		Quaternion new_q = new Quaternion();
		if (target_q.dot(our_q) > 0.99f) {
			// Just look at it
			new_q = target_q;
		} else {
			new_q.slerp(our_q, target_q, pcent);
		}
		spatial.setLocalRotation(new_q);
	}


	public static void TurnAwayFrom(Spatial spatial, Vector3f target, float pcent) {
		if (pcent <= 0) {
			return; //throw new RuntimeException("Invalid pcent: " + pcent);
		}
		if (pcent > 1) {
			pcent = 1;
		}
		Quaternion our_q = spatial.getWorldRotation();
		Vector3f dir_to_target = spatial.getWorldTranslation().subtract(target).normalizeLocal();
		Quaternion target_q = new Quaternion();
		target_q.lookAt(dir_to_target, Vector3f.UNIT_Y);
		Quaternion new_q = new Quaternion();
		new_q.slerp(our_q, target_q, pcent);
		spatial.setLocalRotation(new_q);

	}


	public static void MoveForwards(Spatial spatial, float speed) {
		Vector3f forward = spatial.getLocalRotation().mult(Vector3f.UNIT_Z);
		Vector3f offset = forward.mult(speed);
		spatial.move(offset);
	}


	public static BitmapText CreateBitmapText(BitmapFont guiFont, String text) {
		BitmapText bmp = new BitmapText(guiFont, false);
		bmp.setSize(guiFont.getCharSet().getRenderedSize());
		bmp.setColor(ColorRGBA.White);
		bmp.setText(text);
		return bmp;

	}


	public static float GetAngleBetween_OLD(Spatial spatial, Vector3f target) {
		Vector3f dir_to_target = target.subtract(spatial.getWorldTranslation()).normalizeLocal();
		Quaternion target_q = new Quaternion();
		target_q.lookAt(dir_to_target, Vector3f.UNIT_Y);

		Quaternion our_q = spatial.getWorldRotation();
		return our_q.dot(target_q) ;
	}


	public static float GetAngleBetween(Spatial spatial, Vector3f target) {
		Vector3f dir_to_target = target.subtract(spatial.getWorldTranslation()).normalizeLocal();
		Vector3f forward = spatial.getLocalRotation().mult(Vector3f.UNIT_Z).normalizeLocal();
		float diff = forward.distance(dir_to_target);
		return diff;
	}


	/*
	 * Taken from http://danikgames.com/blog/moving-target-intercept-in-3d/
	 */
	/*private Vector3f FindInterceptVector(Vector3f shotOrigin, float shotSpeed,
			Vector3f targetOrigin, Vector3f targetVel) {

		Vector3f dirToTarget = targetOrigin.subtract(shotOrigin).normalizeLocal();

		// Decompose the target's velocity into the part parallel to the
		// direction to the cannon and the part tangential to it.
		// The part towards the cannon is found by projecting the target's
		// velocity on dirToTarget using a dot product.
		Vector3f targetVelOrth = targetVel.dot(dirToTarget) * dirToTarget;

		// The tangential part is then found by subtracting the
		// result from the target velocity.
		Vector3f targetVelTang = targetVel - targetVelOrth;

		/*
	 * targetVelOrth
	 * |
	 * |
	 *
	 * ^...7  <-targetVel
	 * |  /.
	 * | / .
	 * |/ .
	 * t--->  <-targetVelTang
	 *
	 *
	 * s--->  <-shotVelTang
	 *
	 */

	// The tangential component of the velocities should be the same
	// (or there is no chance to hit)
	// THIS IS THE MAIN INSIGHT!
	/*Vector3f shotVelTang = targetVelTang;

		// Now all we have to find is the orthogonal velocity of the shot

		float shotVelSpeed = shotVelTang.magnitude;
		if (shotVelSpeed > shotSpeed) {
			// Shot is too slow to intercept target, it will never catch up.
			// Do our best by aiming in the direction of the targets velocity.
			return targetVel.normalized * shotSpeed;
		} else {
			// We know the shot speed, and the tangential velocity.
			// Using pythagoras we can find the orthogonal velocity.
			float shotSpeedOrth =
					Mathf.Sqrt(shotSpeed * shotSpeed - shotVelSpeed * shotVelSpeed);
			Vector3f shotVelOrth = dirToTarget * shotSpeedOrth;

			// Finally, add the tangential and orthogonal velocities.
			//return shotVelOrth + shotVelTang;

			// Find the time of collision (distance / relative velocity)
			float timeToCollision = ((shotOrigin - targetOrigin).magnitude - shotRadius - targetRadius)
					/ (shotVelOrth.magnitude-targetVelOrth.magnitude);

			// Calculate where the shot will be at the time of collision
			Vector3f shotVel = shotVelOrth + shotVelTang;
			Vector3f shotCollisionPoint = shotOrigin + shotVel * timeToCollision;
		}
	}*/


	/*
	 * Taken from http://stackoverflow.com/questions/4749951/shoot-projectile-straight-trajectory-at-moving-target-in-3-dimensions
	 */
	public static Vector3f GetCollisionPoint(Vector3f target_pos, Vector3f target_movement, Vector3f shooter_pos, float bulletSpeed) {
		float x_t0 = target_pos.x;
		float y_t0 = target_pos.y;
		float z_t0 = target_pos.z;
		float x_b0 = shooter_pos.x;
		float y_b0 = shooter_pos.y;
		float z_b0 = shooter_pos.z;

		// setup all needed variables
		float c_x = x_t0 - x_b0;
		float c_y = y_t0 - y_b0;
		float c_z = z_t0 - z_b0;
		float v_bulletSpeed = bulletSpeed;
		float v_x = target_movement.x;
		float v_y = target_movement.y;
		float v_z = target_movement.z;

		double a = Math.pow(v_x, 2) + Math.pow(v_y,2) + Math.pow(v_z,2) - Math.pow(v_bulletSpeed,2);
		double b = 2*(v_x*c_x+v_y*c_y+v_z*c_z);
		double c = Math.pow(c_x,2)+Math.pow(c_y,2)+Math.pow(c_z,2);

		if (Math.pow(b,2) < 4*a*c) {
			// no real solutions
			return null;
		}

		double p = -b/(2*a);
		double q = Math.sqrt(Math.pow(b,2) - 4*a*c)/(2*a);

		double t1 = p-q;
		double t2 = p+q;

		if (t1 < 0 && t2 < 0) {
			// no positive solutions, all possible trajectories are in the past
			return null;
		}

		// we want to hit it at the earliest possible time
		float t = 0;
		if (t1 > t2) {
			t = (float)t1;
		} else {
			t = (float)t2;
		}

		// calculate point of collision
		float x = x_t0 + t * v_x;
		float y = y_t0 + t * v_y;
		float z = z_t0 + t * v_z;

		return new Vector3f(x, y, z);
	}


	public static void TestCollisionPoint() {
		Vector3f target_pos = new Vector3f(10, 0, 10);
		Vector3f target_speed = new Vector3f(-1, 0, -.1f);
		Vector3f shooter_pos = new Vector3f(0, 0, 0);
		Vector3f col = GetCollisionPoint(target_pos, target_speed, shooter_pos, 5);
		System.out.println("col=" + col);
	}
	

}
