package com.LyndonFawcett.MiniArchitect;

import com.LyndonFawcett.MiniArchitect.screens.MenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ApplicationWrapper extends Game{
	public static String username;//load from shared pref
	@Override
	public void create() {
		Preferences prefs = Gdx.app.getPreferences("login");//check if preferences already exists
		
        if(prefs.getString("username").equals(""))
    		username="anonymous";
        else
        	username = prefs.getString("username");
        
        System.out.println(username);
		Gdx.input.setCatchBackKey(true);
		setScreen(new MenuScreen());
	}

}
