package com.LyndonFawcett.MiniArchitect.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;


public class Notification extends Dialog{
	Notification handle;
	public Notification(Stage stage,Skin skin, TextButton button){
		super("",skin,"chalk");
		this.add(button);
		handle = this;
		this.show(stage).setPosition((Gdx.graphics.getWidth()/2)-this.getWidth(), Gdx.graphics.getHeight()-this.getHeight());
		this.setModal(false);

		new Timer().scheduleTask(new Task(){ @Override
			public void run() {
			handle.hide();
		}
		}, 4);
	}
	
	public Notification(Stage stage,Skin skin, Label button){
		super("",skin,"chalk");
		this.add(button);
		handle = this;
		this.show(stage).setPosition((Gdx.graphics.getWidth()/2)-this.getWidth(), Gdx.graphics.getHeight()-this.getHeight());
		this.setModal(false);

		new Timer().scheduleTask(new Task(){ @Override
			public void run() {
			handle.hide();
		}
		}, 4);
	}
}
