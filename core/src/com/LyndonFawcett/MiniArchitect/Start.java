package com.LyndonFawcett.MiniArchitect;

import com.LyndonFawcett.MiniArchitect.screens.MenuScreen;
import com.badlogic.gdx.Game;

public class Start extends Game{

	@Override
	public void create() {
		setScreen(new MenuScreen());
	}

}
