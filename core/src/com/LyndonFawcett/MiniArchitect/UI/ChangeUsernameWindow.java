package com.LyndonFawcett.MiniArchitect.UI;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.LyndonFawcett.MiniArchitect.Start;
import com.LyndonFawcett.MiniArchitect.screens.MenuScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ChangeUsernameWindow extends Window{
	/**
	 * Popup window that has prompt for password and username to create a new account
	 * @param skin
	 */
	public ChangeUsernameWindow(Skin skin) {
		super("Create/login", skin);
		final TextField name = new TextField("",skin);
		final TextField password = new TextField("",skin);
		password.setPasswordMode(true);
		password.setPasswordCharacter('*');
		TextButton submit = new TextButton("SUBMIT",skin,"good");
		TextButton cancel = new TextButton("CANCEL",skin,"bad");
		
		this.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		this.setWidth(Gdx.graphics.getWidth()/5);
		final Preferences prefs = Gdx.app.getPreferences("login");
		this.add((new Label("USERNAME :",skin,"24")));
		this.add(name).row();
		this.add((new Label("PASSWORD :",skin,"24")));
		this.add(password).row();
	
		this.add(cancel);
		this.add(submit);
		final ChangeUsernameWindow handle = this;
		submit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				//Check if password and username are right or account doesnt exist yet
				
				//check if field is filled
				if(name.getText().replaceAll(" ", "").isEmpty()){
					return;
				}
				
				try {
					Document	doc = Jsoup.connect("http://lyndonfawcett.me/checkUserAccount.php?username="+name.getText()+"&password="+password.getText()).get();
					if(doc.body().text().contains("success!")){
						//correct account, close dialogue and change username throughout
						prefs.putString("username", name.getText());
						Start.username=name.getText();
						MenuScreen.userLabel.setText(Start.username);
						//If account is legitimate then add username to app
						Gdx.app.getPreferences("login").putString("username", name.getText());
						handle.setVisible(false);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
		});
		

		cancel.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				handle.setVisible(false);
			}
		});
	}

}
