package com.LyndonFawcett.MiniArchitect.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.LyndonFawcett.MiniArchitect.Arena;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Arena(), config);
		config.height=800;
		config.width=1800;
	}
}