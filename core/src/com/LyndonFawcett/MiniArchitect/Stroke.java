package com.LyndonFawcett.MiniArchitect;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import com.LyndonFawcett.MiniArchitect.UI.PublishWindow;
import com.LyndonFawcett.MiniArchitect.screens.MenuScreen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

import dollarN.NBestList;
import dollarN.NDollarRecognizer;
import dollarN.PointR;
import dollarN.Utils;

public abstract class Stroke implements Screen, InputProcessor, GestureListener{

	private static final float BRUSHSIZE = 10;
	SpriteBatch batch;
	static ArrayList<Sprite> sketch;



	public static ArrayList<Item> furnishings;

	public static InputMultiplexer multiplexer;

	public static ArrayList<Vector2> drawingData;
	Vector<PointR> points = new Vector<PointR>();
	Vector<Vector<PointR>> strokes = new Vector<Vector<PointR>>();
	Sprite pixel;
	Pixmap colour, colourDebug;
	Texture background;
	public static OrthographicCamera cam;
	public float zoom = 1.0f;
	int SPRITECOUNT=400;
	int pointer=0;
	protected Stage stage;
	public Button paintBtn,grabBtn;
	public static Button deleteBox;
	Texture table,chair,light,tv,sofa;
	Skin skin,skinText,skinNoBar;
	Label fpsLabel;
	Label resultLabel;
	NDollarRecognizer _rec = null;
	static Boolean grab=false;
	public static Boolean paint=false;
	Texture tableTexture;
	long loadstart;
	public Button camBtn;
	boolean pCamOn = true;
	Table menu;
	Texture texture1;
	int	camPointer;
	Window window;
	public static void wipeDrawings(){
		sketch.clear();
	}

	public void createStroke() {    
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				Gdx.app.log("Time", System.currentTimeMillis()+"");
				//Load gestures

				_rec.LoadGesture(Gdx.files.getFileHandle("gestures/wall.xml", FileType.Internal).read());
				_rec.LoadGesture(Gdx.files.getFileHandle("gestures/light.xml", FileType.Internal).read());
				_rec.LoadGesture(Gdx.files.getFileHandle("gestures/table.xml", FileType.Internal).read());
				_rec.LoadGesture(Gdx.files.getFileHandle("gestures/table2.xml", FileType.Internal).read());
				//_rec.LoadGesture(Gdx.files.getFileHandle("table3.xml", FileType.Internal).read());
				//_rec.LoadGesture(Gdx.files.getFileHandle("table4.xml", FileType.Internal).read());
				//_rec.LoadGesture(Gdx.files.getFileHandle("tv.xml", FileType.Internal).read()); //takes 3 seconds to load
				_rec.LoadGesture(Gdx.files.getFileHandle("gestures/chair2.xml", FileType.Internal).read());
				_rec.LoadGesture(Gdx.files.getFileHandle("gestures/wall.xml", FileType.Internal).read());
				_rec.LoadGesture(Gdx.files.getFileHandle("gestures/wall2.xml", FileType.Internal).read());
				_rec.LoadGesture(Gdx.files.getFileHandle("gestures/chair.xml", FileType.Internal).read());
				//_rec.LoadGesture(Gdx.files.getFileHandle("circle.xml", FileType.Internal).read());
			}
		});


		loadstart = System.currentTimeMillis();
		_rec = new NDollarRecognizer();
		//Load images
		table = new Texture(Gdx.files.internal("sketches/table.png"));
		chair = new Texture(Gdx.files.getFileHandle("sketches/chair.png", FileType.Internal), false);
		light = new Texture(Gdx.files.getFileHandle("sketches/light.png", FileType.Internal), false);
		tv = new Texture(Gdx.files.getFileHandle("sketches/TV.png", FileType.Internal), false);
		sofa = new Texture(Gdx.files.getFileHandle("sketches/sofa.png", FileType.Internal), false);


		batch = new SpriteBatch();
		background = new Texture(Gdx.files.getFileHandle("blueprint.jpg", FileType.Internal), false);

		//tableTexture = new Texture(Gdx.files.internal("Table.png"));

		//setup ui
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		//viewport = new FitViewport(1920, 1080, cam);
		batch.setProjectionMatrix(cam.combined);
		//	Gdx.input.setInputProcessor(this);
		//	Gdx.input.setInputProcessor(new GestureDetector(this));
		skin = new Skin(Gdx.files.internal("ChalkUi/uiskin.json"));
		skinText = new Skin(Gdx.files.internal("ChalkUi/uiskin.json"));
		skinNoBar = new Skin(Gdx.files.internal("ChalkUi/uiskin.json"), new TextureAtlas("ChalkUi/uiskin.atlas"));
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui-blue.atlas"));
		skin.addRegions(atlas);
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		fpsLabel = new Label("fps:", skinText, "24");
		stage = new Stage(new ScreenViewport());
		fpsLabel.setPosition(0, Gdx.graphics.getHeight()-fpsLabel.getHeight()-10);




		resultLabel = new Label("Result:", skinText, "24");
		resultLabel.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-fpsLabel.getHeight()-10);


		ArrayList<String> furn = new ArrayList<String>();
		if (Gdx.app.getType() == ApplicationType.Android){

			FileHandle[] files = Gdx.files.local("downloaded/models").list();
			for(FileHandle file: files) {
				System.out.println(file.name());
				furn.add(file.name());
			}
		}
		else{
			FileHandle[] files = Gdx.files.local("downloaded/models").list();
			for(FileHandle file: files) {
				System.out.println(file.name());
				furn.add(file.name());
			}
		}
		//Furniture scroll pane
		final ScrollPane scroll = new ScrollPane(null, skinNoBar,"transparent");
		ArrayList<Label> list = new ArrayList<Label>();

		//Create tree and nodes
		//Tree tree = new Tree(skin);
		ArrayList<TextButton> foundation = new ArrayList<TextButton>();
		ArrayList<TextButton> misc = new ArrayList<TextButton>();
		ArrayList<TextButton> livingroom = new ArrayList<TextButton>();
		ArrayList<TextButton> bedroom = new ArrayList<TextButton>();
		ArrayList<TextButton> bathroom = new ArrayList<TextButton>();
		ArrayList<TextButton> diningroom = new ArrayList<TextButton>();
		ArrayList<TextButton> kitchen = new ArrayList<TextButton>();
		ArrayList<TextButton> office = new ArrayList<TextButton>();


		for(final String s:furn){
			TextButton listItem = new TextButton(("\n"+
		s.replaceAll(".g3db", "")+"\n\n").replaceAll("", "").replaceAll("_", "")
		.replaceFirst("table", "").replaceFirst("chair", "").replaceFirst("tv", "").replaceFirst("sofa", "")
		.replace("livingroom", "").replace("diningroom", "").replaceFirst("bathroom", "").replace("kitchen", "")
		.replaceFirst("office", "").replace("foundation", "").replace("bedroom", "").replaceFirst("bed", "").trim()
					, skinText,"list");
			//Check that it is actually a model file that has been downloaded
			if(s.contains("g3db"))
				if(s.contains("livingroom")){
					System.out.println("living room item added :" +s);
					livingroom.add(listItem);
				}
				else if(s.contains("diningroom")){
					diningroom.add(listItem);
				}
				else if(s.contains("bedroom")){
					bedroom.add(listItem);
				}
				else if(s.contains("bathroom")){
					bathroom.add(listItem);
				}
				else if(s.contains("office")){
					office.add(listItem);
				}
				else if(s.contains("kitchen")){
					kitchen.add(listItem);
				}
				else if(s.contains("foundation")){
					foundation.add(listItem);
				}
				//misc item
				else {
					System.out.println("WARNING misc item added :" +s);
					misc.add(listItem);
				}
				
			//listItem.setAlignment(Align.center);
			listItem.addListener(new InputListener(){
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					return true;
			 	}
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					if(!scroll.isFlinging()&&!scroll.isDragging()&&!scroll.isPanning()){
						Gdx.app.log("Model added", s);
						addItem(s);
					}
				}
			});
			
			//add new item to list
			//list.add(listItem);
		}

		//wall added to foundation section
		TextButton listItem = new TextButton("\n"+"Wall"+"\n\n", skin, "list");
		//	listItem.setFontScale(2);
		//listItem.debug();
		//listItem.setAlignment(Align.center);
		//Wall listener
		listItem.addListener(new InputListener(){
			boolean creating = false;
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
		 	}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(!scroll.isFlinging()&&!scroll.isDragging()&&!scroll.isPanning()){
					Gdx.app.log("Starting", "wall");
					addItem("wall");
					creating = true;
					//draw wall here
				}
			}
		});
		foundation.add(listItem);




		window = new Window("",skin, "chalk");
		window.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		window.setHeight(Gdx.graphics.getHeight());
		window.setWidth(Gdx.graphics.getWidth()/4);
		window.setResizable(false);
		window.setMovable(false);

		Table titleTable = new Table(skin);

		final Table livingroomcontent = new Table(skin);
		final Table foundationcontent = new Table(skin);
		final Table diningroomcontent = new Table(skin);
		final Table bedroomcontent = new Table(skin);
		final Table officecontent = new Table(skin);
		final Table kitchencontent = new Table(skin);
		final Table bathroomcontent = new Table(skin);
		final Table misccontent = new Table(skin);
		
		float padding = 50;
		for(TextButton l:livingroom)
			livingroomcontent.add(l).width(Gdx.graphics.getWidth()/6).padBottom(padding).row();
		for(TextButton l:kitchen)
			kitchencontent.add(l).width(Gdx.graphics.getWidth()/6).padBottom(padding).row();
		for(TextButton l:diningroom)
			diningroomcontent.add(l).width(Gdx.graphics.getWidth()/6).padBottom(padding).row();
		for(TextButton l:bedroom)
			bedroomcontent.add(l).width(Gdx.graphics.getWidth()/6).padBottom(padding).row();
		for(TextButton l:office)
			officecontent.add(l).width(Gdx.graphics.getWidth()/6).padBottom(padding).row();
		for(TextButton l:bathroom)
			bathroomcontent.add(l).width(Gdx.graphics.getWidth()/6).padBottom(padding).row();
		for(TextButton l:foundation)
			foundationcontent.add(l).width(Gdx.graphics.getWidth()/6).padBottom(padding).row();
		for(TextButton l:misc)
			misccontent.add(l).width(Gdx.graphics.getWidth()/6).padBottom(padding).row();
		misccontent.pad(10,100,10,100);
		foundationcontent.pad(10,100,10,100);
		livingroomcontent.pad(10,100,10,100);
		officecontent.pad(10,100,10,100);
		diningroomcontent.pad(10,100,10,100);
		kitchencontent.pad(10,100,10,100);
		bedroomcontent.pad(10,100,10,100);
		ImageButton backButton = new ImageButton(skin, "back");

		ImageButton publish = new ImageButton(skin, "publish");
		final PublishWindow pubWin = new PublishWindow(skin);
		publish.setSize(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
		publish.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
		 	}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				//open window to publish created room
				stage.addActor(pubWin);
				pubWin.setVisible(true);
			}
		});
		
		
		publish.setPosition((Gdx.graphics.getWidth()/2)-publish.getWidth()/2, Gdx.graphics.getHeight()-(publish.getHeight()+10));
		stage.addActor(publish);

		titleTable.add(backButton).size(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10).align(Align.left).padRight(20);
		final Label title = new Label("            HOME", skin, "24");
		titleTable.add(title);
		
		
		
		
		
		final Table homecontent = new Table(skin);
		
		
		backButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				scroll.setWidget(homecontent);
				title.setText("            HOME");
				return true;
		 	}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});



		String categories[] = {"LIVING ROOM","KITCHEN","DINING ROOM","BEDROOM","OFFICE","BATHROOM","FOUNDATION","MISC"};
		for(final String s:categories){
		TextButton l = new TextButton(s,skin,"black");
		l.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
		 	}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("label " +s);
				if(!scroll.isFlinging()&&!scroll.isDragging()&&!scroll.isPanning()){

					if(s.contains("LIVING ROOM")){
						scroll.setWidget(livingroomcontent);
						title.setText("LIVING ROOM");
					}
					if(s.contains("BEDROOM")){
						scroll.setWidget(bedroomcontent);
						title.setText("     BEDROOM");
					}
					if(s.contains("KITCHEN")){
						scroll.setWidget(kitchencontent);
						title.setText("      KITCHEN");
					}
					if(s.contains("BATHROOM")){
						scroll.setWidget(bathroomcontent);
						title.setText("LIVING ROOM");
					}
					if(s.contains("DINING ROOM")){
						scroll.setWidget(diningroomcontent);
						title.setText("DINING ROOM");
					}
					if(s.contains("OFFICE")){
						scroll.setWidget(officecontent);
						title.setText("    OFFICE");
					}
					if(s.contains("FOUNDATION")){
						scroll.setWidget(foundationcontent);
						title.setText(" FOUNDATION");
					}
					if(s.contains("MISC")){
						scroll.setWidget(misccontent);
						title.setText("          MISC");
					}
					
				}
			}
		});
		homecontent.add(l).width(Gdx.graphics.getWidth()/6).padBottom(50).row();

		}
		
		
/*
		//add list of models to content
		for(Label l:list){
			content.add(l).minWidth(Gdx.graphics.getWidth()/6).minHeight(Gdx.graphics.getWidth()/6).fill();
			content.row();
		}
		
		*/
		
		homecontent.setHeight(Gdx.graphics.getHeight());

		

		
		scroll.setWidget(homecontent); 
		//scroll.getV
		scroll.setScrollingDisabled(true, false);
		//scroll.addListener(listener)
		//scroll.setHeight(Gdx.graphics.getHeight());
		//scroll.setWidth(Gdx.graphics.getDensity()*800);
		Table wrapper = new Table(skin);
		wrapper.add(titleTable).padRight(20).align(Align.left).row();
		wrapper.add(scroll);
		window.add(wrapper);
		window.align(Align.top);
		deleteBox= new ImageButton(skin,"delete");
		//deleteBox.add(new Label("DELETE",skin,"24"));
		//deleteBox.setColor(1, 0, 0, 1);
		deleteBox.setTouchable(Touchable.disabled);
		deleteBox.setBounds(0,Gdx.graphics.getHeight()-deleteBox.getHeight(),
				128,128);



		paintBtn=new Button(skin);
		paintBtn.add(new Label("Paint",skinText, "24"));
		paintBtn.setColor(1, 0, 0, 1);
		paintBtn.setPosition(100*Gdx.graphics.getDensity(), 50*Gdx.graphics.getDensity()); //** Button location **//
		paintBtn.setHeight(40*Gdx.graphics.getDensity()); //** Button Height **//
		paintBtn.setWidth(40*Gdx.graphics.getDensity()); //** Button Width **//
		paintBtn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(paint)
					paint=false;
				else
					paint= true;
				grab=false;
			}
		});

		camBtn=new Button(skin);
		camBtn.add(new Label("2D/3D",skinText, "24"));
		camBtn.setColor(0, 0, 0, 1);
		camBtn.setPosition(25*Gdx.graphics.getDensity(), 300*Gdx.graphics.getDensity()); //** Button location **//
		camBtn.setHeight(40*Gdx.graphics.getDensity()); //** Button Height **//
		camBtn.setWidth(40*Gdx.graphics.getDensity()); //** Button Width **//
		camBtn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(pCamOn)
					pCamOn = false;
				else
					pCamOn = true;
			}
		});

		grabBtn=new Button(skin);
		grabBtn.add(new Label("Grab",skinText, "24"));
		grabBtn.setColor(0, 0, 1, 1);
		grabBtn.setPosition(25*Gdx.graphics.getDensity(), 150*Gdx.graphics.getDensity()); //** Button location **//
		grabBtn.setHeight(40*Gdx.graphics.getDensity()); //** Button Height **//
		grabBtn.setWidth(40*Gdx.graphics.getDensity()); //** Button Width **//
		grabBtn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(grab){
					grab =false;
					multiplexer.removeProcessor(camPointer);
				}
				else{
					camPointer=multiplexer.size();
					multiplexer.addProcessor(camPointer, Arena.camController);
					grab=true;
				}
				paint=false;
			}
		});

		stage.addActor(deleteBox);
		stage.addActor(camBtn);
		stage.addActor(grabBtn);
		stage.addActor(paintBtn);
		stage.addActor(fpsLabel);
		stage.addActor(resultLabel);
		stage.addActor(window);
		if(multiplexer == null)
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


		Gdx.app.log("Loading time", System.currentTimeMillis() - loadstart  + "ms");
	}

	@Override
	public void resize(int width, int height) {
		//	viewport.update(width, height);
		//	stage.getViewport().update(width, height);
	}


	public void addItem(String name){

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + " || RAM USED: "+Gdx.app.getJavaHeap()/131072 +"MB");
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.draw(background, -Gdx.graphics.getWidth()/2,-Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		for(Sprite pixel:sketch){
			pixel.draw(batch);
		}

		for(Item item:furnishings){
			item.drawAll(batch);
		}
		batch.end();
		arenaRender();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();

		cam.update();

	}

	abstract void arenaRender();

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
		multiplexer.clear();
		this.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.BACK || keycode == Keys.BACKSPACE){
			((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
		}
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

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
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

		for(Item item:furnishings){
			if(item.touched) return false;
		}
		if(paint && System.nanoTime()-wait > 900000000L){
			slow=0;
			int diffX=(int) (x-(int)lastPos.x);
			int diffY=(int) (y-(int)lastPos.y);
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


			//	pixel=new Sprite(new Texture(colour), (int) (10*Gdx.graphics.getDensity()), (int) (10*Gdx.graphics.getDensity()));
			Sprite debug=new Sprite(new Texture(colour), (int)(BRUSHSIZE*Gdx.graphics.getDensity()), ((int) Math.ceil(c))+5);
			tp = new Vector3(x, y,0);

			cam.unproject(tp);
			debug.setPosition(tp.x, tp.y);
			debug.setOrigin(debug.getWidth()/2, 0);

			debug.rotate((float) angle);
			debug.flip(true, false);
			sketch.add(debug);

			//Sprite debug2=new Sprite(new Texture(colourDebug), (int) (10*Gdx.graphics.getDensity()), (int) (10*Gdx.graphics.getDensity()));
			tp = new Vector3(x, y,0);
			cam.unproject(tp);
			//	debug2.setPosition(tp.x, tp.y);
			//	sketch.add(debug2);

			addToDrawing(lastPos, new Vector2(x, y));
			lastPos.set(x, y);
		}

		return false;

	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {

		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		/*	if(grab){
			//Calculate pinch to zoom
			float ratio = initialDistance / distance;

			//Clamp range and set zoom
			cam.zoom = MathUtils.clamp(initialScale * ratio, 0.1f, 1.0f);
			cam.update();
			Gdx.app.log("Zoom", distance+"");
		}
		 */
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
		try{
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
		}catch(Exception e){ Gdx.app.log("GESTURE ERROR", e.toString());}
	} 
}