package com.LyndonFawcett.MiniArchitect;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Paint implements ApplicationListener, InputProcessor, GestureListener{

	SpriteBatch batch;
	ArrayList<Sprite> sketch;
	ArrayList<Vector2> drawingData;
	Sprite pixel;
	Pixmap colour, colourDebug;
	Texture background;
	private OrthographicCamera cam;
	public float zoom = 1.0f;
	int SPRITECOUNT=400;
	int pointer=0;
	private Stage stage;
	Button paintBtn,grabBtn;
	Skin skin;
	Label fpsLabel;
	Boolean grab=false,paint=false;
	@Override
	public void create() {
		batch = new SpriteBatch();
		background = new Texture(Gdx.files.getFileHandle("blueprint.jpg", FileType.Internal), false);

		cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.setProjectionMatrix(cam.combined);
		//	Gdx.input.setInputProcessor(this);
		//	Gdx.input.setInputProcessor(new GestureDetector(this));
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		fpsLabel = new Label("fps:", skin);
		stage = new Stage();
		fpsLabel.setPosition(0, Gdx.graphics.getHeight()-fpsLabel.getHeight());

		Window window = new Window("Tools",skin);
		window.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//window.setFillParent(true);
		stage.addActor(window);
		paintBtn=new Button(skin);
		paintBtn.add("Paint");
		paintBtn.setColor(1, 0, 0, 1);
		paintBtn.setPosition(300, 100); //** Button location **//
		paintBtn.setHeight(100); //** Button Height **//
		paintBtn.setWidth(100); //** Button Width **//
		paintBtn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				paint=true;
				grab=false;
			}
		});


		grabBtn=new Button(skin);
		grabBtn.add("Grab");
		grabBtn.setColor(0, 0, 1, 1);
		grabBtn.setPosition(100, 100); //** Button location **//
		grabBtn.setHeight(100); //** Button Height **//
		grabBtn.setWidth(100); //** Button Width **//
		grabBtn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				grab=true;
				paint=false;
			}
		});
		//window.add(paintBtn);
	//	window.row();
		//window.add(grabBtn);
		//window.row();
//		window.add(fpsLabel);
		
		stage.addActor(grabBtn);
		stage.addActor(paintBtn);
		stage.addActor(fpsLabel);
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(new GestureDetector(this));
		multiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);




		sketch= new ArrayList<Sprite>();
		drawingData= new ArrayList<Vector2>();

		colour =new Pixmap(1,1,Format.RGBA8888);
		colourDebug =new Pixmap(1,1,Format.RGBA8888);

		colourDebug.setColor(Color.RED);
		colourDebug.fillCircle(0,0, 10);
		colour.setColor(Color.CYAN);
		colour.fill();

		/*	for(int i=0; i<SPRITECOUNT;i++)
			sketch.add(new Sprite(new Texture(colour), 10,10));
		 */
		pixel=new Sprite(new Texture(colour), 4,4);
		cam.update();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);


		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.draw(background, -Gdx.graphics.getWidth()/2,-Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth(),Gdx.graphics.getHeight() );
		for(Sprite pixel:sketch){
			pixel.draw(batch);
		}
		batch.end();

		stage.act();
		stage.draw();

		cam.update();

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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		create();
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		lastPos = new Vector2(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	Vector2 lastPos;
	Vector3 tp;
	@Override
	public boolean touchDragged(int screenX, int screenY, int p) {
		if(paint){
			int x=screenX;
			int y=screenY;
			int diffX=screenX-(int)lastPos.x;
			int diffY=screenY-(int)lastPos.y;
			double angle = Math.atan2((double)diffX,(double)diffY)*180/Math.PI; //In degrees
			Gdx.app.log("diffX", diffX+"");
			Gdx.app.log("diffY", diffY+"");
			int a = diffX;
			int b = diffY;
			double c = (a*a)+(b*b);
			if(a != 0 && b!=0)
				c=  Math.sqrt(c);
			else if(a==0&&b!=0)
				c=b;
			else if(a!=0&&b==0)
				c=a;
			else
				c=0;

			Gdx.app.log("Length", c+"");
			Gdx.app.log("Angle", angle+"");
			float steps=0;
			pixel=new Sprite(new Texture(colour), 4,4);
			Sprite debug=new Sprite(new Texture(colour), 10, (int) Math.ceil(c));
			tp = new Vector3(screenX, screenY,0);

			cam.unproject(tp);
			debug.setPosition(tp.x, tp.y);
			debug.setOrigin(debug.getWidth()/2, 0);

			debug.rotate((float) angle);
			debug.flip(true, false);
			sketch.add(debug);

			Sprite debug2=new Sprite(new Texture(colourDebug), 10, 10);
			tp = new Vector3(screenX, screenY,0);
			cam.unproject(tp);
			debug2.setPosition(tp.x, tp.y);
			//	sketch.add(debug2);

			addToDrawing(lastPos, new Vector2(screenX, screenY));
			lastPos.set(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if(grab){
			cam.zoom +=(float)amount/10;
			Gdx.app.log("scroll",cam.zoom+"");
			if(cam.zoom>1)
				cam.zoom=1;
			if(cam.zoom<=0.1)
				cam.zoom= 0.1F;
		}
		return false;
	}
	public float initialScale = 1.0f;
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		initialScale = zoom;
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {

		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {

		//IF PAINT BRUSH NOT SELECTED DO THIS
		if(grab){
			cam.translate(-deltaX,deltaY);
			cam.update();
		}
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		if(grab){
			//Calculate pinch to zoom
			float ratio = initialDistance / distance;

			//Clamp range and set zoom
			cam.zoom = MathUtils.clamp(initialScale * ratio, 0.1f, 1.0f);
			cam.update();
			Gdx.app.log("Zoom", distance+"");
		}
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	public void addToDrawing(Vector2 from, Vector2 to){
		float diffX=from.x-to.x;
		float diffY=from.y-to.y;
		float step = diffX/diffY;
		Gdx.app.log("NEW POINT DATA",from.toString()+"::"+ to.toString());
		if(from.x<to.x){
			for(float x=from.x; x<to.x;x++){
				
				drawingData.add(new Vector2(x,from.y+=step));
				Gdx.app.log("Draw Point",x+": "+from.y+"");
			}
		}
		else{
			for(float x=from.x; x>to.x;x--){
				drawingData.add(new Vector2(x,from.y+=step));
				Gdx.app.log("Draw Point",x+": "+from.y+"");
			}
		}
	}

}