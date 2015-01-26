package com.LyndonFawcett.MiniArchitect.android;

import android.os.Bundle;

import com.LyndonFawcett.MiniArchitect.Start;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;




public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Start(), config);
		
	}
}
