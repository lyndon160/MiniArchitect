package com.LyndonFawcett.MiniArchitect.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.LyndonFawcett.MiniArchitect.Arena;
import com.LyndonFawcett.MiniArchitect.tween.ActorAccessor;
import com.LyndonFawcett.MiniArchitect.utils.Updater;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CreateOptionsScreen implements Screen{

	private Stage stage;
	private Skin skin;
	private Table table;
	private TweenManager tweenManager;
	final private float FADETIME = 0.5f;
	private SpriteBatch batch;
	private Texture background;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		background = new Texture(Gdx.files.getFileHandle("blueprint.jpg", FileType.Internal), false);

		stage = new Stage();
		new Updater();
		batch = new SpriteBatch();
		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("ChalkUi/uiskin.json"));

		table = new Table(skin);
		table.setFillParent(true);
	//	table.debug();


		// creating heading
		Label heading = new Label("Room creation", skin, "48");
		heading.setFontScale(2);

		// creating buttons
		TextButton buttonView = new TextButton("Create from scratch", skin, "32");
		buttonView.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addAction(sequence(moveTo(0, -stage.getHeight(), .5f), run(new Runnable() {

					@Override
					public void run() {
					 ((Game) Gdx.app.getApplicationListener()).setScreen(new Arena());
					}
				})));
			}
		});
		buttonView.pad(15);

		
		
		
		
		TextButton settings = new TextButton("Predefine your room", skin, "32");
		settings.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addAction(sequence(moveTo(0, -stage.getHeight(), .5f), run(new Runnable() {

					@Override
					public void run() {
						//((Game) Gdx.app.getApplicationListener()).setScreen(new Settings());
					}
				})));
			}
		});
		settings.pad(15);

	
		

		TextButton news = new TextButton("Create a bedroom", skin, "32");
		news.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addAction(sequence(moveTo(0, -stage.getHeight(), .5f), run(new Runnable() {

					@Override
					public void run() {
						//((Game) Gdx.app.getApplicationListener()).setScreen(new Settings());
					}
				})));
			}
		});
		news.pad(15);


		TextButton buttonCreate = new TextButton("Create a kitchen", skin, "32");
		buttonCreate.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Timeline.createParallel().beginParallel()
						.push(Tween.to(table, ActorAccessor.ALPHA, .75f).target(0))
						.push(Tween.to(table, ActorAccessor.Y, .75f).target(table.getY() - 50)
								.setCallback(new TweenCallback() {

									@Override
									public void onEvent(int type, BaseTween<?> source) {
										//((Game) Gdx.app.getApplicationListener()).setScreen(new Arena());
									}
								}))
						.end().start(tweenManager);
			}
		});
		buttonCreate.pad(15);


		// putting stuff together
		table.add(heading).spaceBottom(80).row();
		table.add(buttonView).space(15).width(800).height(300);
		table.add(settings).space(15).width(800).height(300).row();
		table.add(buttonCreate).space(15).width(800).height(300);
		table.add(news).space(15).width(800).height(300);
		

		stage.addActor(table);

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
				.push(Tween.set(settings, ActorAccessor.ALPHA).target(0))
				.push(Tween.from(heading, ActorAccessor.ALPHA, FADETIME).target(0))
				.push(Tween.to(buttonView, ActorAccessor.ALPHA, FADETIME).target(1))
				.push(Tween.to(news, ActorAccessor.ALPHA, FADETIME).target(1))
				.push(Tween.to(buttonCreate, ActorAccessor.ALPHA, FADETIME).target(1))
				.push(Tween.to(settings, ActorAccessor.ALPHA, FADETIME).target(1))
				.end().start(tweenManager);

		// table fade-in
		Tween.from(table, ActorAccessor.ALPHA, 1.75f).target(0).start(tweenManager);
		Tween.from(table, ActorAccessor.Y, 1.75f).target(Gdx.graphics.getHeight() / 8).start(tweenManager);

		tweenManager.update(Gdx.graphics.getDeltaTime());
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

		
}