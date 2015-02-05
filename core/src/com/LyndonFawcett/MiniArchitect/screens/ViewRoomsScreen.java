package com.LyndonFawcett.MiniArchitect.screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.LyndonFawcett.MiniArchitect.Arena;
import com.LyndonFawcett.MiniArchitect.ArenaItem;
import com.LyndonFawcett.MiniArchitect.Stroke;
import com.LyndonFawcett.MiniArchitect.utils.MinimalItem;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class ViewRoomsScreen implements Screen{
	private Stage stage;
	private Skin skin;
	private Table table;
	private Texture background;
	private SpriteBatch batch;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(background, 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.end();
		stage.act(delta);
		stage.draw();


		if(Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.BACKSPACE)){
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

		skin = new Skin(Gdx.files.internal("ChalkUi/uiskin.json"));


		background = new Texture(Gdx.files.getFileHandle("blueprint.jpg", FileType.Internal), false);

		table = new Table(skin);
		//Table needs one column left one column right

		Table filterWindow = new Table(skin);
		TextButton myRoomsBtn = new TextButton("MY ROOMS",skin,"24");
		TextButton topRoomsBtn = new TextButton("TOP ROOMS",skin,"24");
		TextButton newRoomsBtn = new TextButton("NEW ROOMS",skin,"24");



		ScrollPane rightPane = new ScrollPane(null,skin,"transparent");

		final Table rightPaneContent = new Table(skin);

		rightPaneContent.add(new Label("filler",skin,"24"));
		rightPaneContent.pack();	
		rightPane.setWidget(rightPaneContent);


		//query for content created my this user
		myRoomsBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Document doc;

				// need http protocol
				try {
					doc = Jsoup.connect("http://lyndonfawcett.me/getTopRooms.php").get();

					System.out.println("Top selected");

					// get all links
					Element jbody = doc.body();
					Json j = new Json();
					//j.fromJson(ArrayList.class, jbody.text().replace(",{}", "");
					if(jbody.text().contentEquals("No room"))
						return;
					JsonValue root = new JsonReader().parse(jbody.text().replace(",{}", ""));
					System.out.println();
					Iterator<JsonValue> i = root.child().iterator();
					rightPaneContent.clear();
					while(i.hasNext()){
						JsonValue obj = i.next();
						final int id = obj.child().asInt();

						String owner = obj.child().next().asString();

						String roomName = obj.child().next().next().asString();

						String roomJson = obj.child().next().next().next().toString();
						//Generate arena items from minimal items

						ArrayList<MinimalItem> minItems =j.fromJson(ArrayList.class, roomJson);
						final Array<ArenaItem> arenaItems = new Array<ArenaItem>();
						if(Stroke.multiplexer == null)
							Stroke.multiplexer = new InputMultiplexer();
						if(Arena.instances == null)
							Arena.instances  = new Array<ArenaItem>();
						for(MinimalItem mini : minItems)
							if(!mini.modelName.contains("wall"))
								arenaItems.add(mini.convertToArenaItem(Stroke.multiplexer));
						//Arena.instances = arenaItems.;


						Label l = new Label(owner + ": " +roomName.replaceAll("_", " "),skin,"32");
						l.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								Arena.instances=arenaItems;
								System.out.println(Arena.instances.size);
								((Game) Gdx.app.getApplicationListener()).setScreen(new Arena());
							}
						});
						rightPaneContent.add(l);

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		//get top content and fill layout
		topRoomsBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Document doc;

				// need http protocol
				try {
					doc = Jsoup.connect("http://lyndonfawcett.me/getTopRooms.php").get();

					System.out.println("Top selected");

					// get all links
					Element jbody = doc.body();
					Json j = new Json();
					//j.fromJson(ArrayList.class, jbody.text().replace(",{}", "");
					if(jbody.text().contentEquals("No room"))
						return;
					JsonValue root = new JsonReader().parse(jbody.text().replace(",{}", ""));
					System.out.println();
					Iterator<JsonValue> i = root.child().iterator();
					rightPaneContent.clear();
					while(i.hasNext()){
						JsonValue obj = i.next();
						final int id = obj.child().asInt();

						String owner = obj.child().next().asString();

						String roomName = obj.child().next().next().asString();

						String roomJson = obj.child().next().next().next().toString();
						//Generate arena items from minimal items

						ArrayList<MinimalItem> minItems =j.fromJson(ArrayList.class, roomJson);
						final Array<ArenaItem> arenaItems = new Array<ArenaItem>();
						if(Stroke.multiplexer == null)
							Stroke.multiplexer = new InputMultiplexer();
						if(Arena.instances == null)
							Arena.instances  = new Array<ArenaItem>();
						for(MinimalItem mini : minItems)
							if(!mini.modelName.contains("wall"))
								arenaItems.add(mini.convertToArenaItem(Stroke.multiplexer));



						TextButton l = new TextButton(owner + ": " +roomName.replaceAll("_", " "),skin,"32");
						l.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								Arena.instances=arenaItems;
								System.out.println(Arena.instances.size);
								((Game) Gdx.app.getApplicationListener()).setScreen(new Arena());
							}
						});
						rightPaneContent.add(l).padBottom(20).height(Gdx.graphics.getWidth()/5).width(Gdx.graphics.getWidth()/2);
						
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
						rightPaneContent.add(like).height(50).width(50).row();

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		//get content by date and layout
		newRoomsBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

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
	}

}
