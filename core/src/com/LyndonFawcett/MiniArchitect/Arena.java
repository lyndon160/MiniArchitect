package com.LyndonFawcett.MiniArchitect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.LyndonFawcett.MiniArchitect.utils.Notification;
import com.LyndonFawcett.MiniArchitect.utils.ScreenshotFactory;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * 3D implementation and view of room
 * 
 * 
 * @author Lyndon
 *
 *
 * Contains 3D models and debug wires
 */
public class Arena extends Stroke{
	public static CameraInputController camController;
	public CameraInputController camController2;
	static public PerspectiveCamera pCam;
	static public OrthographicCamera oCam;
	public Model model;
	public ModelInstance instance;
	public ModelBatch modelBatch;
	public Environment environment;
	public static AssetManager assets;
	static public Array<ArenaItem> instances; 
	static public ArrayList<ModelInstance> wireFrames; 
	static public ArrayList<ModelInstance> debug; 
	public boolean loading;
	CameraInputController camControl;
	static public Renderable renderable;
	private RenderContext renderContext;
	private DefaultShader shader;
	
	static public HashMap<String, ArrayList<Nodelet>> nodes;
	@Override
	public void show() { 
		//Initialise super class (Too complex for super call)
		createStroke();
		if(instances == null)
			instances = new Array<ArenaItem>();
		if(assets==null)
			loadAssets();
		
		debug= new ArrayList<ModelInstance>();
		System.out.println("Loading arena");
		//lighting
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		wireFrames= new ArrayList<ModelInstance>();

		modelBatch = new ModelBatch();
		pCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		pCam.position.set(0f, 5f, 0f);
		pCam.lookAt(0,0,0);
		pCam.near = 1f;
		pCam.far = 100f;
		pCam.update();


		//Viewport set to fix perspective to orthographic
		oCam = new OrthographicCamera(Gdx.graphics.getWidth()/139, Gdx.graphics.getHeight()/135);
		oCam.position.set(0f, 5f, 0f);
		oCam.lookAt(0,0,0);
		oCam.near = 1f;
		oCam.far = 100f;
		oCam.update();
		/*
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(5f, 5f, 5f, 
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);
		instance.transform.setToTranslation(0, 0, 0);
		 */
		camController = new CameraInputController(pCam);

		loading = true;
		System.out.println(instances.size);


		//Get node point json file 
		final Document doc;
		if(nodes==null){
		nodes = new HashMap<String, ArrayList<Nodelet>>();
		// need http protocol
		try {
			System.out.println("Connecting to site");
			doc = Jsoup.connect("http://lyndonfawcett.me/nodes.json").ignoreContentType(true).get();


			// get all links
			Element jbody = doc.body();
			Json j = new Json();
			
			JsonValue root = new JsonReader().parse(jbody.text().replace(",{}", ""));
			Iterator<JsonValue> i = root.child().iterator();//item
			while(i.hasNext()){
				JsonValue obj = i.next();
				String item = obj.child().asString();
				String connector = obj.child().next().next().asString();
				System.out.println("DOWNLOAD NODES");
				System.out.println("Model name :"+item);
				nodes.put(item, new ArrayList<Nodelet>());
				for(JsonValue jVal:obj.child().next()){
					float x =jVal.child().asFloat();//x
					float z =jVal.child().next().asFloat();//z
					float roty =jVal.child().next().next().asFloat();
					float rotw =jVal.child().next().next().next().asFloat();
					System.out.println(x + "   " + z);
					nodes.get(item).add(new Nodelet(x,z,roty,rotw,connector));
				}
				


			}
		}catch(Exception e){System.out.println(e);};

		}
		for(ArenaItem i:instances)
			System.out.println("Arena instances :"+i.modelName);

	}


	private void doneLoading() {
		loading = false;
	}
	static public void loadAssets(){

		assets = new AssetManager(new LocalFileHandleResolver());
		if (Gdx.app.getType() == ApplicationType.Android){
			FileHandle[] files = Gdx.files.local("downloaded/models").list();
			for(FileHandle file: files) {
				if(file.path().contains(".g3db"))
					assets.load(file.path(), Model.class);
			}
		}
		else{
			FileHandle[] files = Gdx.files.local("downloaded/models").list();
			for(FileHandle file: files) {


				if(file.path().contains(".g3db")){
					System.out.println("Loading :"+file.path());
					assets.load(file.path(), Model.class);
				}
			}
		}


	}

	@Override
	public void addItem(String item){
		
		if(instances.size>10){
			new Notification(stage, skin, new Label("Remove some items before adding more", skin, "24"));
			if(item.equals("wall")){
				//start listening for wall placement
				multiplexer.addProcessor(new WallListener(multiplexer));


			}
			return;
		}
		
		
		System.out.println("ADDING ITEM");
		if(item.equals("wall")){
			//start listening for wall placement
			multiplexer.addProcessor(new WallListener(multiplexer));


		}
		else{
			instances.add(new ArenaItem(assets.get("downloaded/models/"+item, Model.class),item, multiplexer));
			System.out.println("DEUBG!!!!!!!! "+item.replace(".g3db", "").replaceAll("\\D+","")!="");
			if(item.contains("QQQ"))
				runningCost.setText("£"+ (Integer.parseInt(runningCost.getText().toString().replaceAll("\\D+",""))+	 Integer.parseInt(item.replace(".g3db", "").replaceAll("\\D+",""))));
		}
	}



	@Override
	void arenaRender() {
		if (loading && assets.update())
			doneLoading();

		if(pCamOn)
			modelBatch.begin(pCam);
		else
			modelBatch.begin(oCam);

		modelBatch.render(instances, environment);
		modelBatch.render(wireFrames, environment);
		modelBatch.render(debug, environment);
		modelBatch.end();


		oCam.update();
		pCam.update();

	}
	@Override
	public void arenaDispose(){
		model.dispose();
		
		Arena.debug.clear();
		Arena.instances.clear();
		Arena.wireFrames.clear();		
	}

	public void captureScreen(){
		ScreenshotFactory.saveScreenshot();
	}


	@Override
	public void hide() {
		instances.clear();
	}





}
