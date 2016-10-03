package com.scs.aresdogfighter.numbermenus;
/*

import com.scs.aresdogfighter.AresDogfighter;
import com.scs.aresdogfighter.missions.EscortMission;

public class MissionsMenu extends AbstractNumberMenu {

	public MissionsMenu(AresDogfighter game, IMenuController mc) {
		super(game, mc);
	}

	@Override
	public String getTitle() {
		return "MISSIONS";
	}

	
	@Override
	public String getOptions() {
		StringBuilder str = new StringBuilder();
		str.append("Press the number in [] to select the options\n\n");
		str.append("[1] - Escort\n\n");
		str.append("[0] - Return\n\n");
		return str.toString();
	}

	
	@Override
	public void optionSelected(String i) {
		if (i.equals("1")) {
			this.game.game_data.current_mission = new EscortMission();
			this.returnTo();
		} else if (i.equals("0") || i.equalsIgnoreCase("Esc")) {
			this.returnTo();
		}
	}

	
	private void returnTo() {
		this.menu_controller.showMenu(new MissionsMenu(game, this.menu_controller));
	}
}
*/
