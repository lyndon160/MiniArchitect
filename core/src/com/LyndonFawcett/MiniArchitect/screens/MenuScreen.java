package com.LyndonFawcett.MiniArchitect.screens;


import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.Timer;
import java.util.TimerTask;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.LyndonFawcett.MiniArchitect.Start;
import com.LyndonFawcett.MiniArchitect.UI.CardWindow;
import com.LyndonFawcett.MiniArchitect.UI.ChangeUsernameWindow;
import com.LyndonFawcett.MiniArchitect.tween.ActorAccessor;
import com.LyndonFawcett.MiniArchitect.utils.Updater;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;




public class MenuScreen implements Screen{

	private Stage stage;
	private Skin skin;
	private Table table;
	private TweenManager tweenManager;
	private Texture background;
	final private float FADETIME = 0.5f;
	private SpriteBatch batch;
	private Table tablecol;
	private Table tabletitle;
	private Timer autoModeTimer;
	static public Label userLabel; 
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(background, 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.end();
		stage.act(delta);
		stage.draw();
		tweenManager.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		//table.invalidateHierarchy();
	}

	@Override
	public void show() {
		
		stage = new Stage();
		new Updater();
		batch = new SpriteBatch(); 
		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("ChalkUi/uiskin.json"));

		table = new Table(skin);
		//table.setFillParent(true);
	//	table.debug();
		background = new Texture(Gdx.files.getFileHandle("blueprint.jpg", FileType.Internal), false);

		// creating heading
		Label heading = new Label("Mini Architect", skin, "48");
		//heading.setFontScale(2);

		// creating buttons
		TextButton buttonView = new TextButton("VIEW ROOMS", skin, "32");
		buttonView.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Timeline.createParallel().beginParallel()
				.push(Tween.to(tablecol, ActorAccessor.ALPHA, .75f).target(0))
				.push(Tween.to(tablecol, ActorAccessor.Y, .75f).target(table.getY() - 50)
						.setCallback(new TweenCallback() {

							@Override
							public void onEvent(int type, BaseTween<?> source) {
								((Game) Gdx.app.getApplicationListener()).setScreen(new ViewRoomsScreen());
							}
						}))
				.end().start(tweenManager);
			}
		});
		buttonView.pad(15);

		//Username label
		userLabel = new Label(Start.username,skin,"24");
		userLabel.setPosition(10, Gdx.graphics.getHeight()-userLabel.getHeight());
		stage.addActor(userLabel);
		
		ImageButton settingsBtn = new ImageButton(skin, "settings");
		settingsBtn.setBounds(Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/9,Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
		stage.addActor(settingsBtn);
		settingsBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addActor(new ChangeUsernameWindow(skin));
			
			}});
			
		


		FileHandle file = Gdx.files.local("downloaded/news/news.txt");
	
		
		//create news window
		CardWindow news = new CardWindow(skin);


		TextButton buttonCreate = new TextButton("CREATE NEW ROOM", skin, "32");
		buttonCreate.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Timeline.createParallel().beginParallel()
						.push(Tween.to(tablecol, ActorAccessor.ALPHA, .75f).target(0))
						.push(Tween.to(tablecol, ActorAccessor.Y, .75f).target(table.getY() - 50)
								.setCallback(new TweenCallback() {

									@Override
									public void onEvent(int type, BaseTween<?> source) {
										((Game) Gdx.app.getApplicationListener()).setScreen(new CreateOptionsScreen());
									}
								}))
						.end().start(tweenManager);
			}
		});
		buttonCreate.pad(15);


		// putting stuff together
	//	table.add(heading).spaceBottom(80).row();
		table.add(buttonView).space(15).width(Gdx.graphics.getWidth() / 2.3f).height(Gdx.graphics.getHeight() / 2.67f).row();
		//table.add(news).space(15).width(800).height(300).row();
		table.add(buttonCreate).space(15).width(Gdx.graphics.getWidth() / 2.3f).height(Gdx.graphics.getHeight() / 2.67f);
		//table.add(settings).space(15).width(800).height(300);
		
		//table.debug();
		
		tablecol = new Table(skin);
		//tablecol.setFillParent(true);
		tablecol.add(table);
		//tablecol.add(news).space(15).width(800).height(615);
		tablecol.add(news).space(15).width(Gdx.graphics.getWidth() / 2.3f).height(Gdx.graphics.getHeight() / 1.3f);
	
		
		
		tabletitle = new Table(skin);
		tabletitle.setFillParent(true);
		tabletitle.add(heading).space(50).row();
		tabletitle.add(tablecol).width(Gdx.graphics.getWidth() / 2.3f).height(Gdx.graphics.getHeight() / 1.3f);
		stage.addActor(tabletitle);

		// creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// heading color animation
		//Change this to be less distracting
		Timeline.createSequence().beginSequence()
				.push(Tween.to(heading, ActorAccessor.RGB, 1.5f).target(0, 0, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, 1.5f).target(0, 1, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, 1.5f).target(1, 0, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, 1.5f).target(1, 1, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, 1.5f).target(0, 1, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, 1.5f).target(1, 0, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, 1.5f).target(1, 1, 1))
				.end().repeat(Tween.INFINITY, 0).start(tweenManager);

		// heading and buttons fade-in
		Timeline.createSequence().beginSequence()
				.push(Tween.set(buttonView, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(news, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(buttonCreate, ActorAccessor.ALPHA).target(0))
				.push(Tween.from(heading, ActorAccessor.ALPHA, FADETIME).target(0))
				.push(Tween.to(buttonView, ActorAccessor.ALPHA, FADETIME).target(1))
				.push(Tween.to(news, ActorAccessor.ALPHA, FADETIME).target(1))
				.push(Tween.to(buttonCreate, ActorAccessor.ALPHA, FADETIME).target(1))
				.end().start(tweenManager);

		// table fade-in
		Tween.from(table, ActorAccessor.ALPHA, FADETIME).target(0).start(tweenManager);
		Tween.from(table, ActorAccessor.Y, FADETIME).target(Gdx.graphics.getHeight() / 8).start(tweenManager);

		tweenManager.update(Gdx.graphics.getDeltaTime());
		
		
		
		//Setup async for loading news to news window
		autoModeTimer = new Timer();
		//To cancel any task:
		autoModeTimer.purge();
		//To Start a Task:
		autoModeTimer.schedule(new TimerTask() {
		            @Override public void run() {

		            }}, 0);
		

		
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
}
