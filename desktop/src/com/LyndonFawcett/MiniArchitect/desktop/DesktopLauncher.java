package com.LyndonFawcett.MiniArchitect.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.LyndonFawcett.MiniArchitect.Arena;
import com.LyndonFawcett.MiniArchitect.ApplicationWrapper;
import com.LyndonFawcett.MiniArchitect.screens.MenuScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new ApplicationWrapper(), config);
		
		config.height=800;
		config.width=1800;
	}
}