package com.scs.aresdogfighter.gameobjects;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.modules.ManualShipControlModule;

public class PulseLaserShot extends AbstractBullet {

	private static final float SIZE = .4f;

	private Node node;

	public PulseLaserShot(ManualShipControlModule game, AssetManager assetManager, Ship1 _shooter, float _range, float _damage, Vector3f optional_target) {
		super(game, assetManager, "PulseLaserShot", _shooter, _range, _damage, optional_target);

		Geometry pulse = new Geometry("Pulse Billboard", new Quad(SIZE, SIZE));
		pulse.setModelBound(new BoundingSphere());
		pulse.updateModelBound();
		node.attachChild(pulse);

		Texture t = assetManager.loadTexture("Textures/laser_bolt.png");
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		mat.setTexture("DiffuseMap", t);
		pulse.setMaterial(mat);

		pulse.setQueueBucket(Bucket.Transparent);

		Vector3f origin = shooter.getGunNode().getWorldTranslation().clone();
		//origin.addLocal(dir.mult(2f)); // Prevent it starting inside the shooter
		node.setLocalTranslation(origin);

		try {
			if (!Statics.MUTE) {
				laser_audio = new AudioNode(assetManager, "Sound/space laser.wav", false);
				laser_audio.setPositional(false);
				//Node n = (Node)this.getSpatial();
				node.attachChild(laser_audio);
				laser_audio.play();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	@Override
	public void process(float tpf) {
		super.process(tpf);
		node.lookAt(module.getCam().getLocation(), Vector3f.UNIT_Y);
	}


	@Override
	public Node getNode() {
		return node;
	}


}
