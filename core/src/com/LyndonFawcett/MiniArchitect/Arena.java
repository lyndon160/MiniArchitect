package com.LyndonFawcett.MiniArchitect;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

/**
 * 3D implementation and view of room
 * 
 * 
 * @author Lyndon
 *
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
	public boolean loading;
	CameraInputController camControl;
	@Override
	public void show() { 
		//Initialise super class (Too complex for super call)
		createStroke();
		if(instances == null)
			instances = new Array<ArenaItem>();
		if(assets==null)
			loadAssets();
		System.out.println("Loading arena");
		//lighting
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

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

		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(5f, 5f, 5f, 
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);
		instance.transform.setToTranslation(0, 0, 0);
	
		camController = new CameraInputController(pCam);

		loading = true;
		System.out.println(instances.size);
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
		if(item.equals("wall")){
			//start listening for wall placement
			multiplexer.addProcessor(new WallListener(multiplexer));
			
			
		}
		else
			instances.add(new ArenaItem(assets.get("downloaded/models/"+item, Model.class),item, multiplexer));
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
		modelBatch.end();

		oCam.update();
		pCam.update();

	}
	




	@Override
	public void hide() {
		instances.clear();
		stage.dispose();
	}





}
