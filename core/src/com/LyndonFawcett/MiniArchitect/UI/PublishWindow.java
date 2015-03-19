package com.LyndonFawcett.MiniArchitect.UI;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.LyndonFawcett.MiniArchitect.Arena;
import com.LyndonFawcett.MiniArchitect.ArenaItem;
import com.LyndonFawcett.MiniArchitect.ApplicationWrapper;
import com.LyndonFawcett.MiniArchitect.utils.MinimalItem;
import com.LyndonFawcett.MiniArchitect.utils.Notification;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Json;
public class PublishWindow extends Window{

	TextField roomName;
	TextButton publish, cancel;
	
	public PublishWindow(final Skin skin) {
		super("Publish", skin, "default");
		this.setPosition((Gdx.graphics.getWidth()/2)-this.getWidth(), (Gdx.graphics.getHeight()/2));
		this.add(new Label("Enter room description",skin,"24")).width(Gdx.graphics.getWidth()/2).row();
		roomName = new TextField("",skin);
		publish = new TextButton("PUBLISH",skin,"good");
		cancel = new TextButton("CANCEL",skin,"bad");
		Table buttonTable = new Table(skin);
		buttonTable.add(cancel).padRight(Gdx.graphics.getWidth()/3);
		buttonTable.add(publish);

		final PublishWindow handle = this;
		
		publish.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
		 	}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(Arena.instances.size<1 || roomName.getText().length() == 0)
					return;
				Json json = new Json();
				ArrayList<MinimalItem> temp = new ArrayList<MinimalItem>();
	
				for(ArenaItem i :Arena.instances)
					temp.add(new MinimalItem(i));

				//push this result to mysql
				//input username, room name, json string jsoup
				
				
				//HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
				//httpGet.setUrl("http://lyndonfawcett.me/publishRoom.php?username="+Start.username+"&name="+roomName.getText()+"&room="+json.toJson(temp));
				//Post data to db 
				Connection con = Jsoup.connect("http://lyndonfawcett.me/publishRoom.php")
						  .data("username", ApplicationWrapper.username)
						  .data("name", roomName.getText().replaceAll("!", "").replaceAll(";", "").replaceAll("#", "").replaceAll("@", "").replaceAll(",", "").replaceAll(" ", "_"))
						  .data("room", json.toJson(temp, ArrayList.class));
				try {
					con.post();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println(json.toJson(temp));
				/*Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
					public void handleHttpResponse(HttpResponse httpResponse) {

							if(httpResponse.getResultAsString().contains("success")){
								System.out.println("Published");
							}
						




					}

					public void failed(Throwable t) {
						System.out.println("failed to publish");
						//do stuff here based on the failed attempt
					}

					@Override
					public void cancelled() {
						// TODO Auto-generated method stub

					}
				});
				
				*/
				
				
				
				
				new Notification(getStage(),skin,new Label("ROOM PUBLISHED",skin,"24"));
				
				
				handle.setVisible(false);
			}
		});
		
		
		cancel.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
		 	}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				//remove window
				handle.setVisible(false);
			}
		});
		
		this.add(roomName).width(Gdx.graphics.getWidth()/2).padBottom(20).row();
		this.add(buttonTable);
		this.pack();
	}

}
