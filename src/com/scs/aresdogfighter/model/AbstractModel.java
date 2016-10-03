package com.scs.aresdogfighter.model;

import jme3tools.optimize.LodGenerator;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LodControl;
import com.scs.aresdogfighter.Statics;

public abstract class AbstractModel extends Node {

	/*protected int id;
	public static AtomicInteger next_id = new AtomicInteger(1);
	public static HashMap<Integer, GameObject> objmap = new HashMap<Integer, GameObject>();
	 */
	public AbstractModel(String name) {
		super(name);

		/*if (obj != null) {
			id = next_id.addAndGet(1);
			obj.id = id;
			objmap.put(id, obj);
		}*/

	}


	protected void generateLODs() {
		if (Statics.USE_LOD) {
			long start = System.currentTimeMillis();
			Statics.Debug("Generating LODs for " + this.name + "...");
			Node n = (Node)this.getChild(0) ;
			for (Spatial s : n.getChildren()) {
				if (s instanceof Geometry) {
					Geometry g = (Geometry)s;
					try {
						//LodGenerator lod = new LodGenerator(g);
						//lod.bakeLods(LodGenerator.TriangleReductionMethod.COLLAPSE_COST, 0.5f);
						LodControl lc = new LodControl();
						g.addControl(lc);
					} catch (Exception ex) {
						Statics.Debug("Error generating LODs for " + this.name + "/" + g.getName());
					}
				}
			}
			long took = System.currentTimeMillis() - start;
			//Statics.Debug("Finished generating LODs for " + this.name + ".  Took " + (took/1000) + " secs");

		}
	}

}
