package com.LyndonFawcett.MiniArchitect;

import com.LyndonFawcett.MiniArchitect.screens.MenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Start extends Game{
	public static String username;//load from shared pref
	@Override
	public void create() {
		username="anonymous";
		Gdx.input.setCatchBackKey(true);
		setScreen(new MenuScreen());
	}

}
