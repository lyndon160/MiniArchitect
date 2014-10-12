package com.LyndonFawcett.MiniArchitect;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

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
import com.badlogic.gdx.utils.viewport.Viewport;

import dollarN.NBestList;
import dollarN.NDollarRecognizer;
import dollarN.PointR;
import dollarN.Utils;

public class Stroke implements ApplicationListener, InputProcessor, GestureListener{

	private static final float BRUSHSIZE = 10;
	SpriteBatch batch;
	ArrayList<Sprite> sketch;
	
	public static ArrayList<Item> furnishings;
	
	public static InputMultiplexer multiplexer;
	
	ArrayList<Vector2> drawingData;
	Vector<PointR> points = new Vector<PointR>();
	Vector<Vector<PointR>> strokes = new Vector<Vector<PointR>>();
	Sprite pixel;
	Pixmap colour, colourDebug;
	Texture background;
	public static OrthographicCamera cam;
	public float zoom = 1.0f;
	int SPRITECOUNT=400;
	int pointer=0;
	private Stage stage;
	Button paintBtn,grabBtn;
	Texture table,chair,light,tv,sofa;
	Skin skin;
	Label fpsLabel;
	Label resultLabel;
	NDollarRecognizer _rec = null;
	Boolean grab=false,paint=false;
	Texture tableTexture;
	
	@Override
	public void create() {
		_rec = new NDollarRecognizer();
		//Load images
		table = new Texture(Gdx.files.internal("table.png"));
		chair = new Texture(Gdx.files.getFileHandle("chair.png", FileType.Internal), false);
		light = new Texture(Gdx.files.getFileHandle("light.png", FileType.Internal), false);
		tv = new Texture(Gdx.files.getFileHandle("TV.png", FileType.Internal), false);
		sofa = new Texture(Gdx.files.getFileHandle("sofa.png", FileType.Internal), false);
		
		//Load gestures
		_rec.LoadGesture(Gdx.files.getFileHandle("wall.xml", FileType.Internal).read());
		_rec.LoadGesture(Gdx.files.getFileHandle("light.xml", FileType.Internal).read());
		_rec.LoadGesture(Gdx.files.getFileHandle("table.xml", FileType.Internal).read());
		_rec.LoadGesture(Gdx.files.getFileHandle("table2.xml", FileType.Internal).read());
		_rec.LoadGesture(Gdx.files.getFileHandle("table3.xml", FileType.Internal).read());
		_rec.LoadGesture(Gdx.files.getFileHandle("table4.xml", FileType.Internal).read());
		_rec.LoadGesture(Gdx.files.getFileHandle("tv.xml", FileType.Internal).read());
		_rec.LoadGesture(Gdx.files.getFileHandle("chair2.xml", FileType.Internal).read());
		_rec.LoadGesture(Gdx.files.getFileHandle("wall.xml", FileType.Internal).read());
		_rec.LoadGesture(Gdx.files.getFileHandle("wall2.xml", FileType.Internal).read());
		_rec.LoadGesture(Gdx.files.getFileHandle("chair.xml", FileType.Internal).read());
		_rec.LoadGesture(Gdx.files.getFileHandle("circle.xml", FileType.Internal).read());
		
		
		batch = new SpriteBatch();
		background = new Texture(Gdx.files.getFileHandle("blueprint.jpg", FileType.Internal), false);

		//tableTexture = new Texture(Gdx.files.internal("Table.png"));
		
		//setup ui
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		//viewport = new FitViewport(1920, 1080, cam);
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
		fpsLabel.setFontScale(2);
		fpsLabel.setPosition(0, Gdx.graphics.getHeight()-fpsLabel.getHeight()-10);
		
		
		resultLabel = new Label("Result:", skin);
		resultLabel.setFontScale(2);
		resultLabel.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-fpsLabel.getHeight()-10);
		

		Window window = new Window("Tools",skin);
		window.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//window.setFillParent(true);
		stage.addActor(window);
		paintBtn=new Button(skin);
		paintBtn.add("Paint");
		paintBtn.setColor(1, 0, 0, 1);
		paintBtn.setPosition(150*Gdx.graphics.getDensity(), 100*Gdx.graphics.getDensity()); //** Button location **//
		paintBtn.setHeight(40*Gdx.graphics.getDensity()); //** Button Height **//
		paintBtn.setWidth(40*Gdx.graphics.getDensity()); //** Button Width **//
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
		grabBtn.setPosition(50*Gdx.graphics.getDensity(), 150*Gdx.graphics.getDensity()); //** Button location **//
		grabBtn.setHeight(40*Gdx.graphics.getDensity()); //** Button Height **//
		grabBtn.setWidth(40*Gdx.graphics.getDensity()); //** Button Width **//
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
		stage.addActor(resultLabel);
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(new GestureDetector(this));
		multiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);

		//viewport = new FitViewport(1920, 1080, cam);  

		sketch= new ArrayList<Sprite>();
		drawingData= new ArrayList<Vector2>();
		furnishings= new ArrayList<Item>();

		colour =new Pixmap(1,1,Format.RGBA8888);
		colourDebug =new Pixmap(1,1,Format.RGBA8888);

		colourDebug.setColor(Color.RED);
		colourDebug.fillCircle(0,0, 10);
		colour.setColor(Color.CYAN);
		colour.fill();
		
		pixel=new Sprite(new Texture(colour), 4,4);
		cam.update();
	}

	@Override
	public void resize(int width, int height) {
	//	viewport.update(width, height);
		stage.getViewport().update(width, height);
	}

	@Override
	public void render() {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + " || RAM: "+Gdx.app.getNativeHeap()/131072 +"MB");
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.draw(background, -Gdx.graphics.getWidth()/2,-Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth(),Gdx.graphics.getHeight() );
		for(Sprite pixel:sketch){
			pixel.draw(batch);
		}
		
		for(Item item:furnishings){
			item.drawAll(batch);
		}
		batch.end();

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();

		cam.update();

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		colour.dispose();
		colourDebug.dispose();
		background.dispose();
		stage.dispose();
		
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		gesture();
		return false;
	}

	int fingers =0;
	long wait = 0;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		lastPos = new Vector2(screenX, screenY);
		fingers++;
		if (fingers == 3){
			wait = System.nanoTime();
			gesture();
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (points.size() > 1) {
			strokes.add(new Vector<PointR>(points));
		//	recLabel.setText("Stroke #" + (strokes.size()) + " recorded.");
		}
		drawingData.clear();
		points.clear();
		fingers--;
		return false;
	}

	Vector2 lastPos;
	Vector3 tp;
	int slow = 0;
	@Override
	public boolean touchDragged(int screenX, int screenY, int p) {
		for(Item item:furnishings){
			if(item.touched) return false;
		}
		if(paint && System.nanoTime()-wait > 900000000L){
			slow=0;
			int diffX=screenX-(int)lastPos.x;
			int diffY=screenY-(int)lastPos.y;
			double angle = Math.atan2((double)diffX,(double)diffY)*180/Math.PI; //In degrees
			//Gdx.app.log("diffX", diffX+"");
			//Gdx.app.log("diffY", diffY+"");
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

			//TODO Should probably take current sprite(s) and merge them into one for massive performance increase
		//	pixel=new Sprite(new Texture(colour), (int) (10*Gdx.graphics.getDensity()), (int) (10*Gdx.graphics.getDensity()));
			Sprite debug=new Sprite(new Texture(colour), (int)(BRUSHSIZE*Gdx.graphics.getDensity()), ((int) Math.ceil(c))+5);
			tp = new Vector3(screenX, screenY,0);

			cam.unproject(tp);
			debug.setPosition(tp.x, tp.y);
			debug.setOrigin(debug.getWidth()/2, 0);

			debug.rotate((float) angle);
			debug.flip(true, false);
			sketch.add(debug);

			//Sprite debug2=new Sprite(new Texture(colourDebug), (int) (10*Gdx.graphics.getDensity()), (int) (10*Gdx.graphics.getDensity()));
			tp = new Vector3(screenX, screenY,0);
			cam.unproject(tp);
		//	debug2.setPosition(tp.x, tp.y);
			//	sketch.add(debug2);

			addToDrawing(lastPos, new Vector2(screenX, screenY));
			lastPos.set(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
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
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	
	}
	
	

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
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
		return false;
	}
	//Should probably return a shape object or something
	


	public void addToDrawing(Vector2 from, Vector2 to){
		//Might have to do this twice!
		float diffX=from.x-to.x;
		float diffY=from.y-to.y;
		float step = 0;
		
		points.add(new PointR(from.x,from.y));
		//System.out.println(from.x);
		
		if(diffY!=0)
			if(diffX>diffY)
				step = diffX/diffY;
			else
				step = diffY/diffX;
		//Gdx.app.log("NEW POINT DATA",from.toString()+"::"+ to.toString());
		//TODO Work out all points within these points for use with shape recognition
		if(from.x<to.x){

			for(float x=from.x; x<to.x;x++){
				drawingData.add(new Vector2(x,from.y+=step));
			//	points.add(new PointR(x,Math.round(from.y+=step)));

			}
		}
		else{
			for(float x=from.x; x>to.x;x--){
				drawingData.add(new Vector2(x,from.y+=step));
			//	points.add(new PointR(x,Math.round(from.y+=step)));
			}
		}
	}

	
	public void gesture(){
		if (strokes.size() > 0) {
			Vector<PointR> allPoints = new Vector<PointR>();
			Enumeration<Vector<PointR>> en = strokes.elements();
			while (en.hasMoreElements()) {
				Vector<PointR> pts = en.nextElement();
				allPoints.addAll(pts);
			}
			NBestList result = _rec.Recognize(allPoints, strokes.size());
			String resultTxt;
			if (result.getScore() == -1) {
				
				resultTxt = MessageFormat.format(
						"No Match!\n[{0} out of {1} comparisons made]",
						result.getActualComparisons(),
						result.getTotalComparisons());
				/*
				recLabel.setText("No Match!");*/
			} else {
				resultTxt = MessageFormat
						.format("{0}: {1} ({2}px, {3}{4})  [{5,number,integer} out of {6,number,integer} comparisons made]",
								result.getName(),
								Utils.round(result.getScore(), 2),
								Utils.round(result.getDistance(), 2),
								Utils.round(result.getAngle(), 2),
								(char) 176, result.getActualComparisons(),
								result.getTotalComparisons());
			/*	recLabel.setText("Result: " + result.getName() + " ("
						+ Utils.round(result.getScore(), 2) + ")");*/
			}
			System.out.println(resultTxt);
			
			//fetch name that can be used to match image/ obj
			if(result.getName().replaceAll("[0-9]","").contains("chair")){
				Item i =new Item(chair);				
				i.setBounds(10,10,200,200);
			
				furnishings.add(i);
			}
			if(result.getName().replaceAll("[0-9]","").contains("light")){
				Item i =new Item(light);
				i.setBounds(10,10,100,100);
				furnishings.add(i);
			}
			
			if(result.getName().replaceAll("[0-9]","").contains("sofa")){
				Item i =new Item(sofa);
				i.setBounds(10,10,300,200);
				furnishings.add(i);
			}
			
			if(result.getName().replaceAll("[0-9]","").contains("table")){
				Item i =new Item(table);
				i.setBounds(10,10,300,200);
				furnishings.add(i);
			}
			if(result.getName().replaceAll("[0-9]","").contains("tv")){
				Item i =new Item(tv);
				i.setBounds(10,10,200,200);
				furnishings.add(i);
			}
			
			
			
			
			resultLabel.setText(resultTxt);
			points.clear();
		}
		//Wipe current set for next gesture
		points.clear();
		sketch.clear();
		drawingData.clear();
		strokes.clear();
	}
	
}