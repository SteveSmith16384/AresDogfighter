package com.scs.aresdogfighter.controllers;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.Statics;

public class CameraController implements IProcessable {

	private static final float CAM_TURN_SPEED = .1f;
	private static final float CAM_MOVE_SPEED = 20f;

	protected Camera cam;
	protected GameObject target;
	private boolean locked = false;
	public float cam_view_dist = Statics.START_CAM_VIEW_DIST;

	public CameraController(Camera _cam) {
		super();

		cam = _cam;
		cam.setLocation(new Vector3f(200, 200, 200));
	}


	public GameObject getTarget() {
		return target;
	}


	public void setTarget(GameObject _target) {
		this.target = _target;
		locked = false;
	}


	@Override
	public void process(float tpf) {
		// Check location
		if (target != null) {
			float dist = this.cam.getLocation().distance(target.getNode().getWorldTranslation());
			if (dist > cam_view_dist) {
				Vector3f offset = target.getNode().getWorldTranslation().subtract(cam.getLocation());
				offset.normalizeLocal();
				//offset.multLocal((dist-cam_view_dist));
				offset.multLocal(tpf * CAM_MOVE_SPEED);
				cam.setLocation(cam.getLocation().add(offset));
			} else if (dist < cam_view_dist) {
				Vector3f offset = target.getNode().getWorldTranslation().subtract(cam.getLocation());
				offset.normalizeLocal();
				offset.multLocal((dist-cam_view_dist)/5);//tpf * CAM_MOVE_SPEED);
				cam.setLocation(cam.getLocation().add(offset));
			}

			// Check angle
			Vector3f dir_to_target = target.getNode().getWorldTranslation().subtract(cam.getLocation()).normalizeLocal();
			float ang = cam.getDirection().angleBetween(dir_to_target) * FastMath.RAD_TO_DEG;
			//System.out.println("Angle: " + ang);
			if (ang < 1 || locked) {
				cam.lookAt(target.getNode().getWorldTranslation(), Vector3f.UNIT_Y);
				locked = true;
			} else {
				//Vector3f start = cam.getDirection().normalize();
				//Vector3f end = target.subtract(cam.getLocation()).normalizeLocal();
				Vector3f newdir = FastMath.interpolateLinear(CAM_TURN_SPEED, cam.getDirection(), dir_to_target);
				cam.lookAt(cam.getLocation().add(newdir), Vector3f.UNIT_Y);
			}
			cam.update();
		}
	}

}
