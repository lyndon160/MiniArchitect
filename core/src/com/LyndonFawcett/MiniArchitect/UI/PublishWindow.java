package com.LyndonFawcett.MiniArchitect.UI;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.LyndonFawcett.MiniArchitect.Arena;
import com.LyndonFawcett.MiniArchitect.ArenaItem;
import com.LyndonFawcett.MiniArchitect.ApplicationWrapper;
import com.LyndonFawcett.MiniArchitect.Stroke;
import com.LyndonFawcett.MiniArchitect.utils.ImageSerializer;
import com.LyndonFawcett.MiniArchitect.utils.MinimalItem;
import com.LyndonFawcett.MiniArchitect.utils.Notification;
import com.LyndonFawcett.MiniArchitect.utils.ScreenshotFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
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
				handle.setVisible(false);
				Arena.pCam.position.set(new Vector3(0f, 60f, 0f));
				Arena.pCam.lookAt(new Vector3(0,0,0));
				return true;
		 	}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(Arena.instances.size<1 || roomName.getText().length() == 0)
					return;
				Json json = new Json();
				ArrayList<MinimalItem> temp = new ArrayList<MinimalItem>();
	
				for(ArenaItem i :Arena.instances)
					temp.add(new MinimalItem(i));
				//this might not work
				

				
				//push this result to mysql
				//input username, room name, json string jsoup
				
				//Get screenshot (piximap) and upload it.
				Pixmap screenShot = ScreenshotFactory.getScreenshot((Gdx.graphics.getWidth()/2)-35,(Gdx.graphics.getHeight()/2)-35,80,60,false);
				//Texture pixmaptex = new Texture( screenShot );
				ByteBuffer pixels = screenShot.getPixels();


				byte[] bytes = new byte[pixels.remaining()];
				pixels.get(bytes, 0, bytes.length);
				//System.out.println();
				//new Pixmap(bytes,0,bytes.length);
			//	System.out.println("pixmap :" + json.toJson(screenShot, Pixmap.class, Pixmap.class));
				//json.fromJson(Pixmap.class, json.toJson(screenShot,Pixmap.class));
				//Uncomment to test screenshot
				//Stroke.sketch.add(new Sprite(pixmaptex));
				
				Pixmap p= new Pixmap(80,60,Format.RGBA8888);

				//p.getPixels().clear();
				p.getPixels().put(bytes, 0, bytes.length);
				p.getPixels().rewind();
				//Sprite s =new Sprite(new Texture(p));
				//s.rotate(180);
				//s.flip(false, true);
				//s.setPosition(-500,-100);
				//Stroke.sketch.add(s);
				System.out.println(bytes.length);
			//	System.out.println(s.getWidth());
				//HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
				//httpGet.setUrl("http://lyndonfawcett.me/publishRoom.php?username="+Start.username+"&name="+roomName.getText()+"&room="+json.toJson(temp));
				//Post data to db 
				Connection con = Jsoup.connect("http://lyndonfawcett.me/publishRoom.php")
						  .data("username", ApplicationWrapper.username)
						  .data("name", roomName.getText().replaceAll("!", "").replaceAll(";", "").replaceAll("#", "").replaceAll("@", "").replaceAll(",", "").replaceAll(" ", "_"))
						  .data("room", json.toJson(temp, ArrayList.class))
						  .data("image", json.toJson(new ImageSerializer(bytes), ImageSerializer.class));
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
