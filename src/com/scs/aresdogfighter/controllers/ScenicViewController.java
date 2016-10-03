package com.scs.aresdogfighter.controllers;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.IProcessable;

public class ScenicViewController extends CameraController implements IProcessable {
	
	private static final float VIEWING_TIME = 3;

	public List<GameObject> objects = new ArrayList<GameObject>();
	
	private float view_time_left = VIEWING_TIME;

	public ScenicViewController(Camera _cam) {
		super(_cam);
	}


	@Override
	public void process(float tpf) {
		if (target == null && objects.isEmpty() == false) {
			target = objects.remove(0);
			System.out.println("ScenicViewController: Showing " + target);
		}
		
		super.process(tpf);

		if (target != null) {
			float dist = this.cam.getLocation().distance(target.getNode().getWorldTranslation());
			if (dist < cam_view_dist) { // Looking at target
				this.view_time_left -= tpf;
				if (view_time_left <= 0) { // Pause to view target
					view_time_left = VIEWING_TIME;
					target = null;
				}
			}
		}
	}


	public void addObject(GameObject o) {
		this.objects.add(o);
	}
}
