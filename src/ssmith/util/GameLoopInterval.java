package ssmith.util;

public class GameLoopInterval {
	
	private float curr_time, total_time;

	public GameLoopInterval(float f) {
		super();
		
		this.curr_time = f;
		this.total_time = f;
		
	}
	
	public boolean hasHit(float tpf) {
		curr_time -= tpf;
		boolean hit = curr_time < 0;
		if (hit) {
			this.restart();
		}
		return hit;
	}
	
	
	public void restart() {
		curr_time = total_time;
	}
	
	
	public void fireNow() {
		this.curr_time = -1;
	}

}
