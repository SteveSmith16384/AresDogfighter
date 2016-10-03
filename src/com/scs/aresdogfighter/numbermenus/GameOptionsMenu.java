package com.scs.aresdogfighter.numbermenus;

import com.scs.aresdogfighter.AresDogfighter;
import com.scs.aresdogfighter.Statics;

public class GameOptionsMenu extends AbstractNumberMenu {

	private static final int SENS_INC = 5;
	
	public GameOptionsMenu(AresDogfighter game, IMenuController mc) {
		super(game, mc);
	}

	@Override
	public String getTitle() {
		return "Game Options";
	}

	@Override
	public String getOptions() {
		StringBuilder str = new StringBuilder();
		str.append("1 - Toggle Inverse Pitch (currently " + (Statics.INVERSE_PITCH?"on":"off") + ")\n");
		str.append("2 - Toggle Graphical Complexity (currently " + (Statics.SIMPLE_GRAPHICS?"simple":"complex") + ")\n");
		str.append("3 - Toggle Music During Game (currently " + (Statics.MUSIC_DURING_GAME?"on":"off") + ")\n");
		str.append("4-5 - Adjust mouse sensitivity (currently " + String.format("%d", (int)(Statics.MOUSE_SENSITIVITY*100)) + "%)\n");
		str.append("6-7 - Adjust Field of View angle (currently " + Statics.FOV_ANGLE + " degrees)\n");
		str.append("0 - Return");
		return str.toString();
	}

	@Override
	public void optionSelected(String i) {
		if (i.equals("1")) {
			Statics.INVERSE_PITCH = !Statics.INVERSE_PITCH;
		} else if (i.equals("2")) {
			Statics.SIMPLE_GRAPHICS = !Statics.SIMPLE_GRAPHICS;
		} else if (i.equals("3")) {
			Statics.MUSIC_DURING_GAME = !Statics.MUSIC_DURING_GAME;
		} else if (i.equals("4")) {
			int pcent = (int)(Statics.MOUSE_SENSITIVITY * 100f);
			pcent = pcent - SENS_INC;
			Statics.MOUSE_SENSITIVITY = (float)pcent / 100f;
		} else if (i.equals("5")) {
			int pcent = (int)(Statics.MOUSE_SENSITIVITY * 100f);
			pcent = pcent + SENS_INC;
			Statics.MOUSE_SENSITIVITY = (float)pcent / 100f;
		} else if (i.equals("6")) {
			Statics.FOV_ANGLE -= 5;
		} else if (i.equals("7")) {
			Statics.FOV_ANGLE += 5;
		} else if (i.equals("0")) {
			Statics.SaveProperties();
			this.menu_controller.showMenu(new SelectGameTypeMenu(game, this.menu_controller));
		}

	}

}
