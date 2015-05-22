package com.LyndonFawcett.MiniArchitect.screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.LyndonFawcett.MiniArchitect.ApplicationWrapper;
import com.LyndonFawcett.MiniArchitect.Arena;
import com.LyndonFawcett.MiniArchitect.ArenaItem;
import com.LyndonFawcett.MiniArchitect.Stroke;
import com.LyndonFawcett.MiniArchitect.utils.ImageSerializer;
import com.LyndonFawcett.MiniArchitect.utils.MinimalItem;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
/**
 * 
 * Screen that shows different online rooms. Connects to database to gather rooms and filters them by top, new, owners
 * 
 * @author Lyndon
 *
 */
public class ViewRoomsScreen implements Screen{
	private Stage stage;
	private Skin skin;
	private Table table;
	private Texture background;
	private SpriteBatch batch;
	
	ArrayList<MinimalItem> minItems;
	final Array<ArenaItem> arenaItems = new Array<ArenaItem>();
	final InputMultiplexer arenaMulti = new InputMultiplexer();
	
	ArrayList<Disposable> assets;
	Document doc;
	Json j;
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(background, 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.end();
		stage.act(delta);
		stage.draw();


		if(Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE)){
			((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
		}
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {

		stage = new Stage();
		batch = new SpriteBatch(); 
		Gdx.input.setInputProcessor(stage);
		assets = new ArrayList<Disposable>();
		skin = new Skin(Gdx.files.internal("ChalkUi/uiskin.json"));

		j = new Json();
		background = new Texture(Gdx.files.getFileHandle("blueprint.jpg", FileType.Internal), false);
		assets.add(background);
		
		
		table = new Table(skin);
		//Table needs one column left one column right

		Table filterWindow = new Table(skin);
		final TextButton myRoomsBtn = new TextButton("MY ROOMS",skin,"toggle24");
		final TextButton topRoomsBtn = new TextButton("TOP ROOMS",skin,"toggle24");
		final TextButton newRoomsBtn = new TextButton("DISCOVER",skin,"toggle24");



		ScrollPane rightPane = new ScrollPane(null,skin,"transparent");

		final Table rightPaneContent = new Table(skin);

		rightPaneContent.add(new Label("Nothing here! Create some rooms",skin,"24"));
		rightPaneContent.pack();	
		rightPane.setWidget(rightPaneContent);


		//query for content created my this user
		myRoomsBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				// need http protocol
				try {
					doc = Jsoup.connect("http://lyndonfawcett.me/getMyRooms.php?id="+ApplicationWrapper.username).get();

					topRoomsBtn.setChecked(false);
					newRoomsBtn.setChecked(false);
					// get all links
					Element jbody = doc.body();

					//j.fromJson(ArrayList.class, jbody.text().replace(",{}", "");
					if(jbody.text().contentEquals("No room"))
						return;
					String parse =jbody.text().replace(",{}", "");
					//System.out.println(parse);
					JsonValue root = new JsonReader().parse(parse);
					
					Iterator<JsonValue> i = root.child().iterator();
					rightPaneContent.clear();
					while(i.hasNext()){
						JsonValue obj = i.next();
						final int id = obj.child().asInt();

						String owner = obj.child().next().asString();

						String roomName = obj.child().next().next().asString();

						String roomJson = obj.child().next().next().next().toString();
						//Generate arena items from minimal items
						String imageJson = obj.child().next().next().next().next().toString();
						
						//convert image to sprite
						ImageSerializer imageBytes = j.fromJson(ImageSerializer.class, imageJson);
						
						Pixmap p= new Pixmap(80,60,Format.RGBA8888);

						p.getPixels().put(imageBytes.bytes, 0, imageBytes.bytes.length);
						p.getPixels().rewind();
						
						final ArrayList<MinimalItem> minItems =j.fromJson(ArrayList.class, roomJson);
						if(Stroke.multiplexer == null)
							Stroke.multiplexer = new InputMultiplexer();
						if(Arena.instances == null){
							Arena.instances  = new Array<ArenaItem>();
							Arena.wireFrames =new ArrayList<ModelInstance>();
						}

						
				
						Window windowWrapper = new Window("",skin,"chalk");
						Table container = new Table(skin);
						Texture t = new Texture(p);
						Image image = new Image(t);
						
						assets.add(t);
						
						p.dispose();
						image.scaleBy(3);
						image.debug();
						Label l = new Label(owner + ": " +roomName.replaceAll("_", " "),skin,"32");
						container.add(l).row().row().row().row();
						container.add(image).padRight(300).padTop(200);

						container.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								for(MinimalItem mini : minItems)
									if(!mini.modelName.contains("wall"))
										arenaItems.add(mini.convertToArenaItem(arenaMulti));
								
								Arena.instances=arenaItems;
								Arena.multiplexer=arenaMulti;
							//	System.out.println(Arena.instances.size);
								((Game) Gdx.app.getApplicationListener()).setScreen(new Arena());
							}
						});
						windowWrapper.add(container);
						rightPaneContent.add(windowWrapper).padBottom(20).height(Gdx.graphics.getWidth()/5).width(Gdx.graphics.getWidth()/2);
						
						//Vote button
						final ImageButton like = new ImageButton(skin,"upvote");
						like.addListener(new ClickListener() {
							Document doc;
							@Override
							public void clicked(InputEvent event, float x, float y) {
								//upvote room
								try {
									doc = Jsoup.connect("http://lyndonfawcett.me/voteOnRoom.php?id="+ id).get();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								like.setDisabled(true);
							}
						});
						rightPaneContent.add(like).height(Gdx.graphics.getHeight()/10).width(Gdx.graphics.getWidth()/10).row();

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		//get top content and fill layout
		topRoomsBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				myRoomsBtn.setChecked(false);
				newRoomsBtn.setChecked(false);
				// need http protocol
				try {
					doc = Jsoup.connect("http://lyndonfawcett.me/getTopRooms.php").get();

				//	System.out.println("Top selected");

					// get all links
					Element jbody = doc.body();


					if(jbody.text().contentEquals("No room"))
						return;
					
					String parse =jbody.text().replace(",{}", "");
					//System.out.println(parse);
					JsonValue root = new JsonReader().parse(parse);
					Iterator<JsonValue> i = root.child().iterator();
					rightPaneContent.clear();
					//Iterate over rooms
					while(i.hasNext()){
						JsonValue obj = i.next();
						final int id = obj.child().asInt();

						String owner = obj.child().next().asString();

						String roomName = obj.child().next().next().asString();

						String roomJson = obj.child().next().next().next().toString();
						
						String imageJson = obj.child().next().next().next().next().toString();
						
						//convert image to sprite
						ImageSerializer imageBytes = j.fromJson(ImageSerializer.class, imageJson);
						
						Pixmap p= new Pixmap(80,60,Format.RGBA8888);

						p.getPixels().put(imageBytes.bytes, 0, imageBytes.bytes.length);
						p.getPixels().rewind();
					//	Sprite s =new Sprite(new Texture(p));
					//	s.flip(false, true);
						
						//Generate arena items from minimal items						
						final ArrayList<MinimalItem> minItems =j.fromJson(ArrayList.class, roomJson);

						if(Stroke.multiplexer == null)
							Stroke.multiplexer = new InputMultiplexer();
						if(Arena.instances == null){
							Arena.instances  = new Array<ArenaItem>();
							Arena.wireFrames =new ArrayList<ModelInstance>();
						}

						Window windowWrapper = new Window("",skin,"chalk");
						Table container = new Table(skin);
						Texture t = new Texture(p);
						Image image = new Image(t);
						assets.add(t);
						p.dispose();
						image.scaleBy(3);
						image.debug();
						Label l = new Label(owner + ": " +roomName.replaceAll("_", " "),skin,"32");
						container.add(l).row().row().row().row();
						container.add(image).padRight(300).padTop(200);

						container.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								
								for(MinimalItem mini : minItems)
									if(!mini.modelName.contains("wall"))
										arenaItems.add(mini.convertToArenaItem(arenaMulti));
								minItems.clear();
								Arena.instances=arenaItems;
								Arena.multiplexer=arenaMulti;
							//	System.out.println(Arena.instances.size);
								((Game) Gdx.app.getApplicationListener()).setScreen(new Arena());
							}
						});
						windowWrapper.add(container);
						rightPaneContent.add(windowWrapper).padBottom(20).height(Gdx.graphics.getWidth()/5).width(Gdx.graphics.getWidth()/2);
					
						//Vote button
						final ImageButton like = new ImageButton(skin,"upvote");
						like.addListener(new ClickListener() {
							Document doc;
							@Override
							public void clicked(InputEvent event, float x, float y) {
								//upvote room
								try {
									doc = Jsoup.connect("http://lyndonfawcett.me/voteOnRoom.php?id="+ id).get();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								like.setDisabled(true);
							}
						});
						rightPaneContent.add(like).height(Gdx.graphics.getHeight()/10).width(Gdx.graphics.getWidth()/10).row();

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		//Discover rooms button
		newRoomsBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				topRoomsBtn.setChecked(false);
				myRoomsBtn.setChecked(false);
				// need http protocol
				try {
					doc = Jsoup.connect("http://lyndonfawcett.me/getTopRooms.php").get();

					//System.out.println("Top selected");

					// get all links
					Element jbody = doc.body();

					//j.fromJson(ArrayList.class, jbody.text().replace(",{}", "");
					if(jbody.text().contentEquals("No room"))
						return;
					JsonValue root = new JsonReader().parse(jbody.text().replace(",{}", ""));
					//System.out.println();
					
					if(Stroke.multiplexer == null)
						Stroke.multiplexer = new InputMultiplexer();
					if(Arena.instances == null){
						Arena.instances  = new Array<ArenaItem>();
						Arena.wireFrames =new ArrayList<ModelInstance>();
					}
					
					Iterator<JsonValue> i = root.child().iterator();
					rightPaneContent.clear();
					while(i.hasNext()){
						JsonValue obj = i.next();
						final int id = obj.child().asInt();

						String owner = obj.child().next().asString();

						String roomName = obj.child().next().next().asString();

						String roomJson = obj.child().next().next().next().toString();
						//Generate arena items from minimal items
						String imageJson = obj.child().next().next().next().next().toString();
						
						//convert image to sprite
						ImageSerializer imageBytes = j.fromJson(ImageSerializer.class, imageJson);
						
						Pixmap p= new Pixmap(80,60,Format.RGBA8888);

						p.getPixels().put(imageBytes.bytes, 0, imageBytes.bytes.length);
						p.getPixels().rewind();
						
						
						final ArrayList<MinimalItem> minItems =j.fromJson(ArrayList.class, roomJson);
						if(Stroke.multiplexer == null)
							Stroke.multiplexer = new InputMultiplexer();
						if(Arena.instances == null){
							Arena.instances  = new Array<ArenaItem>();
							Arena.wireFrames =new ArrayList<ModelInstance>();
						}
						String price ="£0";
						
						
						
						/* Clear other arenaItems
						 * 
						 * 
						 */
						

					//	System.out.println(price);


				


						Window windowWrapper = new Window("",skin,"chalk");
						Table container = new Table(skin);
						Texture t = new Texture(p);
						Image image = new Image(t);
						assets.add(t);
						p.dispose();
						image.scaleBy(3);
						image.debug();
						Label l = new Label(owner + ": " +roomName.replaceAll("_", " "),skin,"32");
						container.add(l).row().row().row().row();
						container.add(image).padRight(300).padTop(200);

						container.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								for(MinimalItem mini : minItems)
									if(!mini.modelName.contains("wall"))
										arenaItems.add(mini.convertToArenaItem(arenaMulti));
								minItems.clear();
								Arena.instances=arenaItems;
								Arena.multiplexer=arenaMulti;
							//	System.out.println(Arena.instances.size);
								((Game) Gdx.app.getApplicationListener()).setScreen(new Arena());
							}
						});
						windowWrapper.add(container);
						rightPaneContent.add(windowWrapper).padBottom(20).height(Gdx.graphics.getWidth()/5).width(Gdx.graphics.getWidth()/2);
						
						//Vote button
						final ImageButton like = new ImageButton(skin,"upvote");
						like.addListener(new ClickListener() {
							Document doc;
							@Override
							public void clicked(InputEvent event, float x, float y) {
								//upvote room
								try {
									doc = Jsoup.connect("http://lyndonfawcett.me/voteOnRoom.php?id="+ id).get();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								like.setDisabled(true);
							}
						});
						rightPaneContent.add(like).height(Gdx.graphics.getHeight()/10).width(Gdx.graphics.getWidth()/10).row();

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		
		
		filterWindow.add(myRoomsBtn).height(Gdx.graphics.getHeight() / 5f).width(Gdx.graphics.getWidth() / 5f).pad(10, 0, 10, 0).row();
		filterWindow.add(topRoomsBtn).height(Gdx.graphics.getHeight() / 5f).width(Gdx.graphics.getWidth() / 5f).pad(10, 0, 10, 0).row();
		filterWindow.add(newRoomsBtn).height(Gdx.graphics.getHeight() / 5f).width(Gdx.graphics.getWidth() / 5f).pad(10, 0, 10, 0);
		filterWindow.pack();
		table.add(filterWindow).width(Gdx.graphics.getWidth() / 5f).height(Gdx.graphics.getHeight());




		table.add(rightPane).width((Gdx.graphics.getWidth() / 5f)*4).height(Gdx.graphics.getHeight());
		table.setPosition(0, 0);
		//table.debug();
		table.setFillParent(true);
		stage.addActor(table);

		
		
		//insert loading dialog screen here
		
		
		
		
		//load my rooms by default
		
		final Document doc;

		// need http protocol
		try {
			myRoomsBtn.setChecked(true);
			doc = Jsoup.connect("http://lyndonfawcett.me/getMyRooms.php?id="+ApplicationWrapper.username).get();

			//System.out.println("Mine selected");
			topRoomsBtn.setChecked(false);
			newRoomsBtn.setChecked(false);
			// get all links
			Element jbody = doc.body();

			//j.fromJson(ArrayList.class, jbody.text().replace(",{}", "");
			if(jbody.text().contentEquals("No room"))
				return;
			String parse =jbody.text().replace(",{}", "");
			//System.out.println(parse);
			JsonValue root = new JsonReader().parse(parse);
			
			Iterator<JsonValue> i = root.child().iterator();
			rightPaneContent.clear();
			while(i.hasNext()){
				JsonValue obj = i.next();
				final int id = obj.child().asInt();

				String owner = obj.child().next().asString();

				String roomName = obj.child().next().next().asString();

				String roomJson = obj.child().next().next().next().toString();
				//Generate arena items from minimal items
				String imageJson = obj.child().next().next().next().next().toString();
				
				//convert image to sprite
				ImageSerializer imageBytes = j.fromJson(ImageSerializer.class, imageJson);
				
				Pixmap p= new Pixmap(80,60,Format.RGBA8888);

				p.getPixels().put(imageBytes.bytes, 0, imageBytes.bytes.length);
				p.getPixels().rewind();
				
				final ArrayList<MinimalItem> minItems =j.fromJson(ArrayList.class, roomJson);

				if(Stroke.multiplexer == null)
					Stroke.multiplexer = new InputMultiplexer();
				if(Arena.instances == null){
					Arena.instances  = new Array<ArenaItem>();
					Arena.wireFrames =new ArrayList<ModelInstance>();
				}

		
				Window windowWrapper = new Window("",skin,"chalk");
				Table container = new Table(skin);
				Texture t = new Texture(p);
				Image image = new Image(t);
				assets.add(t);
				p.dispose();
				if(Gdx.graphics.getWidth()<1700)
					image.scaleBy(2);
				else
					image.scaleBy(3);
				image.debug();
				Label l = new Label(owner + ": " +roomName.replaceAll("_", " "),skin,"32");
				if(Gdx.graphics.getWidth()<1700)
					container.add(l).padBottom(Gdx.graphics.getHeight()/17).row();
				else
					container.add(l).padBottom(Gdx.graphics.getHeight()/7).row();
				container.add(image).padRight(Gdx.graphics.getWidth()/7).padTop((Gdx.graphics.getHeight()/10));

				container.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {

						for(MinimalItem mini : minItems)
							if(!mini.modelName.contains("wall"))
								arenaItems.add(mini.convertToArenaItem(arenaMulti));
						minItems.clear();				
						Arena.instances=arenaItems;
						Arena.multiplexer=arenaMulti;
					//	System.out.println(Arena.instances.size);
						((Game) Gdx.app.getApplicationListener()).setScreen(new Arena());
					}
				});
				windowWrapper.add(container);
				rightPaneContent.add(windowWrapper).padBottom(20).height(Gdx.graphics.getWidth()/5).width(Gdx.graphics.getWidth()/2);
				
				//Vote button
				final ImageButton like = new ImageButton(skin,"upvote");
				like.addListener(new ClickListener() {
					Document doc;
					@Override
					public void clicked(InputEvent event, float x, float y) {
						//upvote room
						try {
							doc = Jsoup.connect("http://lyndonfawcett.me/voteOnRoom.php?id="+ id).get();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						like.setDisabled(true);
					}
				});
				rightPaneContent.add(like).height(Gdx.graphics.getHeight()/10).width(Gdx.graphics.getWidth()/10).row();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		background.dispose();
		if(minItems !=null)
			minItems.clear();
		batch.dispose();
		for(Disposable d : assets)
			d.dispose();
	}

}
