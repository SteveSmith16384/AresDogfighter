package com.scs.aresdogfighter.hud;

import java.util.Iterator;
import java.util.List;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;
import com.scs.aresdogfighter.GameObject;
import com.scs.aresdogfighter.IProcessable;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class Scanner extends Node implements IProcessable{

	private static final float MAX_RANGE = 50f;

	private ManualShipControlModule module;

	public Scanner(ManualShipControlModule _module) {
		super("Scanner");

		module = _module;

	}


	@Override
	public void process(float tpf) {
		//this.setLocalRotation(module.getCam().getRotation());
		//this.rotate(this.module.players_ship.getSpatial().getLocalRotation().getRotationColumn(1));

		//new Quaternion().fromAngleAxis(-1 * tpf * TURN_SPEED, Vector3f.UNIT_X)); //this.module.players_ship.getSpatial().getLocalRotation());
		this.setLocalTranslation(module.getCam().getLocation().add(module.getCam().getDirection().mult(10f)));

		Material linemat = new Material(module.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		linemat.setColor("Color", new ColorRGBA(1, 1, 1, 0.5f)); // 0.5f is the alpha value
		linemat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

		List<IProcessable> list = module.getGameObjects();
		Iterator<IProcessable> it = list.iterator();
		this.detachAllChildren();
		while (it.hasNext()) {
			GameObject o = (GameObject)it.next();
			if (o != this.module.players_avatar) {
				Vector3f pos = o.getNode().getWorldTranslation().clone();
				pos.subtractLocal(module.players_avatar.getNode().getWorldTranslation());
				pos.divideLocal(MAX_RANGE);


				Line linegeom = new Line(new Vector3f(pos.x, pos.y, pos.z), new Vector3f(pos.x, pos.y+1, pos.z));
				linegeom.setLineWidth(2);
				Geometry line = new Geometry("line", linegeom);
				line.setMaterial(linemat);
				line.setCullHint(CullHint.Never);
				this.attachChild(line);


				// Centre point
				Line linegeom2 = new Line(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
				linegeom2.setLineWidth(2);
				Geometry line2 = new Geometry("line", linegeom2);
				line2.setMaterial(linemat);
				line2.setCullHint(CullHint.Never);
				this.attachChild(line2);

			}
		}


	}



}
