package com.LyndonFawcett.MiniArchitect.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.LyndonFawcett.MiniArchitect.Paint;
import com.LyndonFawcett.MiniArchitect.Stroke;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Stroke(), config);
		config.height=800;
		config.width=1800;
	}
}