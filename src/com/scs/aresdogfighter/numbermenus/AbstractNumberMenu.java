package com.scs.aresdogfighter.numbermenus;

import com.scs.aresdogfighter.AresDogfighter;

public abstract class AbstractNumberMenu {
	
	protected AresDogfighter game;
	protected IMenuController menu_controller;

	public AbstractNumberMenu(AresDogfighter _game, IMenuController _menu_controller) {
		super();
		
		game = _game;
		menu_controller = _menu_controller;
	}
	
	public abstract String getTitle();
	
	public abstract String getOptions();
	
	public abstract void optionSelected(String i);

}
