package com.scs.aresdogfighter.numbermenus;

import com.scs.aresdogfighter.AresDogfighter;
import com.scs.aresdogfighter.Statics;

public class SpaceStationMenu extends AbstractNumberMenu {

	public SpaceStationMenu(AresDogfighter game, IMenuController mc) {
		super(game, mc);
	}


	@Override
	public String getTitle() {
		return "Welcome to " + this.game.game_data.getCurrentSector().name + ".  Please select an option";
	}


	@Override
	public String getOptions() {
		StringBuilder str = new StringBuilder();
		str.append("Press the number in [] to choose an option\n\n");
		str.append("[1] - Missions\n\n");
		str.append("[0] - Re-Launch\n\n");
		return str.toString();
	}


	@Override
	public void optionSelected(String i) {
		if (i.equals("1")) {
			//this.menu_controller.showMenu(new MissionsMenu(game, this.menu_controller));
		} else if (i.equals("0") || i.equalsIgnoreCase("Esc")) {
			game.selectModule(AresDogfighter.Module.MANUAL_SHIP_FLYING, false);
		}
	}


}
