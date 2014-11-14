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
	double[] square={4.0,18.0,5.0,38.0,4.0,43.0,3.0,52.0,4.0,59.0,3.0,76.0,4.0,84.0,5.0,84.0,6.0,84.0,7.0,84.0,8.0,84.0,9.0,84.0,10.0,84.0,11.0,84.0,12.0,84.0,13.0,84.0,14.0,84.0,15.0,84.0,16.0,84.0,17.0,84.0,18.0,84.0,19.0,84.0,20.0,85.0,21.0,85.0,22.0,85.0,23.0,85.0,24.0,86.0,25.0,86.0,26.0,86.0,27.0,86.0,28.0,86.0,29.0,86.0,30.0,86.0,31.0,86.0,32.0,86.0,33.0,86.0,34.0,86.0,35.0,86.0,36.0,86.0,37.0,86.0,38.0,86.0,39.0,86.0,40.0,86.0,41.0,86.0,42.0,86.0,43.0,86.0,44.0,86.0,45.0,86.0,46.0,86.0,47.0,86.0,48.0,86.0,49.0,86.0,50.0,86.0,51.0,86.0,52.0,86.0,53.0,85.0,54.0,85.0,55.0,85.0,56.0,85.0,57.0,85.0,58.0,85.0,59.0,85.0,60.0,85.0,61.0,85.0,62.0,85.0,63.0,85.0,64.0,85.0,65.0,85.0,66.0,85.0,67.0,85.0,68.0,85.0,69.0,85.0,70.0,85.0,71.0,85.0,72.0,85.0,73.0,85.0,74.0,82.0,75.0,67.0,76.0,61.0,77.0,59.0,78.0,11.0,77.0,12.0,76.0,10.0,75.0,10.0,74.0,10.0,73.0,10.0,72.0,10.0,71.0,10.0,70.0,10.0,69.0,10.0,68.0,10.0,67.0,10.0,66.0,10.0,65.0,10.0,64.0,10.0,63.0,10.0,62.0,10.0,61.0,10.0,60.0,10.0,59.0,10.0,58.0,10.0,57.0,10.0,56.0,10.0,55.0,10.0,54.0,10.0,53.0,10.0,52.0,10.0,51.0,10.0,50.0,10.0,49.0,10.0,48.0,10.0,47.0,10.0,46.0,10.0,45.0,10.0,44.0,10.0,43.0,10.0,42.0,10.0,41.0,10.0,40.0,10.0,39.0,10.0,38.0,10.0,37.0,10.0,36.0,11.0,35.0,9.0,34.0,9.0,33.0,9.0,32.0,9.0,31.0,9.0,30.0,9.0,29.0,9.0,28.0,9.0,27.0,9.0,26.0,9.0,25.0,9.0,24.0,9.0,23.0,9.0,22.0,9.0,21.0,9.0,20.0,9.0,19.0,9.0,18.0,9.0,17.0,9.0,16.0,9.0,15.0,9.0,14.0,9.0,13.0,9.0,12.0,9.0,11.0,9.0,10.0,9.0,9.0,9.0,8.0,9.0,7.0,9.0,6.0,9.0,5.0,9.0};
	double[] triangle={6.0,102.0,7.0,102.0,8.0,102.0,9.0,102.0,10.0,102.0,11.0,102.0,12.0,102.0,13.0,102.0,14.0,102.0,15.0,103.0,16.0,103.0,17.0,103.0,18.0,103.0,19.0,103.0,20.0,103.0,21.0,103.0,22.0,103.0,23.0,103.0,24.0,103.0,25.0,103.0,26.0,103.0,27.0,103.0,28.0,103.0,29.0,103.0,30.0,103.0,31.0,104.0,32.0,104.0,33.0,104.0,34.0,104.0,35.0,104.0,36.0,104.0,37.0,104.0,38.0,104.0,39.0,104.0,40.0,104.0,41.0,104.0,42.0,104.0,43.0,104.0,44.0,104.0,45.0,104.0,46.0,104.0,47.0,104.0,48.0,104.0,49.0,104.0,50.0,104.0,51.0,104.0,52.0,104.0,53.0,103.0,54.0,103.0,55.0,103.0,56.0,103.0,57.0,103.0,58.0,103.0,59.0,103.0,60.0,103.0,61.0,103.0,62.0,103.0,63.0,103.0,64.0,103.0,65.0,103.0,66.0,103.0,67.0,103.0,68.0,103.0,69.0,103.0,70.0,103.0,71.0,103.0,72.0,103.0,73.0,103.0,74.0,103.0,75.0,103.0,76.0,103.0,77.0,103.0,78.0,103.0,79.0,103.0,80.0,103.0,81.0,103.0,82.0,103.0,83.0,103.0,84.0,103.0,85.0,103.0,86.0,103.0,87.0,103.0,88.0,103.0,89.0,103.0,90.0,103.0,91.0,103.0,92.0,103.0,93.0,103.0,94.0,103.0,95.0,103.0,96.0,103.0,97.0,103.0,98.0,103.0,99.0,103.0,100.0,103.0,101.0,103.0,102.0,103.0,103.0,103.0,104.0,103.0,105.0,103.0,106.0,103.0,107.0,103.0,108.0,103.0,109.0,103.0,110.0,103.0,111.0,102.0,110.0,101.0,109.0,100.0,108.0,99.0,107.0,96.0,106.0,96.0,105.0,94.0,104.0,93.0,103.0,94.0,102.0,92.0,101.0,90.0,100.0,91.0,99.0,90.0,98.0,88.0,97.0,89.0,96.0,88.0,95.0,87.0,94.0,86.0,93.0,84.0,92.0,85.0,91.0,82.0,90.0,81.0,89.0,82.0,88.0,81.0,87.0,79.0,86.0,78.0,85.0,77.0,84.0,75.0,83.0,74.0,82.0,72.0,81.0,70.0,80.0,69.0,79.0,67.0,78.0,66.0,77.0,65.0,76.0,62.0,75.0,60.0,74.0,58.0,73.0,56.0,72.0,55.0,71.0,51.0,70.0,50.0,69.0,45.0,68.0,46.0,67.0,43.0,66.0,42.0,65.0,41.0,64.0,40.0,63.0,40.0,62.0,35.0,61.0,34.0,60.0,33.0,59.0,31.0,58.0,30.0,57.0,26.0,56.0,27.0,55.0,25.0,54.0,24.0,53.0,21.0,52.0,20.0,51.0,18.0,50.0,13.0,49.0,8.0,48.0,7.0,47.0,7.0,46.0,7.0,45.0,8.0,44.0,10.5,43.0,13.0,42.0,14.0,41.0,16.0,40.0,19.0,39.0,20.0,38.0,23.0,37.0,27.0,36.0,28.5,35.0,31.0,34.0,34.0,33.0,36.0,32.0,42.0,31.0,43.0,30.0,46.0,29.0,48.0,28.0,50.0,27.0,51.0,26.0,53.0,25.0,56.0,24.0,58.0,23.0,59.0,22.0,63.0,21.0,66.0,20.0,67.0,19.0,68.0,18.0,72.0,17.0,72.0,16.0,74.0,15.0,77.0,14.0,78.0,13.0,80.0,12.0,82.0,11.0,86.0,10.0,86.0,9.0,90.0,8.0,94.0};
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


		fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + " || RAM: "+Gdx.app.getNativeHeap()/131072 +"MB");
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
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
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
		computeShape();
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
		return false;
	}

	Vector2 lastPos;
	Vector3 tp;
	int slow = 0;
	@Override
	public boolean touchDragged(int screenX, int screenY, int p) {
		if(paint){
			slow=0;
			//int x=screenX;
			//int y=screenY;
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

			//Gdx.app.log("Length", c+"");
			//Gdx.app.log("Angle", angle+"");
			//float steps=0;
			//TODO Should probably take current sprite(s) and merge them into one for massive performance increase
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
	
	public String computeShape(){
		//get the bottom left top position // least on x and y // Normalise data
		double y = drawingData.get(0).y;
		double x = drawingData.get(0).x;
		for(Vector2 pos:drawingData){
			if(pos.y<y)
				y=pos.y;
			if(pos.x<x)
				x=pos.x;
		}
		int count = 0;
		int countT= 0;
		for(Vector2 pos:drawingData){
			for(Double p:square){
				if(p%2==0)
					if(pos.x > p && pos.x < p+5 || pos.x < p && pos.x > p-5)
						count++;
				else
					if(pos.y > p && pos.y < p+5 || pos.y < p && pos.y > p-5)
						count++;
			}
			
			for(Double p:triangle){
				if(p%2==0)
					if(pos.x > p && pos.x < p+5 || pos.x < p && pos.x > p-5)
						countT++;
				else
					if(pos.y > p && pos.y < p+5 || pos.y < p && pos.y > p-5)
						countT++;
			}
		}
		Gdx.app.log("\nMatch amount Square", count*1.6+"");
		Gdx.app.log("Match amount Triangle", countT+"");
		return null;
		
	}
	

	public void addToDrawing(Vector2 from, Vector2 to){
		//Might have to do this twice!
		float diffX=from.x-to.x;
		float diffY=from.y-to.y;
		float step = 0;
		
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
				System.out.print(x+","+from.y+",");
			}
		}
		else{
			for(float x=from.x; x>to.x;x--){
				drawingData.add(new Vector2(x,from.y+=step));
				System.out.print(x+","+from.y+",");
			}
		}
	}

}