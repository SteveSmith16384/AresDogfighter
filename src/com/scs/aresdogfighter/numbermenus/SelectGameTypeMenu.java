package com.scs.aresdogfighter.numbermenus;

import com.scs.aresdogfighter.AresDogfighter;
import com.scs.aresdogfighter.Statics;
import com.scs.aresdogfighter.data.SectorData;

public class SelectGameTypeMenu extends AbstractNumberMenu {

	public SelectGameTypeMenu(AresDogfighter game, IMenuController mc) {
		super(game, mc);
	}


	@Override
	public String getTitle() {
		return "Select Game Options";
	}


	@Override
	public String getOptions() {
		checkWingmen();

		StringBuilder str = new StringBuilder();
		str.append("Press the number in [] to select the options\n\n");
		str.append("GAME TYPE: " + Statics.GetGameTypeAsString() + "\n");
		str.append("[1] - Toggle game type\n\n");

		if (Statics.GAME_TYPE == Statics.GT_DOGFIGHT) {
			str.append("NUMBER OF ENEMY SHIPS: " + Statics.NUM_ENEMIES + " (max: " + Statics.MAX_ENEMIES + ")\n");
			str.append("[2] - Less enemies, [3] - More enemies\n");
			str.append("(Destroy all enemy ships to increase the maximum)\n\n");

			/*str.append("ENEMY DIFFICULTY (1-3): " + Statics.ENEMY_DIFFICULTY + "\n");
		str.append("[3] - Easier, [4] - Harder\n\n");*/

			str.append("NUMBER OF WINGMEN: " + Statics.NUM_WINGMEN + " (max: " + (Statics.MAX_ENEMIES-1) + ")\n");
			str.append("[4] - Less wingmen, [5] - More wingmen\n");
			str.append("(Must be less than the number of enemy ships)\n\n");
		}

		str.append("[6] - GAME OPTIONS\n\n");
		str.append("[7] - TOGGLE MUTE (currently " + (Statics.MUTE?"sound off":"sound on") + ")\n\n");
		str.append("[0] - START GAME");
		return str.toString();
	}


	@Override
	public void optionSelected(String i) {
		if (i.equals("1")) {
			Statics.GAME_TYPE++;
			if (Statics.GAME_TYPE > 1) {
				Statics.GAME_TYPE = 0;
			}
			checkWingmen();
		} else if (i.equals("2")) {
			Statics.NUM_ENEMIES--;
			if (Statics.NUM_ENEMIES < 0) {
				Statics.NUM_ENEMIES = 0;
			}
			checkWingmen();
		} else if (i.equals("3")) {
			Statics.NUM_ENEMIES++;
			if (Statics.NUM_ENEMIES > Statics.MAX_ENEMIES) {
				Statics.NUM_ENEMIES = Statics.MAX_ENEMIES;
			}
			/*} else if (i.equals("3")) {
			Statics.ENEMY_DIFFICULTY--;
			if (Statics.ENEMY_DIFFICULTY < 1) {
				Statics.ENEMY_DIFFICULTY = 1;
			}
		} else if (i.equals("4")) {
			Statics.ENEMY_DIFFICULTY++;
			if (Statics.ENEMY_DIFFICULTY > 3) {
				Statics.ENEMY_DIFFICULTY = 3;
			}*/
		} else if (i.equals("4")) {
			Statics.NUM_WINGMEN--;
			if (Statics.NUM_WINGMEN < 0) {
				Statics.NUM_WINGMEN = 0;
			}
		} else if (i.equals("5")) {
			Statics.NUM_WINGMEN++;
			checkWingmen();
		} else if (i.equals("6")) {
			this.menu_controller.showMenu(new GameOptionsMenu(game, this.menu_controller));
		} else if (i.equals("7")) {
			Statics.MUTE = !Statics.MUTE;
			game.curr_module.muteChanged();
		} else if (i.equals("0")) {
			this.startGame();
		} else if (i.equals("Esc")) {
			this.game.stop();
		}
	}


	private void checkWingmen() {
		if (Statics.NUM_WINGMEN >= Statics.NUM_ENEMIES-1) {
			Statics.NUM_WINGMEN = Statics.NUM_ENEMIES-1;
		}
		if (Statics.NUM_WINGMEN < 0) {
			Statics.NUM_WINGMEN = 0;
		}
	}


	private void startGame() {
		SectorData sector = this.game.game_data.getCurrentSector();
		/*sector.num_nibbly_fighters = 0;
		sector.num_police = 0;
		sector.num_quaz_fighters = 0;
		sector.num_others = 0;

		if (Statics.GAME_TYPE == Statics.GT_DOGFIGHT) {
			sector.num_quaz_fighters = Statics.NUM_ENEMIES;
			sector.num_nibbly_fighters = Statics.NUM_WINGMEN;
			//game.game_data.mission_text = "Battle against " + Statics.NUM_ENEMIES + " enemies with " + Statics.NUM_WINGMEN + " wingmen";
		}*/
		game.game_data.current_sector.generateKrakatoaEntities();//.generateDefaultSectorEntities();
		game.selectModule(AresDogfighter.Module.MANUAL_SHIP_FLYING, false);
	}

}
